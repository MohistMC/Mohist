/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2024.
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

import com.mohistmc.bukkit.inventory.MohistModsInventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventoryView;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class PlayerEventDispatcher {

    //For PlayerAdvancementDoneEvent
    @SubscribeEvent
    public void onAdvancementDone(AdvancementEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Bukkit.getPluginManager().callEvent(new PlayerAdvancementDoneEvent(player.getBukkitEntity(), event.getAdvancement().bukkit));
        }
    }

    @SubscribeEvent
    public void onContainerClose(PlayerContainerEvent.Close event) {
        // Mohist start - Custom Container compatible with mods
        AbstractContainerMenu abstractcontainermenu = event.getContainer();
        if (abstractcontainermenu.getBukkitView() == null) {
            org.bukkit.inventory.Inventory inventory = new CraftInventory(new MohistModsInventory(abstractcontainermenu, event.getEntity()));
            inventory.getType().setMods(true);
            abstractcontainermenu.bukkitView = new CraftInventoryView(event.getEntity().getBukkitEntity(), inventory, abstractcontainermenu);
        }
        // Mohist end
        CraftEventFactory.handleInventoryCloseEvent(event.getEntity()); // CraftBukkit
    }

    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock  event) {

    }
}
