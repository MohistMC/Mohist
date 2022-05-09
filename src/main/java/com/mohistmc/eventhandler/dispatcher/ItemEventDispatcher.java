/*
 * MohistMC
 * Copyright (C) 2019-2022.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.eventhandler.dispatcher;

import net.minecraft.entity.item.ItemEntity;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.event.CraftEventFactory;

public class ItemEventDispatcher {

    @SubscribeEvent(receiveCanceled = true)
    public void onItemExpireEvent(ItemExpireEvent event) {
        if (Bukkit.getServer() instanceof CraftServer) {
            // CraftBukkit start - fire ItemDespawnEvent
            ItemEntity entity = event.getEntityItem();
            if (CraftEventFactory.callItemDespawnEvent(entity).isCancelled()) {
                entity.age = 0;
                return;
            }
            // CraftBukkit end
        }
    }
}
