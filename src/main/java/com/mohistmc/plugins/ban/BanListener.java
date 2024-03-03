package com.mohistmc.plugins.ban;

import com.mohistmc.MohistConfig;
import com.mohistmc.api.EnchantmentAPI;
import com.mohistmc.api.ItemAPI;
import com.mohistmc.api.ServerAPI;
import com.mohistmc.plugins.ban.utils.BanSaveInventory;
import com.mohistmc.plugins.ban.utils.BanUtils;
import com.mohistmc.tools.ListUtils;
import java.util.List;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class BanListener {

    public static BanSaveInventory openInventory;

    public static void save(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (openInventory != null && openInventory.getInventory() == inventory) {
            if (openInventory.getBanType() == BanType.ITEM) {
                List<String> old = MohistConfig.ban_item_materials;
                for (org.bukkit.inventory.ItemStack itemStack : event.getInventory().getContents()) {
                    if (itemStack != null && itemStack.getType() != Material.AIR) {
                        ListUtils.isDuplicate(old, itemStack.getType().name());
                    }
                }
                BanUtils.saveToYaml(old, BanType.ITEM);
            } else if (openInventory.getBanType() == BanType.ENTITY) {
                List<String> old = MohistConfig.ban_entity_types;
                for (org.bukkit.inventory.ItemStack itemStack : event.getInventory().getContents()) {
                    if (itemStack != null && itemStack.getType() != Material.AIR) {
                        ItemStack nmsItem = ItemAPI.toNMSItem(itemStack);
                        if (nmsItem.getItem() instanceof SpawnEggItem spawnEggItem) {
                            EntityType<?> entitytype = spawnEggItem.getType(nmsItem.getTag());
                            ListUtils.isDuplicate(old, ServerAPI.entityTypeMap.get(entitytype));
                        }
                    }
                }
                BanUtils.saveToYaml(old, BanType.ENTITY);
            } else if (openInventory.getBanType() == BanType.ENCHANTMENT) {
                List<String> old = MohistConfig.ban_enchantment_list;
                for (org.bukkit.inventory.ItemStack itemStack : event.getInventory().getContents()) {
                    if (itemStack != null && itemStack.getType() != Material.AIR) {
                        if (EnchantmentAPI.has(itemStack)) {
                            for (Enchantment e : EnchantmentAPI.get(itemStack)) {
                                ListUtils.isDuplicate(old, e.getKey().toString());
                            }
                        }
                    }
                }
                BanUtils.saveToYaml(old, BanType.ENCHANTMENT);
            }
        }
    }
}
