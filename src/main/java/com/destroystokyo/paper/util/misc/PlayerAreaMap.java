package com.destroystokyo.paper.util.misc;

import net.minecraft.entity.player.PlayerEntity;

/**
 * @author Spottedleaf
 */
public final class PlayerAreaMap extends AreaMap<PlayerEntity> {

    public PlayerAreaMap() {
        super();
    }

    public PlayerAreaMap(final PooledLinkedHashSets<PlayerEntity> pooledHashSets) {
        super(pooledHashSets);
    }

    public PlayerAreaMap(final PooledLinkedHashSets<PlayerEntity> pooledHashSets, final ChangeCallback<PlayerEntity> addCallback,
                         final ChangeCallback<PlayerEntity> removeCallback) {
        this(pooledHashSets, addCallback, removeCallback, null);
    }

    public PlayerAreaMap(final PooledLinkedHashSets<PlayerEntity> pooledHashSets, final ChangeCallback<PlayerEntity> addCallback,
                         final ChangeCallback<PlayerEntity> removeCallback, final ChangeSourceCallback<PlayerEntity> changeSourceCallback) {
        super(pooledHashSets, addCallback, removeCallback, changeSourceCallback);
    }

    @Override
    protected PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<PlayerEntity> getEmptySetFor(final PlayerEntity player) {
        return player.cachedSingleHashSet;
    }
}
