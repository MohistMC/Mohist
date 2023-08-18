package com.mohistmc.api;

import com.mohistmc.bukkit.inventory.MohistModsInventory;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/11 2:39:19
 */
public class InventoryAPI {

    public static boolean isMods(Inventory bukkit) {
        return bukkit.getType().isMods();
    }

    public static String getModsInvName(Inventory bukkit) {
        if (((CraftInventory) bukkit).getInventory() instanceof MohistModsInventory mohistModsInventory) {
            return mohistModsInventory.getContainer().getClass().getSimpleName();
        }
        bukkit.getType().setMods(false);
        return ((CraftInventory) bukkit).getInventory().getClass().getSimpleName();
    }
}
