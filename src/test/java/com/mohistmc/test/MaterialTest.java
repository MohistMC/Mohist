package com.mohistmc.test;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.mohistmc.api.ItemAPI;

public class MaterialTest {

    public String debug () {
        // ic2
        int id = 4096;
        // name = X + ID
        // TNT
        ItemStack ic2 = new ItemStack(Material.getMaterial("X" + id), 1, (short)1);
        return ic2.getType().name();
    }

    public boolean debug0 (Player player) {
        return player.getInventory().getItemInMainHand().getType().isForgeBlock();
    }

    public boolean debug1 (Location location) {
        return location.getBlock().getType().isForgeBlock();
    }

    public boolean debug2 (Material material) {
        return ItemAPI.getModid(material.name()).equals("IC2");
    }

}
