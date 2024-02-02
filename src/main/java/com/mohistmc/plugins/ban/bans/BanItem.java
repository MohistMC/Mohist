package com.mohistmc.plugins.ban.bans;

import com.mohistmc.MohistConfig;
import com.mohistmc.api.ItemAPI;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;

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
}
