package org.bukkit.craftbukkit.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlockState;

public class BlockStateListPopulator extends DummyGeneratorAccess {
    private final Level world;
    private final LinkedHashMap<net.minecraft.core.BlockPos, CraftBlockState> list;

    public BlockStateListPopulator(Level world) {
        this(world, new LinkedHashMap<>());
    }

    public BlockStateListPopulator(Level world, LinkedHashMap<net.minecraft.core.BlockPos, CraftBlockState> list) {
        this.world = world;
        this.list = list;
    }

    @Override
    public net.minecraft.world.level.block.state.BlockState getType(net.minecraft.core.BlockPos bp) {
        CraftBlockState state = list.get(bp);
        return (state != null) ? state.getHandle() : world.getBlockState(bp);
    }

    @Override
    public FluidState getFluid(net.minecraft.core.BlockPos bp) {
        CraftBlockState state = list.get(bp);
        return (state != null) ? state.getHandle().getFluidState() : world.getFluidState(bp);
    }

    @Override
    public boolean setBlock(net.minecraft.core.BlockPos position, net.minecraft.world.level.block.state.BlockState data, int flag) {
        CraftBlockState state = CraftBlockState.getBlockState(world, position, flag);
        state.setData(data);
        // remove first to keep insertion order
        list.remove(position);
        list.put(position.immutable(), state);
        return true;
    }

    public void updateList() {
        for (BlockState state : list.values()) {
            state.update(true);
        }
    }

    public Set<net.minecraft.core.BlockPos> getBlocks() {
        return list.keySet();
    }

    public List<CraftBlockState> getList() {
        return new ArrayList<>(list.values());
    }

    public Level getWorld() {
        return world;
    }
}
