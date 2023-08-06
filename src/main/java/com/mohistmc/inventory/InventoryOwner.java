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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.PlayerArmorInvWrapper;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlockEntityState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import javax.annotation.Nullable;

/**
 * @author Mgazul
 * @date 2020/4/10 13:39
 */
public class InventoryOwner {

    public static InventoryHolder get(TileEntity te) {
        return get(te.level, te.getBlockPos(), true);
    }

    public static InventoryHolder get(IInventory inventory) {
        try {
            return inventory.getOwner();
        } catch (AbstractMethodError e) {
            return (inventory instanceof TileEntity) ? get((TileEntity) inventory) : null;
        }
    }

    public static InventoryHolder get(World world, BlockPos pos, boolean useSnapshot) {
        if (world == null) return null;
        // Spigot start
        org.bukkit.block.Block block = world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
        if (block == null) {
            return null;
        }
        // Spigot end
        org.bukkit.block.BlockState state = block.getState(useSnapshot);
        if (state instanceof InventoryHolder) {
            return (InventoryHolder) state;
        } else if (state instanceof CraftBlockEntityState) {
            TileEntity te = ((CraftBlockEntityState) state).getTileEntity();
            if (te instanceof IInventory) {
                return new CraftCustomInventory((IInventory) te);
            }
        }
        return null;
    }

    @Nullable
    public static InventoryHolder get(IItemHandler handler) {
        if (handler == null) {
            return null;
        }
        if (handler instanceof ItemStackHandler) {
            return new CraftCustomInventory((ItemStackHandler) handler);
        }
        if (handler instanceof SlotItemHandler) {
            return new CraftCustomInventory(((SlotItemHandler) handler).container);
        }
        if (handler instanceof InvWrapper) {
            return new CraftCustomInventory(((InvWrapper) handler).getInv());
        }
        if (handler instanceof SidedInvWrapper) {
            return new CraftCustomInventory(((SidedInvWrapper) handler).getInv());
        }
        if (handler instanceof PlayerInvWrapper) {
            IItemHandlerModifiable[] piw = ((PlayerInvWrapper) handler).itemHandler();
            for (IItemHandlerModifiable itemHandler : piw) {
                if (itemHandler instanceof PlayerMainInvWrapper) {
                    return new CraftCustomInventory(((PlayerMainInvWrapper) itemHandler).getInventoryPlayer());
                }
                if (itemHandler instanceof PlayerArmorInvWrapper) {
                    return new CraftCustomInventory(((PlayerArmorInvWrapper) itemHandler).getInventoryPlayer());
                }
            }
        }
        return null;
    }

    @Nullable
    public static Inventory inventoryFromForge(IItemHandler handler) {
        InventoryHolder holder = get(handler);
        return holder != null ? holder.getInventory() : null;
    }

}
