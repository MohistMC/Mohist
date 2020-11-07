package com.mohistmc.api;

import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class ItemAPI {

    public static net.minecraft.item.ItemStack toNMSItem(Material materialcb) {
        ItemStack itemStackcb = new ItemStack(materialcb);
        return CraftItemStack.asNMSCopy(itemStackcb);
    }

    public static ItemStack getBukkit(Material material){
        return CraftItemStack.asBukkitCopy(toNMSItem(material));
    }
}
