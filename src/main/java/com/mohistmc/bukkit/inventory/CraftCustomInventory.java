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

package com.mohistmc.bukkit.inventory;

import java.util.List;
import net.minecraft.world.Container;
import net.minecraftforge.items.ItemStackHandler;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventoryPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class CraftCustomInventory implements InventoryHolder {

    private final CraftInventory container;

    public CraftCustomInventory(Container inventory) {
        this.container = new CraftInventory(inventory);
    }

    public CraftCustomInventory(ItemStackHandler handler) {
        this.container = new CraftInventoryCustom(this, handler.stacks);
    }

    public CraftCustomInventory(net.minecraft.world.entity.player.Inventory playerInventory) {
        this.container = new CraftInventoryPlayer(playerInventory);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.container;
    }

    public static List<HumanEntity> getViewers(Container inventory) {
        try {
            return inventory.getViewers();
        } catch (AbstractMethodError e) {
            return new java.util.ArrayList<>();
        }
    }
}
