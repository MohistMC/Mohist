package red.mohist.test;

import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginLoadOrder;
import red.mohist.api.event.BukkitHookForgeEvent;
import red.mohist.api.event.BukkitStateForgeEvent;

public class BukkitHookForgeEventTest implements Listener {

    /**
     * Using Bukkit to handle Forge ExplosionEvent
     *
     * @param event
     */
    @EventHandler
    public void test(BukkitHookForgeEvent event){
        if (event.getEvent() instanceof ExplosionEvent.Detonate) {
            ExplosionEvent.Detonate explosionEvent = (ExplosionEvent.Detonate)event.getEvent();
            explosionEvent.getAffectedBlocks().clear();
        }
    }

    @SubscribeEvent
    public void test2(BukkitStateForgeEvent.PluginsEnable event) {
        if(event.getType().equals(PluginLoadOrder.POSTWORLD) && event.getServer().getPluginManager().getPlugin("Mohist") != null){
            event.getServer().getConsoleSender().sendMessage("Thank you for using Mohist!");
        }
    }
}
