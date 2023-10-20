package com.mohistmc.plugins.ban.bans;

import com.mohistmc.MohistConfig;
import com.mohistmc.api.EntityAPI;
import com.mohistmc.api.ItemAPI;
import com.mohistmc.plugins.ban.BanType;
import com.mohistmc.plugins.ban.BanUtils;
import com.mohistmc.tools.ListUtils;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntityType;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/27 13:35:20
 */
public class BanEntity {

    public static boolean check(Entity entity) {
        if (!MohistConfig.ban_entity_enable) return false;
        return EntityAPI.isBan(entity.getBukkitEntity());
    }

    public static void save(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals("§4Add bans entity")) {

            List<String> old = MohistConfig.ban_entity_types;
            for (org.bukkit.inventory.ItemStack itemStack : event.getInventory().getContents()) {
                if (itemStack != null) {
                    ItemStack nmsItem = ItemAPI.toNMSItem(itemStack);
                    if (nmsItem.getItem() instanceof SpawnEggItem spawnEggItem) {
                        EntityType<?> entitytype = spawnEggItem.getType(nmsItem.getTag());
                        ListUtils.isDuplicate(old, CraftEntityType.minecraftToBukkit(entitytype).name());
                    }
                }
            }
            BanUtils.saveToYaml(old, BanType.ENTITY);
        }
    }
}
