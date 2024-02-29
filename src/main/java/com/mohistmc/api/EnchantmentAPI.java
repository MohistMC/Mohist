package com.mohistmc.api;

import com.mohistmc.forge.ForgeInjectBukkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.bukkit.craftbukkit.v1_20_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/27 15:30:06
 */
public class EnchantmentAPI {

    public static boolean has(org.bukkit.inventory.ItemStack itemStack) {
        return itemStack.hasItemMeta() && itemStack.getItemMeta().hasEnchants();
    }

    public static boolean has(net.minecraft.world.item.ItemStack itemStack) {
        return has(CraftItemStack.asBukkitCopy(itemStack));
    }

    public static List<Enchantment> getNMS(org.bukkit.inventory.ItemStack itemStack) {
        if (has(itemStack)) {
            Map<org.bukkit.enchantments.Enchantment, Integer> map = itemStack.getEnchantments();
            return map.keySet().stream().map(CraftEnchantment::getRaw).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public static List<org.bukkit.enchantments.Enchantment> get(org.bukkit.inventory.ItemStack itemStack) {
        if (has(itemStack)) {
            return new ArrayList<>(itemStack.getEnchantments().keySet());
        }
        return Collections.emptyList();
    }

    public static String getNameByNMS(net.minecraft.world.item.enchantment.Enchantment nms) {
        return ForgeInjectBukkit.normalizeName(EnchantmentHelper.getEnchantmentId(nms).toString());
    }
}
