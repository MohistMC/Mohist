package red.mohist.api.mixin.command;

import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.command.ICommandSource;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author Mgazul
 * @date 2019/12/25 16:24
 */
@Mixin(ICommandSource.class)
public class MixinCommandSource {

    public CommandNode currentCommand; // CraftBukkit

    // CraftBukkit start
    public org.bukkit.command.CommandSender getBukkitSender() {
        MixinCommandSource base;
        return base.getBukkitSender(this);
    }
    // CraftBukkit end
}
