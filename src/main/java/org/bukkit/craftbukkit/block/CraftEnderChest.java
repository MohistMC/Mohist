package org.bukkit.craftbukkit.block;

import net.minecraft.block.entity.EnderChestBlockEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.EnderChest;

public class CraftEnderChest extends CraftBlockEntityState<EnderChestBlockEntity> implements EnderChest {

    public CraftEnderChest(final Block block) {
        super(block, EnderChestBlockEntity.class);
    }

    public CraftEnderChest(final Material material, final EnderChestBlockEntity te) {
        super(material, te);
    }
}
