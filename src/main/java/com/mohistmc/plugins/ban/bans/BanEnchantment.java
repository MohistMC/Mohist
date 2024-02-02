package com.mohistmc.plugins.ban.bans;

import com.mohistmc.MohistConfig;
import com.mohistmc.api.EnchantmentAPI;
import org.bukkit.craftbukkit.v1_20_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/27 15:18:21
 */
public class BanEnchantment {

    public static boolean check(net.minecraft.world.item.enchantment.Enchantment enchantment) {
        return check(new CraftEnchantment(enchantment));
    }

    public static boolean check(Enchantment enchantment) {
        if (!MohistConfig.ban_enchantment_enable) return false;
        return MohistConfig.ban_enchantment_list.contains(enchantment.getName());
    }

    public static boolean check(ItemStack itemStack) {
        if (!MohistConfig.ban_enchantment_enable) return false;
        if (EnchantmentAPI.has(itemStack)) {
            for (Enchantment enchantment : EnchantmentAPI.get(itemStack)) {
                return MohistConfig.ban_enchantment_list.contains(enchantment.getName());
            }
        }
        return false;
    }

    public static boolean check(net.minecraft.world.item.ItemStack itemStack) {
        if (!MohistConfig.ban_enchantment_enable) return false;
        if (EnchantmentAPI.has(itemStack)) {
            for (Enchantment enchantment : EnchantmentAPI.get(CraftItemStack.asBukkitCopy(itemStack))) {
                return MohistConfig.ban_enchantment_list.contains(enchantment.getName());
            }
        }
        return false;
    }
}
