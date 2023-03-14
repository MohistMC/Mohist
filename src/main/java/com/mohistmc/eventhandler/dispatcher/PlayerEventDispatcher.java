/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2023.
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

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.player.*;

import java.util.Objects;

public class PlayerEventDispatcher {

    //For PlayerAdvancementDoneEvent
    @SubscribeEvent
    public void onAdvancementDone(AdvancementEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Bukkit.getPluginManager().callEvent(new PlayerAdvancementDoneEvent(player.getBukkitEntity(), event.getAdvancement().bukkit));
        }
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        ResourceKey<Level> fromLevel = event.getFrom();
        if (player instanceof final ServerPlayer serverPlayer) {
            PlayerChangedWorldEvent bukkitEvent =
                    new PlayerChangedWorldEvent(serverPlayer.getBukkitEntity(),
                            Objects.requireNonNull(MinecraftServer.getServer().getLevel(fromLevel)).getWorld());
            Bukkit.getServer().getPluginManager().callEvent(bukkitEvent);
        }
    }
}
