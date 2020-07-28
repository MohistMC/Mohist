package org.bukkit.craftbukkit.v1_16_R1.command;

import java.util.Map;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;

public class CraftCommandMap extends SimpleCommandMap {

    public CraftCommandMap(Server server) {
        super(server);

        // Register our commands
        for (String s : new String[] {"version", "ver", "about"});
    }

    public Map<String, Command> getKnownCommands() {
        return knownCommands;
    }

}