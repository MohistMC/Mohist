package org.bukkit.craftbukkit.v1_12_R1.command;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ModCustomCommand extends Command {

    public ModCustomCommand(String name, String description, String usage, List<String> aliases) {
        super(name, description, usage, aliases);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        // Dummy method
        return false; //TODO test this method
    }
}