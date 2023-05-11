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

package com.mohistmc.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.craftbukkit.v1_19_R3.block.CraftBlockEntityState;
import org.bukkit.inventory.InventoryHolder;

/**
 * @author Mgazul
 * @date 2020/4/10 13:39
 */
public class InventoryOwner {

    public static InventoryHolder get(BlockEntity te) {
        return get(te.getLevel(), te.getBlockPos(), true);
    }

    public static InventoryHolder get(Container inventory) {
        try {
            return inventory.getOwner();
        } catch (AbstractMethodError e) {
            return (inventory instanceof BlockEntity blockEntity) ? get(blockEntity) : null;
        }
    }

    public static InventoryHolder get(Level world, BlockPos pos, boolean useSnapshot) {
        if (world == null) return null;
        // Spigot start
        org.bukkit.block.Block block = world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
        if (block == null) {
            return null;
        }
        // Spigot end
        org.bukkit.block.BlockState state = block.getState();
        if (state instanceof InventoryHolder) {
            return (InventoryHolder) state;
        } else if (state instanceof CraftBlockEntityState) {
            BlockEntity te = ((CraftBlockEntityState) state).getTileEntity();
            if (te instanceof Container container) {
                return new CraftCustomInventory(container);
            }
        }
        return null;
    }
}
