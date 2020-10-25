package com.mohistmc.test;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.mohistmc.api.ItemAPI;

public class MaterialTest {

    public String debug () {
        // ic2
        int id = 4096;
        // name_id = X + ID
        String name_id = "X4096";
        String name = "BLOCK_IC2_TE";
        // TNT
        ItemStack ic2_id = new ItemStack(Material.getMaterial(name_id), 1, (short)1);
        ItemStack ic2_name = new ItemStack(Material.getMaterial(name), 1, (short)1);

        return ic2_id.getType().name() + " - " + ic2_name.getType().name();
    }

    public boolean debug0 (Player player) {
        return player.getInventory().getItemInMainHand().getType().isForgeBlock();
    }

    public boolean debug1 (Location location) {
        return location.getBlock().getType().isForgeBlock();
    }

    public boolean debug1 (Block block) {
        return block.getType().isForgeBlock();
    }

    public boolean debug2 (Material material) {
        return ItemAPI.getModid(material.name()).equals("IC2");
    }

}
