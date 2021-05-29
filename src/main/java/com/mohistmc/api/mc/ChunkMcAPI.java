package com.mohistmc.api.mc;

import com.mojang.datafixers.util.Either;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkHolder.IChunkLoadingError;
import net.minecraft.world.server.ChunkHolder.LocationType;
import net.minecraft.world.server.ServerWorld;

/**
 * Mohist API for Minecraft chunks.
 * @author KR33PY
 */
public abstract class ChunkMcAPI {

    /**
     * Returns true if the chunk on provided coordinates is loaded and has LocationType of BORDER or higher.
     */
    public static boolean isBorderChunkLoaded(ServerWorld world, int cX, int cZ) {
        return getBorderChunkNow(world, cX, cZ).isPresent();
    }

    /**
     * See {@link #isBorderChunkLoaded(ServerWorld, int, int)}
     */
    public static boolean isBorderChunkLoaded(ServerWorld world, long cPos) {
        return getBorderChunkNow(world, cPos).isPresent();
    }

    /**
     * See {@link #isBorderChunkLoaded(ServerWorld, int, int)}
     */
    public static boolean isBorderChunkLoaded(ChunkHolder holder) {
        return getBorderChunkNow(holder).isPresent();
    }

    /**
     * Returns true if the chunk on provided coordinates is loaded and has LocationType of TICKING or higher.
     */
    public static boolean isTickingChunkLoaded(ServerWorld world, int cX, int cZ) {
        return getTickingChunkNow(world, cX, cZ).isPresent();
    }

    /**
     * See {@link #isTickingChunkLoaded(ServerWorld, int, int)}
     */
    public static boolean isTickingChunkLoaded(ServerWorld world, long cPos) {
        return getTickingChunkNow(world, cPos).isPresent();
    }

    /**
     * See {@link #isTickingChunkLoaded(ServerWorld, int, int)}
     */
    public static boolean isTickingChunkLoaded(ChunkHolder holder) {
        return getTickingChunkNow(holder).isPresent();
    }

    /**
     * Returns true if the chunk on provided coordinates is loaded and has LocationType of ENTITY_TICKING.
     */
    public static boolean isEntityTickingChunkLoaded(ServerWorld world, int cX, int cZ) {
        return getEntityTickingChunkNow(world, cX, cZ).isPresent();
    }

    /**
     * See {@link #isEntityTickingChunkLoaded(ServerWorld, int, int)}
     */
    public static boolean isEntityTickingChunkLoaded(ServerWorld world, long cPos) {
        return getEntityTickingChunkNow(world, cPos).isPresent();
    }

    /**
     * See {@link #isEntityTickingChunkLoaded(ServerWorld, int, int)}
     */
    public static boolean isEntityTickingChunkLoaded(ChunkHolder holder) {
        return getEntityTickingChunkNow(holder).isPresent();
    }

    /**
     * Returns non-empty optional if the chunk on provided coordinates is loaded and has LocationType of BORDER or higher.
     */
    public static Optional<Chunk> getBorderChunkNow(ServerWorld world, int cX, int cZ) {
        return _getChunkNow(getChunkHolder(world, cX, cZ).orElse(null), LocationType.BORDER);
    }

    /**
     * See {@link #getBorderChunkNow(ServerWorld, int, int)}
     */
    public static Optional<Chunk> getBorderChunkNow(ServerWorld world, long cPos) {
        return _getChunkNow(getChunkHolder(world, cPos).orElse(null), LocationType.BORDER);
    }

    /**
     * See {@link #getBorderChunkNow(ServerWorld, int, int)}
     */
    public static Optional<Chunk> getBorderChunkNow(ChunkHolder holder) {
        return _getChunkNow(holder, LocationType.BORDER);
    }

    /**
     * Returns non-empty optional if the chunk on provided coordinates is loaded and has LocationType of TICKING or higher.
     */
    public static Optional<Chunk> getTickingChunkNow(ServerWorld world, int cX, int cZ) {
        return _getChunkNow(getChunkHolder(world, cX, cZ).orElse(null), LocationType.TICKING);
    }

    /**
     * See {@link #getTickingChunkNow(ServerWorld, int, int)}
     */
    public static Optional<Chunk> getTickingChunkNow(ServerWorld world, long cPos) {
        return _getChunkNow(getChunkHolder(world, cPos).orElse(null), LocationType.TICKING);
    }

    /**
     * See {@link #getTickingChunkNow(ServerWorld, int, int)}
     */
    public static Optional<Chunk> getTickingChunkNow(ChunkHolder holder) {
        return _getChunkNow(holder, LocationType.TICKING);
    }

    /**
     * Returns non-empty optional if the chunk on provided coordinates is loaded and has LocationType of ENTITY_TICKING.
     */
    public static Optional<Chunk> getEntityTickingChunkNow(ServerWorld world, int cX, int cZ) {
        return _getChunkNow(getChunkHolder(world, cX, cZ).orElse(null), LocationType.ENTITY_TICKING);
    }

    /**
     * See {@link #getEntityTickingChunkNow(ServerWorld, int, int)}
     */
    public static Optional<Chunk> getEntityTickingChunkNow(ServerWorld world, long cPos) {
        return _getChunkNow(getChunkHolder(world, cPos).orElse(null), LocationType.ENTITY_TICKING);
    }

    /**
     * See {@link #getEntityTickingChunkNow(ServerWorld, int, int)}
     */
    public static Optional<Chunk> getEntityTickingChunkNow(ChunkHolder holder) {
        return _getChunkNow(holder, LocationType.ENTITY_TICKING);
    }

    /**
     * Returns non-empty optional if the chunk holder with provided coordinates is found.
     */
    public static Optional<ChunkHolder> getChunkHolder(ServerWorld world, int cX, int cZ) {
        return _getChunkHolder(world, ChunkPos.asLong(cX, cZ));
    }

    /**
     * See {@link #getChunkHolder(ServerWorld, int, int)}
     */
    public static Optional<ChunkHolder> getChunkHolder(ServerWorld world, long cPos) {
        return _getChunkHolder(world, cPos);
    }

    private static Optional<Chunk> _getChunkNow(ChunkHolder holder, LocationType type) {
        if (holder != null) {
            CompletableFuture<Either<Chunk, IChunkLoadingError>> future;
            switch (type) {
                case BORDER: future = holder.getFullChunkFuture(); break;
                case TICKING: future = holder.getTickingChunkFuture(); break;
                case ENTITY_TICKING: future = holder.getEntityTickingChunkFuture(); break;
                default: future = null; break;
            }
            if (future != null) {
                return future.getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK).left();
            }
        }
        return Optional.empty();
    }

    private static Optional<ChunkHolder> _getChunkHolder(ServerWorld world, long cPos) {
        if (world == null)
            throw new IllegalArgumentException("`world` must not be null in getChunkHolder(...) call!");
        return Optional.ofNullable(world.getChunkSource().chunkMap.getVisibleChunkIfPresent(cPos));
    }

}