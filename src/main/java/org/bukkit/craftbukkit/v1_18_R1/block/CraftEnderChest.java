package org.bukkit.craftbukkit.v1_18_R1.block;

import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.EnderChest;

public class CraftEnderChest extends CraftBlockEntityState<EnderChestBlockEntity> implements EnderChest {

    public CraftEnderChest(World world, final EnderChestBlockEntity te) {
        super(world, te);
    }
}
