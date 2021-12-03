package com.mohistmc.eventhandler.dispatcher;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerEventDispatcher {

    //For PlayerAdvancementDoneEvent
    @SubscribeEvent
    public void onAdvancementDone(AdvancementEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            Bukkit.getPluginManager().callEvent(new PlayerAdvancementDoneEvent(player.getBukkitEntity(), event.getAdvancement().bukkit));
        }
    }

    //For PlayerRespawnEvent
    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            Bukkit.getPluginManager().callEvent(new PlayerRespawnEvent(player.getBukkitEntity(), player.getBukkitEntity().getLocation(), false, false));
        }
    }
}
