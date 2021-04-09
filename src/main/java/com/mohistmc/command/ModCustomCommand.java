package com.mohistmc.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ModCustomCommand extends Command {

    public ModCustomCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        // Dummy method
        return false; //TODO test this method
    }
}