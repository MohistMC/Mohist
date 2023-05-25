/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2023.
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

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class EntityEventDispatcher {

    @SubscribeEvent(receiveCanceled = true)
    public void onEntityMountEvent(EntityMountEvent event) {
        Entity passenger = event.getEntityMounting();
        Entity passenger1 = event.getEntityBeingMounted();
        if (event.isDismounting()) {
            // CraftBukkit start
            CraftEntity craft = (CraftEntity) passenger1.getBukkitEntity().getVehicle();
            Entity orig = craft == null ? null : craft.getHandle();
            if (passenger1.getBukkitEntity() instanceof Vehicle && passenger1.getBukkitEntity() instanceof org.bukkit.entity.LivingEntity) {
                VehicleExitEvent CBevent = new VehicleExitEvent(
                        (Vehicle) passenger1.getBukkitEntity(),
                        (org.bukkit.entity.LivingEntity) passenger.getBukkitEntity()
                );
                Bukkit.getPluginManager().callEvent(CBevent);
                CraftEntity craftn = (CraftEntity) passenger1.getBukkitEntity().getVehicle();
                Entity n = craftn == null ? null : craftn.getHandle();
                CBevent.setCancelled(event.isCanceled());
                if (CBevent.isCancelled() || n != orig) {
                    event.setCanceled(true);
                }
            }
            // CraftBukkit end
            // Spigot start
            org.spigotmc.event.entity.EntityDismountEvent SPevent = new org.spigotmc.event.entity.EntityDismountEvent(passenger1.getBukkitEntity(), passenger.getBukkitEntity());
            // Suppress during worldgen
            if (passenger.valid) {
                Bukkit.getPluginManager().callEvent(SPevent);
            }
            SPevent.setCancelled(event.isCanceled());
            if (SPevent.isCancelled()) {
                event.setCanceled(true);
            }
            // Spigot end
        }
        if (event.isMounting()) {
            // CraftBukkit start
            com.google.common.base.Preconditions.checkState(!passenger1.passengers.contains(this), "Circular entity riding! %s %s", this, passenger);
            CraftEntity craft = (CraftEntity) passenger1.getBukkitEntity().getVehicle();
            Entity orig = craft == null ? null : craft.getHandle();
            if (passenger.getBukkitEntity() instanceof Vehicle && passenger1.getBukkitEntity() instanceof org.bukkit.entity.LivingEntity) {
                VehicleEnterEvent CBevent = new VehicleEnterEvent(
                        (Vehicle) passenger.getBukkitEntity(),
                        passenger1.getBukkitEntity()
                );
                // Suppress during worldgen
                if (passenger.valid) {
                    Bukkit.getPluginManager().callEvent(CBevent);
                }
                CraftEntity craftn = (CraftEntity) passenger1.getBukkitEntity().getVehicle();
                Entity n = craftn == null ? null : craftn.getHandle();
                CBevent.setCancelled(event.isCanceled());
                if (CBevent.isCancelled() || n != orig) {
                    event.setCanceled(true);
                }
            }
            // CraftBukkit end
            // Spigot start
            org.spigotmc.event.entity.EntityMountEvent SPevent = new org.spigotmc.event.entity.EntityMountEvent(passenger1.getBukkitEntity(), passenger.getBukkitEntity());
            // Suppress during worldgen
            if (passenger.valid) {
                Bukkit.getPluginManager().callEvent(SPevent);
            }
            SPevent.setCancelled(event.isCanceled());
            if (SPevent.isCancelled()) {
                event.setCanceled(true);
            }
            // Spigot end
        }
    }

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

}
