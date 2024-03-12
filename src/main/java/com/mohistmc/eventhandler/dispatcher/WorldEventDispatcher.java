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

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;

public class WorldEventDispatcher {

    //For WorldLoadEvent
    @SubscribeEvent(receiveCanceled = true)
    public void onWorldLoadEvent(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel handle) {
            CraftWorld craftWorld = handle.getWorld();
            Bukkit.getPluginManager().callEvent(new WorldLoadEvent(craftWorld));
            Bukkit.getPluginManager().callEvent(new WorldInitEvent(craftWorld));
        }
    }

    //For WorldSaveEvent
    @SubscribeEvent(receiveCanceled = true)
    public void onWorldSaveEvent(LevelEvent.Save event) {
        if (event.getLevel() instanceof ServerLevel handle) {
            WorldSaveEvent save = new WorldSaveEvent(handle.getWorld());
            Bukkit.getPluginManager().callEvent(save);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            ((CraftServer) Bukkit.getServer()).removeWorld(serverLevel);
        }
    }
}
