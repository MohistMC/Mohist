package org.bukkit.craftbukkit.v1_20_R4.inventory;

import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R4.util.CraftMagicNumbers;

public class CraftItemType {

    public static Material minecraftToBukkit(Item item) {
        return CraftMagicNumbers.getMaterial(item);
    }

    public static Item bukkitToMinecraft(Material material) {
        return CraftMagicNumbers.getItem(material);
    }
}
