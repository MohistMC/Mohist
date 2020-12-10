package com.mohistmc.api;

import net.minecraft.block.BlockFarmland;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;

public class BlockAPI {

    /**
     * Determine whether {@link org.bukkit.Material} is Farmland
     *
     * @param material
     * @return
     */
    public static boolean isFarmland(Material material) {
        return material == Material.SOIL || (material.isForgeBlock() && CraftMagicNumbers.getBlock(material) instanceof BlockFarmland);
    }

    /**
     * Determine whether {@link org.bukkit.block.Block} is Farmland
     *
     * @param block
     * @return
     */
    public static boolean isFarmland(org.bukkit.block.Block block) {
        return block.getType() == Material.SOIL || (block.getType().isForgeBlock() && CraftMagicNumbers.getBlock(block) instanceof BlockFarmland);
    }
}
