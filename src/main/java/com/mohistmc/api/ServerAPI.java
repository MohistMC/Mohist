/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2022.
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

package com.mohistmc.api;

import io.netty.util.internal.ConcurrentSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class ServerAPI {

    public static Map<String, Integer> mods = new ConcurrentHashMap();
    public static Set<String> modlists = new ConcurrentSet();
    public static Map<String, String> forgecmdper = new ConcurrentHashMap();
    public static List<Command> forgecmd = new ArrayList<>();
    public static Map<net.minecraft.entity.EntityType<?>, String> entityTypeMap = new ConcurrentHashMap<>();

    // Don't count the default number of mods
    public static int getModSize() {
        return mods.get("mods") == null ? 0 : mods.get("mods") - 2;
    }

    public static String getModList() {
        return modlists.toString();
    }

    public static Boolean hasMod(String modid) {
        return getModList().contains(modid);
    }

    public static Boolean hasPlugin(String pluginname) {
        return Bukkit.getPluginManager().getPlugin(pluginname) != null;
    }

    public static void registerBukkitEvents(Listener listener, Plugin plugin){
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public static MinecraftServer getNMSServer(){
        return MinecraftServer.getServer();
    }
}
