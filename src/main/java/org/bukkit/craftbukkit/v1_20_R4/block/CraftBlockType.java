package org.bukkit.craftbukkit.v1_20_R4.block;

import net.minecraft.world.level.block.Block;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R4.util.CraftMagicNumbers;

public class CraftBlockType {

    public static Material minecraftToBukkit(Block block) {
        return CraftMagicNumbers.getMaterial(block);
    }

    public static Block bukkitToMinecraft(Material material) {
        return CraftMagicNumbers.getBlock(material);
    }
}
