package com.mohistmc.plugins.ban.bans;

import com.mohistmc.MohistConfig;
import com.mohistmc.api.EntityAPI;
import com.mohistmc.api.ItemAPI;
import com.mohistmc.api.ServerAPI;
import com.mohistmc.plugins.ban.BanType;
import com.mohistmc.plugins.ban.utils.BanUtils;
import com.mohistmc.tools.ListUtils;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
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
}
