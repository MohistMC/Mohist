package red.mohist.forge;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import net.minecraftforge.eventbus.api.Event;
import org.bukkit.Bukkit;
import red.mohist.api.event.BukkitHookForgeEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *This class is make BukkitHookForge event add.
 *
 * @author 1798643961
 */
public class EventAddTransformer implements ITransformer<Boolean> {

    public Event event;

    @Override
    public Boolean transform(Boolean input, ITransformerVotingContext context) {
        if (Bukkit.getServer() != null) {
            BukkitHookForgeEvent bukkitHookForgeEvent = new BukkitHookForgeEvent(event);
            if (bukkitHookForgeEvent.getHandlers().getRegisteredListeners().length > 0) {
                org.bukkit.Bukkit.getPluginManager().callEvent(bukkitHookForgeEvent);
            }
        }
        return event.isCancelable() && event.isCanceled();
    }

    @Override
    public TransformerVoteResult castVote(ITransformerVotingContext context) {
        return TransformerVoteResult.YES;
    }

    @Override
    public Set<Target> targets() {
        return new HashSet<Target>(Arrays.asList(Target.targetClass("EventBus")));
    }
}
