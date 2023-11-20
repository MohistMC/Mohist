package com.mohistmc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftNamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.enchantment.EnchantItemEvent;

/**
 * @author Mgazul by MohistMC
 * @date 2023/11/20 22:38:56
 */
public class EnchantmentMenuUtils {

    public static EnchantItemEvent itemEvent;

    public static boolean isCancelledEnchantItemEvent(Player p_39465_, int p_39466_, EnchantmentMenu menu, List<EnchantmentInstance> list, ItemStack itemstack2) {
        Map<Enchantment, Integer> enchants = new java.util.HashMap<org.bukkit.enchantments.Enchantment, Integer>();
        for (Object obj : list) {
            EnchantmentInstance instance = (EnchantmentInstance) obj;
            enchants.put(org.bukkit.enchantments.Enchantment.getByKey(CraftNamespacedKey.fromMinecraft(Registry.ENCHANTMENT.getKey(instance.enchantment))), instance.level);
        }
        CraftItemStack item = CraftItemStack.asCraftMirror(itemstack2);

        EnchantItemEvent event = new EnchantItemEvent((org.bukkit.entity.Player) p_39465_.getBukkitEntity(), menu.getBukkitView(), menu.access.getLocation().getBlock(), item, menu.costs[p_39466_], enchants, p_39466_);
        Bukkit.getPluginManager().callEvent(event);

        int level = event.getExpLevelCost();
        if (event.isCancelled() || (level > p_39465_.experienceLevel && !p_39465_.getAbilities().instabuild) || event.getEnchantsToAdd().isEmpty()) {
            return true;
        }
        itemEvent = event;
        return false;
    }

    public static void initCBList(List<EnchantmentInstance> list) {
        List<EnchantmentInstance> list0 = new ArrayList<>();
        for (Map.Entry<org.bukkit.enchantments.Enchantment, Integer> entry : itemEvent.getEnchantsToAdd().entrySet()) {
            NamespacedKey enchantId = entry.getKey().getKey();
            net.minecraft.world.item.enchantment.Enchantment nms = Registry.ENCHANTMENT.get(CraftNamespacedKey.toMinecraft(enchantId));
            if (nms == null) {
                continue;
            }

            EnchantmentInstance enchantmentinstance = new EnchantmentInstance(nms, entry.getValue());
            list.add(enchantmentinstance);
        }
        list = list0;
    }
}
