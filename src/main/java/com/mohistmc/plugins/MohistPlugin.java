package com.mohistmc.plugins;

import com.mohistmc.MohistConfig;
import com.mohistmc.plugins.back.BackCommands;
import com.mohistmc.plugins.back.BackConfig;
import com.mohistmc.plugins.ban.BanListener;
import com.mohistmc.plugins.item.ItemsConfig;
import com.mohistmc.plugins.pluginmanager.Control;
import com.mohistmc.plugins.tpa.TpaComamands;
import com.mohistmc.plugins.tpa.TpacceptCommands;
import com.mohistmc.plugins.tpa.TpadenyCommands;
import com.mohistmc.plugins.warps.WarpsCommands;
import com.mohistmc.plugins.warps.WarpsConfig;
import com.mohistmc.plugins.world.WorldManage;
import com.mohistmc.plugins.world.commands.WorldsCommands;
import com.mohistmc.plugins.world.listener.InventoryClickListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

/**
 * @author Mgazul by MohistMC
 * @date 2023/6/14 14:46:34
 */
public class MohistPlugin {

    public static Plugin plugin;

    public static Logger LOGGER = LogManager.getLogger("MohistPlugin");

    public static void init(Server server) {
        if (MohistConfig.yml.getBoolean("worldmanage", true)) WorldManage.onEnable();
        ItemsConfig.init();
        BackConfig.init();
        WarpsConfig.init();
        File out = new File("libraries/com/mohistmc/cache", "libPath.txt");
        if (out.exists()) {
            String data = null;
            try {
                data = Files.readString(out.toPath());
            } catch (IOException e) {
                data = "libraries";
            }
            File file = new File(data, "com/mohistmc/mohistplugins/mohistplugins-1.20.1.jar");
            if (file.exists()) {
                plugin = Control.loadPlugin(file);
                if (plugin != null) {
                    server.getPluginManager().enablePlugin(plugin);
                } else {
                    LOGGER.error("Failed to load mohistplugins.jar");
                }
            }
        }
        EntityClear.start();
    }

    public static void registerCommands(Map<String, Command> map) {
        if (MohistConfig.yml.getBoolean("worldmanage", true)) {
            map.put("worlds", new WorldsCommands("worlds"));
        }
        map.put("warps", new WarpsCommands("warps"));
        if (MohistConfig.yml.getBoolean("tpa.enable", false)) {
            map.put("tpa", new TpaComamands("tpa"));
            map.put("tpadeny", new TpadenyCommands("tpadeny"));
            map.put("tpaccept", new TpacceptCommands("tpaccept"));
        }
        if (MohistConfig.yml.getBoolean("back.enable", false)) {
            map.put("back", new BackCommands("back"));
        }
    }

    public static void registerListener(Event event) {
        if (event instanceof InventoryClickEvent inventoryClickEvent) {
            InventoryClickListener.init(inventoryClickEvent);
        }
        if (event instanceof PrepareAnvilEvent prepareAnvilEvent) {
            EnchantmentFix.anvilListener(prepareAnvilEvent);
        }
        if (event instanceof InventoryCloseEvent event1) {
            BanListener.save(event1);
        }
        if (event instanceof PluginEnableEvent event1) {
            PluginHooks.register(event1);
        }
        if (event instanceof PlayerTeleportEvent event1) {
            BackCommands.hookTeleport(event1);
        }
        if (event instanceof PlayerDeathEvent event1) {
            BackCommands.hooktDeath(event1);
        }
    }

}
