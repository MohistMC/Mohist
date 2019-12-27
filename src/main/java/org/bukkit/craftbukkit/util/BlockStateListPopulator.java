package org.bukkit.craftbukkit.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import net.minecraft.util.math.BlockPos;
import net.minecraft.fluid.IFluidState;
import net.minecraft.block.BlockState;
import net.minecraft.world.World;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlockState;

public class StateContainerPopulator extends DummyIWorld {
    private final World world;
    private final LinkedHashMap<BlockPos, CraftBlockState> list;

    public StateContainerPopulator(World world) {
        this(world, new LinkedHashMap<>());
    }

    public StateContainerPopulator(World world, LinkedHashMap<BlockPos, CraftBlockState> list) {
        this.world = world;
        this.list = list;
    }

    @Override
    public BlockState getType(BlockPos bp) {
        CraftBlockState state = list.get(bp);
        return (state != null) ? state.getHandle() : world.getType(bp);
    }

    @Override
    public Fluid getFluid(BlockPos bp) {
        CraftBlockState state = list.get(bp);
        return (state != null) ? state.getHandle().getFluid() : world.getFluid(bp);
    }

    @Override
    public boolean setTypeAndData(BlockPos position, BlockState data, int flag) {
        CraftBlockState state = CraftBlockState.getBlockState(world, position, flag);
        state.setData(data);
        list.put(position, state);
        return true;
    }

    public void updateList() {
        for (BlockState state : list.values()) {
            state.update(true);
        }
    }

    public Set<BlockPos> getBlocks() {
        return list.keySet();
    }

    public List<CraftBlockState> getList() {
        return new ArrayList<>(list.values());
    }

    public World getWorld() {
        return world;
    }
}
