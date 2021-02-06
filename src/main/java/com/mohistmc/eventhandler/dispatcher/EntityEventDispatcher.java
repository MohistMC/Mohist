package com.mohistmc.eventhandler.dispatcher;

import net.minecraft.entity.item.ItemEntity;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.craftbukkit.v1_16_R3.event.CraftEventFactory;

public class EntityEventDispatcher {

    //For ItemDespawnEvent
    @SubscribeEvent(receiveCanceled = true)
    public void onItemDespawnEvent(ItemExpireEvent event){
        ItemEntity itemEntity = event.getEntityItem();
        if (CraftEventFactory.callItemDespawnEvent(itemEntity).isCancelled()) {
            event.getEntityItem().age = 0;
            return;
        }
    }
}
