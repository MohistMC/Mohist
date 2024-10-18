package com.mohistmc.eventhandler.dispatcher;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.craftbukkit.v1_18_R2.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class EntityEventDispatcher {

    @SubscribeEvent(receiveCanceled = true)
    public void onLivingDropsEvent(LivingDropsEvent event) {
        if (!(event.getEntityLiving() instanceof ServerPlayer)) {
            LivingEntity livingEntity = event.getEntityLiving();
            List<ItemStack> bukkitDrops = new ArrayList<>();
            for (ItemEntity forgeDrop : event.getDrops()) {
                bukkitDrops.add(CraftItemStack.asCraftMirror(forgeDrop.getItem()));
            }

            CraftEventFactory.callEntityDeathEvent(livingEntity, bukkitDrops);
        }
    }

}
