package com.mohistmc.plugins;

import com.mohistmc.MohistConfig;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/13 11:36:40
 */
public class EnchantmentFix {

    public static void anvilListener(PrepareAnvilEvent e) {
        if (!MohistConfig.enchantment_fix) return;
        ItemStack is = e.getResult();
        if (is == null) {
            return;
        }

        ItemStack itemStack1 = e.getInventory().getItem(0);
        ItemStack itemStack2 = e.getInventory().getItem(1);
        if (itemStack1 == null || itemStack2 == null) {
            return;
        }

        Map<Enchantment, Integer> enchantments1 = getEnchantments(itemStack1);
        Map<Enchantment, Integer> enchantments2 = getEnchantments(itemStack2);
        Map<Enchantment, Integer> merged = new HashMap<>();
        for (Map<Enchantment, Integer> m : Arrays.asList(enchantments1, enchantments2)) {
            for (Map.Entry<Enchantment, Integer> enchantmentIntegerEntry : m.entrySet()) {
                merged.merge(enchantmentIntegerEntry.getKey(), enchantmentIntegerEntry.getValue(), EnchantmentFix::getUplevel);
            }
        }

        for (var ench : getEnchantments(is).entrySet()) {
            if (merged.containsKey(ench.getKey())) {
                setEnchantment(is, ench.getKey(), merged.get(ench.getKey()));
            }
        }
    }

    private static void setEnchantment(ItemStack itemStack, Enchantment enchantment, int value) {
        if (enchantment.getMaxLevel() == 1) return;
        if (itemStack.getItemMeta() instanceof EnchantmentStorageMeta enchantmentStorageMeta) {
            enchantmentStorageMeta.removeStoredEnchant(enchantment);
            enchantmentStorageMeta.addStoredEnchant(enchantment, value, true);
            itemStack.setItemMeta(enchantmentStorageMeta);
            return;
        }
        itemStack.removeEnchantment(enchantment);
        itemStack.addUnsafeEnchantment(enchantment, value);
    }

    private static Map<Enchantment, Integer> getEnchantments(ItemStack itemStack) {
        if (itemStack.getItemMeta() instanceof EnchantmentStorageMeta enchantmentStorageMeta) {
            return enchantmentStorageMeta.getStoredEnchants();
        }
        return itemStack.getEnchantments();
    }

    private static int getUplevel(int a, int b) {
        return Math.min(MohistConfig.max_enchantment_level, getUplevel0(a, b));
    }

    private static int getUplevel0(int a, int b) {
        if (a == b) {
            return a + 1;
        }
        return Math.max(a, b);
    }
}
