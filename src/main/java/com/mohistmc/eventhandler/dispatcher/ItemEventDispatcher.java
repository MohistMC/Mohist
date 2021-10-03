package com.mohistmc.eventhandler.dispatcher;

import net.minecraft.entity.item.ItemEntity;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.event.CraftEventFactory;

public class ItemEventDispatcher {

    @SubscribeEvent(receiveCanceled = true)
    public void onItemExpireEvent(ItemExpireEvent event) {
        if (Bukkit.getServer() instanceof CraftServer) {
            // CraftBukkit start - fire ItemDespawnEvent
            ItemEntity entity = event.getEntityItem();
            if (CraftEventFactory.callItemDespawnEvent(entity).isCancelled()) {
                entity.age = 0;
                return;
            }
            // CraftBukkit end
        }
    }
}
