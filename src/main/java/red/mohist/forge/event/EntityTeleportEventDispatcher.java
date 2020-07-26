package red.mohist.forge.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class EntityTeleportEventDispatcher {

    @SubscribeEvent(receiveCanceled = true)
    public void onTeleport(EnderTeleportEvent event) {
        if (!(event.getEntity() instanceof EndermanEntity)) {
            if (event.getEntity() instanceof ServerPlayerEntity) {
                CraftPlayer player = ((ServerPlayerEntity) event.getEntity()).getBukkitEntity();
                PlayerTeleportEvent bukkitEvent = new PlayerTeleportEvent(player, player.getLocation(), new Location(player.getWorld(), event.getTargetX(), event.getTargetY(), event.getTargetZ()), PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
                Bukkit.getPluginManager().callEvent(bukkitEvent);
                event.setCanceled(bukkitEvent.isCancelled());
                event.setTargetX(bukkitEvent.getTo().getX());
                event.setTargetY(bukkitEvent.getTo().getY());
                event.setTargetZ(bukkitEvent.getTo().getZ());
            } else {
                CraftEntity entity = ((Entity) event.getEntity()).getBukkitEntity();
                EntityTeleportEvent bukkitEvent = new EntityTeleportEvent(entity, entity.getLocation(), new Location(entity.getWorld(), event.getTargetX(), event.getTargetY(), event.getTargetZ()));
                Bukkit.getPluginManager().callEvent(bukkitEvent);
                event.setCanceled(bukkitEvent.isCancelled());
                event.setTargetX(bukkitEvent.getTo().getX());
                event.setTargetY(bukkitEvent.getTo().getY());
                event.setTargetZ(bukkitEvent.getTo().getZ());
            }
        }
    }
}
