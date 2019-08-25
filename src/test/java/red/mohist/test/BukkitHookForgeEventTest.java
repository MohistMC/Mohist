package red.mohist.test;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginLoadOrder;
import red.mohist.event.BukkitHookForgeEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import red.mohist.event.BukkitStateForgeEvent;
import red.mohist.event.ForgeHookBukkitEvent;

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

    /**
     * Using mod to handle bukkit PlayerJoinEvent
     *
     * @param event
     */
    @Deprecated
    @SubscribeEvent
    public void test1(ForgeHookBukkitEvent event) {
        if (event.getEvent() instanceof PlayerJoinEvent) {
            PlayerJoinEvent playerJoinEvent = (PlayerJoinEvent)event.getEvent();
            playerJoinEvent.setJoinMessage("Thank you for using Mohist!");
            Player player = playerJoinEvent.getPlayer();
            player.getInventory().addItem(new ItemStack(Material.APPLE));
        }
    }

    @SubscribeEvent
    public void test2(BukkitStateForgeEvent.PluginsEnable event) {
        if(event.getType().equals(PluginLoadOrder.POSTWORLD) && event.getServer().getPluginManager().getPlugin("Mohist") != null){
            event.getServer().getConsoleSender().sendMessage("Thank you for using Mohist!");
        }
    }
}
