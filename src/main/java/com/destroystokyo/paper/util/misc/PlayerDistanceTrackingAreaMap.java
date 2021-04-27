package com.destroystokyo.paper.util.misc;

import net.minecraft.entity.player.PlayerEntity;

public class PlayerDistanceTrackingAreaMap extends DistanceTrackingAreaMap<PlayerEntity> {

    public PlayerDistanceTrackingAreaMap() {
        super();
    }

    public PlayerDistanceTrackingAreaMap(final PooledLinkedHashSets<PlayerEntity> pooledHashSets) {
        super(pooledHashSets);
    }

    public PlayerDistanceTrackingAreaMap(final PooledLinkedHashSets<PlayerEntity> pooledHashSets, final ChangeCallback<PlayerEntity> addCallback,
                                         final ChangeCallback<PlayerEntity> removeCallback, final DistanceChangeCallback<PlayerEntity> distanceChangeCallback) {
        super(pooledHashSets, addCallback, removeCallback, distanceChangeCallback);
    }

    @Override
    protected PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<PlayerEntity> getEmptySetFor(final PlayerEntity player) {
        return player.cachedSingleHashSet;
    }
}