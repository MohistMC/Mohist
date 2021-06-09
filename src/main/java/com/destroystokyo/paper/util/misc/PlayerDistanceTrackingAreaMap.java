package com.destroystokyo.paper.util.misc;

import net.minecraft.entity.player.ServerPlayerEntity;

public class PlayerDistanceTrackingAreaMap extends DistanceTrackingAreaMap<ServerPlayerEntity> {

    public PlayerDistanceTrackingAreaMap() {
        super();
    }

    public PlayerDistanceTrackingAreaMap(final PooledLinkedHashSets<ServerPlayerEntity> pooledHashSets) {
        super(pooledHashSets);
    }

    public PlayerDistanceTrackingAreaMap(final PooledLinkedHashSets<ServerPlayerEntity> pooledHashSets, final ChangeCallback<ServerPlayerEntity> addCallback,
                                         final ChangeCallback<ServerPlayerEntity> removeCallback, final DistanceChangeCallback<ServerPlayerEntity> distanceChangeCallback) {
        super(pooledHashSets, addCallback, removeCallback, distanceChangeCallback);
    }

    @Override
    protected PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<ServerPlayerEntity> getEmptySetFor(final ServerPlayerEntity player) {
        return player.cachedSingleHashSet;
    }
}
