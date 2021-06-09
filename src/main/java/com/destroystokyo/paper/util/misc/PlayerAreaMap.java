package com.destroystokyo.paper.util.misc;

import net.minecraft.entity.player.ServerPlayerEntity;

/**
 * @author Spottedleaf
 */
public final class PlayerAreaMap extends AreaMap<ServerPlayerEntity> {

    public PlayerAreaMap() {
        super();
    }

    public PlayerAreaMap(final PooledLinkedHashSets<ServerPlayerEntity> pooledHashSets) {
        super(pooledHashSets);
    }

    public PlayerAreaMap(final PooledLinkedHashSets<ServerPlayerEntity> pooledHashSets, final ChangeCallback<ServerPlayerEntity> addCallback,
                         final ChangeCallback<ServerPlayerEntity> removeCallback) {
        this(pooledHashSets, addCallback, removeCallback, null);
    }

    public PlayerAreaMap(final PooledLinkedHashSets<ServerPlayerEntity> pooledHashSets, final ChangeCallback<ServerPlayerEntity> addCallback,
                         final ChangeCallback<ServerPlayerEntity> removeCallback, final ChangeSourceCallback<ServerPlayerEntity> changeSourceCallback) {
        super(pooledHashSets, addCallback, removeCallback, changeSourceCallback);
    }

    @Override
    protected PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<ServerPlayerEntity> getEmptySetFor(final ServerPlayerEntity player) {
        return player.cachedSingleHashSet;
    }
}
