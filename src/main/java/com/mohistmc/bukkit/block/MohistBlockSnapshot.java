package com.mohistmc.bukkit.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.BlockSnapshot;
import org.bukkit.craftbukkit.v1_20_R1.block.CraftBlock;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/25 19:54:50
 */
public class MohistBlockSnapshot extends CraftBlock {

    private final BlockState blockState;

    public MohistBlockSnapshot(BlockSnapshot blockSnapshot, boolean current) {
        super(blockSnapshot.getLevel(), blockSnapshot.getPos());
        this.blockState = current ? blockSnapshot.getCurrentBlock() : blockSnapshot.getReplacedBlock();
    }

    @Override
    public BlockState getNMS() {
        return blockState;
    }

    public static MohistBlockSnapshot fromBlockSnapshot(BlockSnapshot blockSnapshot, boolean current) {
        return new MohistBlockSnapshot(blockSnapshot, current);
    }
}
