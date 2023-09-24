package org.bukkit.craftbukkit.v1_20_R2.block.impl;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/24 11:14:05
 */
public final class CraftBarrier extends org.bukkit.craftbukkit.v1_20_R2.block.data.CraftBlockData implements org.bukkit.block.data.Waterlogged {

    public CraftBarrier() {
        super();
    }

    public CraftBarrier(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftWaterlogged

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = getBoolean(net.minecraft.world.level.block.BarrierBlock.class, "waterlogged");

    @Override
    public boolean isWaterlogged() {
        return get(WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        set(WATERLOGGED, waterlogged);
    }
}
