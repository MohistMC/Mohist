package red.mohist.mixin.command;

import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import org.bukkit.command.CommandSender;
import org.spongepowered.asm.mixin.Mixin;
import red.mohist.extra.command.ExtraCommandOutput;

@Mixin(CommandOutput.class)
public interface MixinCommandOutput extends ExtraCommandOutput {

    @Override
    public CommandSender getBukkitSender(ServerCommandSource source);

}
