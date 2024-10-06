package org.bukkit.craftbukkit.v1_20_R1.block;

import com.mohistmc.paper.adventure.PaperAdventure;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import org.bukkit.World;
import org.bukkit.block.CommandBlock;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftChatMessage;

public class CraftCommandBlock extends CraftBlockEntityState<CommandBlockEntity> implements CommandBlock {

    public CraftCommandBlock(World world, final CommandBlockEntity te) {
        super(world, te);
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

    // Paper start
    @Override
    public net.kyori.adventure.text.Component name() {
        return PaperAdventure.asAdventure(getSnapshot().getCommandBlock().getName());
    }

    @Override
    public void name(net.kyori.adventure.text.Component name) {
        getSnapshot().getCommandBlock().setName(name == null ? net.minecraft.network.chat.Component.literal("@") : PaperAdventure.asVanilla(name));
    }
    // Paper end
}
