package com.mohistmc.eventhandler;

import com.google.common.collect.Lists;
import com.mohistmc.forge.ForgeToBukkit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EntityEventDispatcher {

    @SubscribeEvent(receiveCanceled = true)
    public void onTeleport(EnderTeleportEvent event) {
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

    @SubscribeEvent(receiveCanceled = true)
    public void onLivingDeath(LivingDropsEvent event) {
        if (event.getEntityLiving() instanceof ServerPlayerEntity) {
            return;
        }
        LivingEntity livingEntity = event.getEntityLiving();
        Collection<ItemEntity> drops = event.getDrops();
        if (!(drops instanceof ArrayList)) {
            drops = new ArrayList<>(drops);
        }
        List<ItemStack> itemStackList = Lists.transform((List<ItemEntity>) drops,
                (ItemEntity entity) -> CraftItemStack.asCraftMirror(entity.getItem()));
        CraftEventFactory.callEntityDeathEvent(livingEntity, itemStackList);
        if (drops.isEmpty()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onExpire(ItemExpireEvent event) {
        event.setCanceled(CraftEventFactory.callItemDespawnEvent(event.getEntityItem()).isCancelled());
    }

    @SubscribeEvent
    public void onEntityTame(AnimalTameEvent event) {
        event.setCanceled(CraftEventFactory.callEntityTameEvent(event.getAnimal(), event.getTamer()).isCancelled());
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof LivingEntity && !(event.getEntity() instanceof PlayerEntity)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onItemTossEvent(ItemTossEvent event) {
        event.setCanceled(CraftEventFactory.callItemSpawnEvent(event.getEntityItem()).isCancelled());
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onLivingAttackEvent(LivingAttackEvent event) {
        EntityDamageEvent bev;
        if (event.getSource().getTrueSource() != null) {
            bev = new EntityDamageByEntityEvent(
                    CraftEntity.getEntity(CraftServer.getInstance(),
                            event.getSource().getTrueSource()),
                    CraftEntity.getEntity(CraftServer.getInstance(),
                            event.getEntity()),
                    ForgeToBukkit.getDamageCause(event.getSource()), event.getAmount());
        } else {
            bev = new EntityDamageEvent(
                    CraftEntity.getEntity(CraftServer.getInstance(), event.getEntity()),
                    ForgeToBukkit.getDamageCause(event.getSource()),
                    event.getAmount());
        }
        bev.setCancelled(event.isCanceled());
        Bukkit.getPluginManager().callEvent(bev);

        event.setCanceled(bev.isCancelled());
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onTarget(LivingSetAttackTargetEvent event) {
        CraftEventFactory.callEntityTargetEvent(event.getEntity(), event.getTarget(), EntityTargetEvent.TargetReason.CUSTOM);
    }
}
