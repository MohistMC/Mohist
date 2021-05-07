package com.mohistmc.eventhandler.dispatcher;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.EntityTeleportEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerEventDispatcher {

    //For PlayerAdvancementDoneEvent
    @SubscribeEvent
    public void onAdvancementDone(AdvancementEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            Bukkit.getPluginManager().callEvent(new PlayerAdvancementDoneEvent(player.getBukkitEntity(), event.getAdvancement().bukkit));
        }
    }

    @SubscribeEvent
    public void onChorusFruit(EntityTeleportEvent.ChorusFruit event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            Player player = (Player) event.getEntityLiving().getBukkitEntity();
            PlayerTeleportEvent teleEvent = new PlayerTeleportEvent(player, player.getLocation(), new Location(player.getWorld(), event.getTargetX(), event.getTargetY(), event.getTargetZ()), PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT);
            teleEvent.setCancelled(event.isCanceled());
            Bukkit.getPluginManager().callEvent(teleEvent);
            event.setCanceled(teleEvent.isCancelled());
            event.setTargetX(teleEvent.getTo().getX());
            event.setTargetY(teleEvent.getTo().getY());
            event.setTargetZ(teleEvent.getTo().getZ());
        }
    }
}
