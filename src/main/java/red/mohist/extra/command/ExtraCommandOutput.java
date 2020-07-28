package red.mohist.extra.command;

import org.bukkit.command.CommandSender;
import net.minecraft.server.command.ServerCommandSource;

public interface ExtraCommandOutput {

    CommandSender getBukkitSender(ServerCommandSource serverCommandSource);

}