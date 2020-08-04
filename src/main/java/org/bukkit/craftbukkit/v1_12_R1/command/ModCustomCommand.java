package org.bukkit.craftbukkit.v1_12_R1.command;

import net.minecraft.command.CommandBase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ModCustomCommand extends Command {

    public ModCustomCommand(CommandBase modCommand, String description, String usage) {
        super(modCommand.getName(), description, usage, modCommand.getAliases());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        // Dummy method
        return false; //TODO test this method
    }
}