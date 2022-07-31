/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.items.wrapper;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.items.IItemHandler;

/**
 * Exposes the hands inventory of an {@link LivingEntity} as an {@link IItemHandler} using {@link LivingEntity#getItemStackFromSlot} and
 * {@link LivingEntity#setSlot}.
 */
public class EntityHandsInvWrapper extends EntityEquipmentInvWrapper
{
    public EntityHandsInvWrapper(LivingEntity entity)
    {
        super(entity, EquipmentSlotType.Group.HAND);
    }
}
