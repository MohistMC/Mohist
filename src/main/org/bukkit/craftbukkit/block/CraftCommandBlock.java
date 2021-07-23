package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.CommandBlockEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.craftbukkit.util.CraftChatMessage;

public class CraftCommandBlock extends CraftBlockEntityState<CommandBlockEntity> implements CommandBlock {

    public CraftCommandBlock(Block block) {
        super(block, CommandBlockEntity.class);
    }

    public CraftCommandBlock(final Material material, final CommandBlockEntity te) {
        super(material, te);
    }

    @Override
    public String getCommand() {
        return getSnapshot().getCommandBlock().getCommand();
    }

    @Override
    public void setCommand(String command) {
        getSnapshot().getCommandBlock().setCommand(command != null ? command : "");
    }

    @Override
    public String getName() {
        return CraftChatMessage.fromComponent(getSnapshot().getCommandBlock().getName());
    }

    @Override
    public void setName(String name) {
        getSnapshot().getCommandBlock().setName(CraftChatMessage.fromStringOrNull(name != null ? name : "@"));
    }
}
