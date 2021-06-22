package com.destroystokyo.paper.util;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.SectionPos;
import org.spigotmc.AsyncCatcher;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Spottedleaf
 */
public final class PlayerMobDistanceMap {

	private static final PooledHashSets.PooledObjectLinkedOpenHashSet<ServerPlayerEntity> EMPTY_SET = new PooledHashSets.PooledObjectLinkedOpenHashSet<>();

	private final Map<ServerPlayerEntity, SectionPos> players = new HashMap<>();
	// we use linked for better iteration.
	private final Long2ObjectOpenHashMap<PooledHashSets.PooledObjectLinkedOpenHashSet<ServerPlayerEntity>> playerMap = new Long2ObjectOpenHashMap<>(32, 0.5f);
	private final PooledHashSets<ServerPlayerEntity> pooledHashSets = new PooledHashSets<>();
	private int viewDistance;

	public PooledHashSets.PooledObjectLinkedOpenHashSet<ServerPlayerEntity> getPlayersInRange(final ChunkPos chunkPos) {
		return this.getPlayersInRange(chunkPos.x, chunkPos.z);
	}

	public PooledHashSets.PooledObjectLinkedOpenHashSet<ServerPlayerEntity> getPlayersInRange(final int chunkX, final int chunkZ) {
		return this.playerMap.getOrDefault(ChunkPos.asLong(chunkX, chunkZ), EMPTY_SET);
	}

	public void update(final List<ServerPlayerEntity> currentPlayers, final int newViewDistance) {
		AsyncCatcher.catchOp("Distance map update");
		final ObjectLinkedOpenHashSet<ServerPlayerEntity> gone = new ObjectLinkedOpenHashSet<>(this.players.keySet());

		final int oldViewDistance = this.viewDistance;
		this.viewDistance = newViewDistance;

		for (final ServerPlayerEntity player : currentPlayers) {
			if(player.isSpectator() /*|| !player.affectsSpawning*/) {
				continue; // will be left in 'gone' (or not added at all)
			}

			gone.remove(player);

			final SectionPos newPosition = player.getPlayerMapSection();
			final SectionPos oldPosition = this.players.put(player, newPosition);

			if(oldPosition == null) {
				this.addNewPlayer(player, newPosition, newViewDistance);
			} else {
				this.updatePlayer(player, oldPosition, newPosition, oldViewDistance, newViewDistance);
			}
			//this.validatePlayer(player, newViewDistance); // debug only
		}

		for (final ServerPlayerEntity player : gone) {
			final SectionPos oldPosition = this.players.remove(player);
			if(oldPosition != null) {
				this.removePlayer(player, oldPosition, oldViewDistance);
			}
		}
	}

	// expensive op, only for debug
	private void validatePlayer(final ServerPlayerEntity player, final int viewDistance) {
		int entiesGot = 0;
		int expectedEntries = (2 * viewDistance+ 1);
		expectedEntries *= expectedEntries;

		final SectionPos currPosition = player.getPlayerMapSection();

		final int centerX = currPosition.getX();
		final int centerZ = currPosition.getZ();

		for (final Long2ObjectLinkedOpenHashMap.Entry<PooledHashSets.PooledObjectLinkedOpenHashSet<ServerPlayerEntity>> entry : this.playerMap.long2ObjectEntrySet()) {
			final long key = entry.getLongKey();
			final PooledHashSets.PooledObjectLinkedOpenHashSet<ServerPlayerEntity> map = entry.getValue();

			if(map.referenceCount == 0) {
				throw new IllegalStateException("Invalid map");
			}

			if(map.set.contains(player)) {
				++entiesGot;

				final int chunkX = ChunkPos.getX(key);
				final int chunkZ = ChunkPos.getZ(key);

				final int dist = Math.max(Math.abs(chunkX - centerX), Math.abs(chunkZ - centerZ));

				if(dist > viewDistance) {
					throw new IllegalStateException("Expected view distance "+viewDistance+", got "+dist);
				}
			}
		}

		if(entiesGot != expectedEntries) {
			throw new IllegalStateException("Expected "+expectedEntries+", got "+entiesGot);
		}
	}

	private void addPlayerTo(final ServerPlayerEntity player, final int chunkX, final int chunkZ) {
		this.playerMap.compute(ChunkPos.asLong(chunkX, chunkZ), (final Long key, final PooledHashSets.PooledObjectLinkedOpenHashSet<ServerPlayerEntity> players) -> {
			if(players == null) {
				return player.cachedSingleMobDistanceMap;
			} else {
				return PlayerMobDistanceMap.this.pooledHashSets.findMapWith(players, player);
			}
		});
	}

	private void removePlayerFrom(final ServerPlayerEntity player, final int chunkX, final int chunkZ) {
		this.playerMap.compute(ChunkPos.asLong(chunkX, chunkZ), (final Long keyInMap, final PooledHashSets.PooledObjectLinkedOpenHashSet<ServerPlayerEntity> players) -> {
			return PlayerMobDistanceMap.this.pooledHashSets.findMapWithout(players, player); // rets null instead of an empty map
		});
	}

	private void updatePlayer(final ServerPlayerEntity player, final SectionPos oldPosition, final SectionPos newPosition, final int oldViewDistance, final int newViewDistance) {
		final int toX = newPosition.getX();
		final int toZ = newPosition.getZ();
		final int fromX = oldPosition.getX();
		final int fromZ = oldPosition.getZ();

		final int dx = toX - fromX;
		final int dz = toZ - fromZ;

		final int totalX = Math.abs(fromX - toX);
		final int totalZ = Math.abs(fromZ - toZ);

		if(Math.max(totalX, totalZ) > (2 * oldViewDistance)) {
			// teleported?
			this.removePlayer(player, oldPosition, oldViewDistance);
			this.addNewPlayer(player, newPosition, newViewDistance);
			return;
		}

		// x axis is width
		// z axis is height
		// right refers to the x axis of where we moved
		// top refers to the z axis of where we moved

		if(oldViewDistance == newViewDistance) {
			// same view distance

			// used for relative positioning
			final int up = 1 | (dz >> (Integer.SIZE - 1)); // 1 if dz >= 0, -1 otherwise
			final int right = 1 | (dx >> (Integer.SIZE - 1)); // 1 if dx >= 0, -1 otherwise

			// The area excluded by overlapping the two view distance squares creates four rectangles:
			// Two on the left, and two on the right. The ones on the left we consider the "removed" section
			// and on the right the "added" section.
			// https://i.imgur.com/MrnOBgI.png is a reference image. Note that the outside border is not actually
			// exclusive to the regions they surround.

			// 4 points of the rectangle
			int maxX; // exclusive
			int minX; // inclusive
			int maxZ; // exclusive
			int minZ; // inclusive

			if(dx != 0) {
				// handle right addition

				maxX = toX + (oldViewDistance * right) + right; // exclusive
				minX = fromX + (oldViewDistance * right) + right; // inclusive
				maxZ = fromZ + (oldViewDistance * up) + up; // exclusive
				minZ = toZ - (oldViewDistance * up); // inclusive

				for (int currX = minX; currX != maxX; currX += right) {
					for (int currZ = minZ; currZ != maxZ; currZ += up) {
						this.addPlayerTo(player, currX, currZ);
					}
				}
			}

			if(dz != 0) {
				// handle up addition

				maxX = toX + (oldViewDistance * right) + right; // exclusive
				minX = toX - (oldViewDistance * right); // inclusive
				maxZ = toZ + (oldViewDistance * up) + up; // exclusive
				minZ = fromZ + (oldViewDistance * up) + up; // inclusive

				for (int currX = minX; currX != maxX; currX += right) {
					for (int currZ = minZ; currZ != maxZ; currZ += up) {
						this.addPlayerTo(player, currX, currZ);
					}
				}
			}

			if(dx != 0) {
				// handle left removal

				maxX = toX - (oldViewDistance * right); // exclusive
				minX = fromX - (oldViewDistance * right); // inclusive
				maxZ = fromZ + (oldViewDistance * up) + up; // exclusive
				minZ = toZ - (oldViewDistance * up); // inclusive

				for (int currX = minX; currX != maxX; currX += right) {
					for (int currZ = minZ; currZ != maxZ; currZ += up) {
						this.removePlayerFrom(player, currX, currZ);
					}
				}
			}

			if(dz != 0) {
				// handle down removal

				maxX = fromX + (oldViewDistance * right) + right; // exclusive
				minX = fromX - (oldViewDistance * right); // inclusive
				maxZ = toZ - (oldViewDistance * up); // exclusive
				minZ = fromZ - (oldViewDistance * up); // inclusive

				for (int currX = minX; currX != maxX; currX += right) {
					for (int currZ = minZ; currZ != maxZ; currZ += up) {
						this.removePlayerFrom(player, currX, currZ);
					}
				}
			}
		} else {
			// different view distance
			// for now :)
			this.removePlayer(player, oldPosition, oldViewDistance);
			this.addNewPlayer(player, newPosition, newViewDistance);
		}
	}

	private void removePlayer(final ServerPlayerEntity player, final SectionPos position, final int viewDistance) {
		final int x = position.getX();
		final int z = position.getZ();

		for (int xoff = -viewDistance; xoff <= viewDistance; ++xoff) {
			for (int zoff = -viewDistance; zoff <= viewDistance; ++zoff) {
				this.removePlayerFrom(player, x + xoff, z + zoff);
			}
		}
	}

	private void addNewPlayer(final ServerPlayerEntity player, final SectionPos position, final int viewDistance) {
		final int x = position.getX();
		final int z = position.getZ();

		for (int xoff = -viewDistance; xoff <= viewDistance; ++xoff) {
			for (int zoff = -viewDistance; zoff <= viewDistance; ++zoff) {
				this.addPlayerTo(player, x + xoff, z + zoff);
			}
		}
	}
}
