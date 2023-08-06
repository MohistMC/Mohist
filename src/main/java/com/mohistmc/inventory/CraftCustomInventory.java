/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2022.
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

package com.mohistmc.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.ItemStackHandler;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryCustom;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public class CraftCustomInventory implements InventoryHolder {

    private final CraftInventory container;

    public CraftCustomInventory(IInventory inventory) {
        this.container = new CraftInventory(inventory);
    }

    public CraftCustomInventory(ItemStackHandler handler) {
        this.container = new CraftInventoryCustom(this, handler.getStacks());
    }

    @Override
    public Inventory getInventory() {
        return this.container;
    }

    public static List<HumanEntity> getViewers(IInventory inventory) {
        try {
            return inventory.getViewers();
        } catch (AbstractMethodError e) {
            return new java.util.ArrayList<>();
        }
    }
}
