package org.bukkit.craftbukkit.v1_12_R1.command;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;

import java.util.Arrays;
import java.util.regex.Pattern;

public class CraftSimpleCommandMap extends SimpleCommandMap {
    private static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", Pattern.LITERAL);
    private ICommandSender vanillaConsoleSender;

    public CraftSimpleCommandMap(Server server) {
        super(server);
    }

    /**
     * {@inheritDoc}
     */
    public boolean dispatch(CommandSender sender, String commandLine) throws CommandException {
        String[] args = PATTERN_ON_SPACE.split(commandLine);

        if (args.length == 0) {
            return false;
        }

        String sentCommandLabel = args[0].toLowerCase();
        Command target = getCommand(sentCommandLabel);

        if (target == null) {
            return false;
        }
        try {
            // Cauldron start - if command is a mod command, check permissions and route through vanilla
            if (target instanceof ModCustomCommand) {
                if (!target.testPermission(sender)) {
                    return true;
                }
                if (sender instanceof ConsoleCommandSender) {
                    FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(this.vanillaConsoleSender, commandLine);
                } else {
                    FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(((CraftPlayer) sender).getHandle(), commandLine);
                }
            } else {
                // Cauldron end
                // Note: we don't return the result of target.execute as thats success / failure, we return handled (true) or not handled (false)
                target.execute(sender, sentCommandLabel, Arrays.copyOfRange(args, 1, args.length)); //TODO testing Arrays.copyOfRange()..
            }
        } catch (CommandException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new CommandException("Unhandled exception executing '" + commandLine + "' in " + target, ex);
        }
        // return true as command was handled
        return true;
    }

    // Cauldron start - sets the vanilla console sender
    public void setVanillaConsoleSender(ICommandSender console) {
        this.vanillaConsoleSender = console;
    }
    // Cauldron end
}
