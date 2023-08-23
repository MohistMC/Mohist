package com.mohistmc.plugins;

import com.mohistmc.plugins.ban.bans.BanEnchantment;
import com.mohistmc.plugins.ban.bans.BanEntity;
import com.mohistmc.plugins.ban.bans.BanItem;
import com.mohistmc.plugins.item.ItemsConfig;
import com.mohistmc.plugins.pluginmanager.Control;
import com.mohistmc.plugins.world.WorldManage;
import com.mohistmc.plugins.world.commands.WorldsCommands;
import com.mohistmc.plugins.world.listener.InventoryClickListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

/**
 * @author Mgazul by MohistMC
 * @date 2023/6/14 14:46:34
 */
public class MohistPlugin {

    public static Plugin plugin;

    public static Logger LOGGER = LogManager.getLogger("MohistPlugin");

    public static void init(Server server) {
        WorldManage.onEnable();
        ItemsConfig.init();
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
        map.put(WorldManage.command, new WorldsCommands(WorldManage.command));
    }

    public static void registerListener(Event event) {
        if (event instanceof InventoryClickEvent inventoryClickEvent) {
            InventoryClickListener.init(inventoryClickEvent);
        }
        if (event instanceof PrepareAnvilEvent prepareAnvilEvent) {
            EnchantmentFix.anvilListener(prepareAnvilEvent);
        }
        if (event instanceof InventoryCloseEvent event1) {
            BanItem.save(event1);
            BanEntity.save(event1);
            BanEnchantment.save(event1);
        }
    }

}
