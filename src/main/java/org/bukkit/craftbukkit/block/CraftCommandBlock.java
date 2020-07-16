package org.bukkit.craftbukkit.block;

import net.minecraft.block.entity.CommandBlockBlockEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.craftbukkit.util.CraftChatMessage;

public class CraftCommandBlock extends CraftBlockEntityState<CommandBlockBlockEntity> implements CommandBlock {

    private String command;
    private String name;

    public CraftCommandBlock(Block block) {
        super(block, CommandBlockBlockEntity.class);
    }

    public CraftCommandBlock(final Material material, final CommandBlockBlockEntity te) {
        super(material, te);
    }

    @Override
    public void load(CommandBlockBlockEntity commandBlock) {
        super.load(commandBlock);

        command = commandBlock.getCommandExecutor().getCommand();
        name = CraftChatMessage.fromComponent(commandBlock.getCommandExecutor().getCustomName());
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
    public void applyTo(CommandBlockBlockEntity commandBlock) {
        super.applyTo(commandBlock);

        commandBlock.getCommandExecutor().setCommand(command);
        commandBlock.getCommandExecutor().setCustomName(CraftChatMessage.fromStringOrNull(name));
    }
}
