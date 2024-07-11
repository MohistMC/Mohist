package com.mohistmc.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.forgespi.language.IModInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class ServerAPI {

    public static HashSet<String> modlists_Client = new HashSet<>();
    public static HashSet<String> modlists_Server = new HashSet<>();
    public static Set<String> modlists_Inside = Set.of("minecraft", "forge", "mohist");
    public static List<String> modlists_All = new ArrayList<>();
    public static Map<String, String> forgecmdper = new ConcurrentHashMap<>();
    public static List<Command> forgecmd = new ArrayList<>();
    public static Map<net.minecraft.world.entity.EntityType<?>, String> entityTypeMap = new ConcurrentHashMap<>();
    public static Map< EntityType, net.minecraft.world.entity.EntityType<?>> entityTypeMap0 = new ConcurrentHashMap<>();
    public static Map<Integer, EnderDragon.Phase> phasetypeMap = new ConcurrentHashMap<>();

    public static boolean yes_steve_model() {
        return modlists_All.contains("yes_steve_model");
    };

    static {
        for (IModInfo modInfo : ModLoader.getModList().getMods()) {
            modlists_All.add(modInfo.getModId());
            for (IModInfo.ModVersion modVersion : modInfo.getDependencies()) {
                if (modVersion.getSide().name().equals("CLIENT")) {
                    modlists_Client.add(modInfo.getModId());
                } else if (modVersion.getSide().name().equals("DEDICATED_SERVER")) {
                    modlists_Server.add(modInfo.getModId());
                }
            }
        }
    }

    public static Set<String> channels_Incoming() {
        return Bukkit.getMessenger().getIncomingChannels();
    }

    public static Set<String> channels_Outgoing() {
        return Bukkit.getMessenger().getOutgoingChannels();
    }

    // Don't count the default number of mods
    public static int getModSize() {
        return ModLoader.getModList().size();
    }

    public static Boolean hasMod(String modid) {
        return modlists_All.contains(modid);
    }

    public static Boolean hasPlugin(String pluginname) {
        return Bukkit.getPluginManager().getPlugin(pluginname) != null;
    }

    public static void putBukkitEvents(Listener listener, Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public static MinecraftServer getNMSServer() {
        return MinecraftServer.getServer();
    }
}
