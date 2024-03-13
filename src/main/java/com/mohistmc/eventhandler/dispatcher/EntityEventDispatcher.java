/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2024.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.eventhandler.dispatcher;

import io.izzel.tools.collection.XmapList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EntityEventDispatcher {

    @SubscribeEvent(receiveCanceled = true)
    public void changeTargetEvent(LivingChangeTargetEvent event) {
        EntityTargetEvent.TargetReason reason = event.getReason();
        LivingEntity entityliving = event.getNewTarget();

        if (entityliving instanceof Mob mob) {
            if (event.isFireCBEvent()) {
                if (reason == EntityTargetEvent.TargetReason.UNKNOWN && mob.getTarget() != null && entityliving == null) {
                    reason = mob.getTarget().isAlive() ? EntityTargetEvent.TargetReason.FORGOT_TARGET : EntityTargetEvent.TargetReason.TARGET_DIED;
                }
                CraftLivingEntity ctarget = null;
                if (entityliving != null) {
                    ctarget = (CraftLivingEntity) entityliving.getBukkitEntity();
                }
                EntityTargetLivingEntityEvent CBevent = new EntityTargetLivingEntityEvent(event.getEntity().getBukkitEntity(), ctarget, reason);
                Bukkit.getPluginManager().callEvent(CBevent);
                if (CBevent.isCancelled()) {
                    event.setCanceled(true);
                } else {
                    if (CBevent.getTarget() != null) {
                        event.setNewTarget(((CraftLivingEntity) CBevent.getTarget()).getHandle());
                    }
                }
            }
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onLivingDeath(LivingDropsEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            return;
        }
        LivingEntity livingEntity = event.getEntity();
        Collection<ItemEntity> drops = event.getDrops();
        if (!(drops instanceof ArrayList)) {
            drops = new ArrayList<>(drops);
        }
        List<ItemStack> itemStackList = XmapList.create((List<ItemEntity>) drops, ItemStack.class, (ItemEntity entity) -> CraftItemStack.asCraftMirror(entity.getItem()), itemStack -> {
            ItemEntity itemEntity = new ItemEntity(livingEntity.level(), livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), CraftItemStack.asNMSCopy(itemStack));
            itemEntity.setDefaultPickUpDelay();
            return itemEntity;
        });

        CraftLivingEntity craftLivingEntity = livingEntity.getBukkitLivingEntity();
        EntityDeathEvent eventCB = new EntityDeathEvent(craftLivingEntity, itemStackList, livingEntity.getExperienceReward());
        Bukkit.getPluginManager().callEvent(eventCB);

        if (drops.isEmpty()) {
            event.setCanceled(true);
        }
    }
}
