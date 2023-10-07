package com.mohistmc.plugins.ban.bans;

import com.mohistmc.MohistConfig;
import com.mohistmc.api.ItemAPI;
import com.mohistmc.plugins.ban.BanType;
import com.mohistmc.plugins.ban.BanUtils;
import com.mohistmc.tools.ListUtils;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/27 2:54:23
 */
public class BanItem {

    public static boolean check(UseOnContext use) {
        return check(use.getPlayer(), use.getItemInHand());
    }

    public static boolean check(net.minecraft.world.entity.player.Player player, ItemStack itemStack) {
        if (player == null) return false;
        if (BanEnchantment.check(itemStack)) {
            player.containerMenu.sendAllDataToRemote();
            return true;
        }
        if (check(itemStack)) {
            player.containerMenu.sendAllDataToRemote();
            return true;
        }
        return false;
    }

    public static boolean check(net.minecraft.world.entity.player.Player player) {
        return check(player.getMainHandItem()) || check(player.getOffhandItem());
    }

    public static boolean check(ItemStack itemStack) {
        if (!MohistConfig.ban_item_enable) return false;
        return ItemAPI.isBan(CraftItemStack.asCraftMirror(itemStack));
    }

    public static void save(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals("§4Add bans item")) {

            List<String> old = MohistConfig.ban_item_materials;
            for (org.bukkit.inventory.ItemStack itemStack : event.getInventory().getContents()) {
                if (itemStack != null) {
                    ListUtils.isDuplicate(old, itemStack.getType().name());
                }
            }
            BanUtils.saveToYaml(old, BanType.ITEM);
        }
    }
}
