/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.extensions;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface IForgeBlockEntity extends ICapabilitySerializable<CompoundTag>
{
    private BlockEntity self() { return (BlockEntity) this; }

    @Override
    default void deserializeNBT(CompoundTag nbt)
    {
        self().load(nbt);
    }

    @Override
    default CompoundTag serializeNBT()
    {
        return self().saveWithFullMetadata();
    }

    /**
     * Called when you receive a TileEntityData packet for the location this
     * TileEntity is currently in. On the client, the NetworkManager will always
     * be the remote server. On the server, it will be whomever is responsible for
     * sending the packet.
     *
     * @param net The NetworkManager the packet originated from
     * @param pkt The data packet
     */
    default void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
    {
        CompoundTag compoundtag = pkt.getTag();
        if (compoundtag != null) {
            self().load(compoundtag);
        }
    }

    /**
     * Called when the chunk's TE update tag, gotten from {@link BlockEntity#getUpdateTag()}, is received on the client.
     * <p>
     * Used to handle this tag in a special way. By default this simply calls {@link BlockEntity#load(CompoundTag)}.
     *
     * @param tag The {@link CompoundTag} sent from {@link BlockEntity#getUpdateTag()}
     */
     default void handleUpdateTag(CompoundTag tag)
     {
         self().load(tag);
     }

    /**
     * Gets a {@link CompoundTag} that can be used to store custom data for this block entity.
     * It will be written, and read from disc, so it persists over world saves.
     *
     * @return A compound tag for custom persistent data
     */
     CompoundTag getPersistentData();

     default void onChunkUnloaded(){}

    /**
     * Called when this is first added to the world (by {@link LevelChunk#addAndRegisterBlockEntity(BlockEntity)})
     * or right before the first tick when the chunk is generated or loaded from disk.
     * Override instead of adding {@code if (firstTick)} stuff in update.
     */
     default void onLoad()
     {
     }

     /**
      * Sometimes default render bounding box: infinite in scope. Used to control rendering on {@link BlockEntityWithoutLevelRenderer}.
      */
     public static final AABB INFINITE_EXTENT_AABB = new net.minecraft.world.phys.AABB(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);


}
