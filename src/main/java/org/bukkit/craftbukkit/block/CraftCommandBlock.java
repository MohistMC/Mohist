package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.CommandBlockTileEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.craftbukkit.util.CraftChatMessage;

public class CraftCommandBlock extends CraftBlockEntityState<CommandBlockTileEntity> implements CommandBlock {

    private String command;
    private String name;

    public CraftCommandBlock(Block block) {
        super(block, CommandBlockTileEntity.class);
    }

    public CraftCommandBlock(final Material material, final CommandBlockTileEntity te) {
        super(material, te);
    }

    @Override
    public void load(CommandBlockTileEntity commandBlock) {
        super.load(commandBlock);

        command = commandBlock.getCommandBlockLogic().getCommand();
        name = CraftChatMessage.fromComponent(commandBlock.getCommandBlockLogic().getName());
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public void setCommand(String command) {
        this.command = command != null ? command : "";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name != null ? name : "@";
    }

    @Override
    public void applyTo(CommandBlockTileEntity commandBlock) {
        super.applyTo(commandBlock);

        commandBlock.getCommandBlockLogic().setCommand(command);
        commandBlock.getCommandBlockLogic().setName(CraftChatMessage.fromStringOrNull(name));
    }
}
