package com.mohistmc.block;

import net.minecraft.block.BlockState;
import net.minecraftforge.common.util.BlockSnapshot;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlock;

public class CraftCustomSnapshot extends CraftBlock {

    private final BlockState blockState;

    public CraftCustomSnapshot(BlockSnapshot blockSnapshot, boolean current) {
        super(blockSnapshot.getWorld(), blockSnapshot.getPos());
        this.blockState = current ? blockSnapshot.getCurrentBlock() : blockSnapshot.getReplacedBlock();
    }

    @Override
    public BlockState getNMS() {
        return blockState;
    }

    public static CraftCustomSnapshot fromBlockSnapshot(BlockSnapshot blockSnapshot, boolean current) {
        return new CraftCustomSnapshot(blockSnapshot, current);
    }
}
