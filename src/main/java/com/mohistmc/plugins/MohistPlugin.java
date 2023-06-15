package com.mohistmc.plugins;

import com.mohistmc.plugins.world.WorldManage;
import com.mohistmc.plugins.world.commands.WorldsCommands;
import com.mohistmc.plugins.world.listener.InventoryClickListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;

/**
 * @author Mgazul by MohistMC
 * @date 2023/6/14 14:46:34
 */
public class MohistPlugin {

    public static Logger LOGGER = LogManager.getLogger("MohistPlugin");

    public static void init(Server server) {
        WorldManage.onEnable();
    }

    public static void registerCommands(Map<String, Command> map) {
        map.put(WorldManage.command, new WorldsCommands(WorldManage.command));
    }

    public static void registerListener(Event event) {
        if (event instanceof InventoryClickEvent inventoryClickEvent) {
            InventoryClickListener.init(inventoryClickEvent);
        }

    }

}
