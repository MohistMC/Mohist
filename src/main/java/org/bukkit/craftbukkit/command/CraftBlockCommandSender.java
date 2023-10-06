package org.bukkit.craftbukkit.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandBlockLogic;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;

/**
 * Represents input from a command block
 */
public class CraftBlockCommandSender extends ServerCommandSender implements BlockCommandSender {
    private final CommandBlockLogic commandBlock;

    public CraftBlockCommandSender(CommandBlockLogic commandBlockListenerAbstract) {
        super();
        this.commandBlock = commandBlockListenerAbstract;
    }

    public Block getBlock() {
        return commandBlock.getEntityWorld().getWorld().getBlockAt(commandBlock.getPlayerCoordinates().posX, commandBlock.getPlayerCoordinates().posY, commandBlock.getPlayerCoordinates().posZ);
    }

    public void sendMessage(String message) {
    }

    public void sendMessage(String[] messages) {
    }

    public String getName() {
        return commandBlock.getCommandSenderName();
    }

    public boolean isOp() {
        return true;
    }

    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of a block");
    }

    public ICommandSender getTileEntity() {
        return commandBlock;
    }
}
