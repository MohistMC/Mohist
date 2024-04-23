package com.mohistmc.mohist.plugins.ban.bans;

import com.mohistmc.mohist.MohistConfig;
import com.mohistmc.mohist.api.EntityAPI;
import net.minecraft.world.entity.Entity;

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
