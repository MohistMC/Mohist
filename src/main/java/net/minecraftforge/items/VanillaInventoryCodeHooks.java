/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.items;

import com.mohistmc.bukkit.inventory.InventoryOwner;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.DropperBlock;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class VanillaInventoryCodeHooks
{
    /**
     * Copied from TileEntityHopper#captureDroppedItems and added capability support
     * @return Null if we did nothing {no IItemHandler}, True if we moved an item, False if we moved no items
     */
    @Nullable
    public static Boolean extractHook(Level level, Hopper dest)
    {
        return getItemHandler(level, dest, Direction.UP)
                .map(itemHandlerResult -> {
                    IItemHandler handler = itemHandlerResult.getKey();
                    Container container = HopperBlockEntity.getSourceContainer(level, dest);
                    for (int i = 0; i < handler.getSlots(); i++)
                    {
                        ItemStack extractItem = handler.extractItem(i, level.spigotConfig.hopperAmount, true);

                        if (!extractItem.isEmpty())
                        {
                            CraftItemStack oitemstack = CraftItemStack.asCraftMirror(extractItem);

                            InventoryHolder owner = InventoryOwner.get(dest);
                            if (container != null) {
                                Inventory sourceInventory = InventoryOwner.getInventory(container);
                                Inventory destinationInventory = owner != null ? owner.getInventory() : InventoryOwner.inventoryFromForge(handler);
                                if (destinationInventory != null && sourceInventory != null) {
                                    InventoryMoveItemEvent event;
                                    try {
                                        event = new InventoryMoveItemEvent(sourceInventory, oitemstack.clone(), destinationInventory, true);
                                        Bukkit.getPluginManager().callEvent(event);
                                        if (event.isCancelled()) {
                                            extractItem = ItemStack.EMPTY;
                                        } else {
                                            extractItem = CraftItemStack.asNMSCopy(event.getItem());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            for (int j = 0; j < dest.getContainerSize(); j++)
                            {
                                ItemStack destStack = dest.getItem(j);
                                if (dest.canPlaceItem(j, extractItem) && (destStack.isEmpty() || destStack.getCount() < destStack.getMaxStackSize() && destStack.getCount() < dest.getMaxStackSize() && ItemHandlerHelper.canItemStacksStack(extractItem, destStack)))
                                {
                                    extractItem = handler.extractItem(i, 1, false);
                                    if (destStack.isEmpty())
                                        dest.setItem(j, extractItem);
                                    else
                                    {
                                        destStack.grow(1);
                                        dest.setItem(j, destStack);
                                    }
                                    dest.setChanged();
                                    return true;
                                }
                            }
                        }
                    }
                    return false;
                }).orElse(null); // TODO bad null
    }

    /**
     * Copied from BlockDropper#dispense and added capability support
     */
    public static boolean dropperInsertHook(Level level, BlockPos pos, DispenserBlockEntity dropper, int slot, @NotNull ItemStack stack)
    {
        Direction enumfacing = level.getBlockState(pos).getValue(DropperBlock.FACING);
        BlockPos blockpos = pos.relative(enumfacing);
        return getItemHandler(level, (double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ(), enumfacing.getOpposite())
                .map(destinationResult -> {
                    IItemHandler itemHandler = destinationResult.getKey();
                    Object destination = destinationResult.getValue();
                    // CraftBukkit start - Fire event when pushing items into other inventories
                    CraftItemStack oitemstack = CraftItemStack.asCraftMirror(stack.copy().split(1));
                    InventoryHolder owner = InventoryOwner.get((BlockEntity) destination);
                    Inventory destinationInventory = owner != null ? owner.getInventory() : InventoryOwner.inventoryFromForge(itemHandler);
                    InventoryMoveItemEvent event = new InventoryMoveItemEvent(dropper.getOwner().getInventory(), oitemstack.clone(), destinationInventory, true);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return true;
                    }
                    ItemStack remainder = putStackInInventoryAllSlots(dropper, destination, itemHandler, CraftItemStack.asNMSCopy(event.getItem()));

                    if (event.getItem().equals(oitemstack) && remainder.isEmpty())
                    {
                        remainder = stack.copy();
                        remainder.shrink(1);
                    }
                    else
                    {
                        remainder = stack.copy();
                    }

                    dropper.setItem(slot, remainder);
                    return false;
                })
                .orElse(true);
    }

    /**
     * Copied from TileEntityHopper#transferItemsOut and added capability support
     */
    public static boolean insertHook(HopperBlockEntity hopper)
    {
        Direction hopperFacing = hopper.getBlockState().getValue(HopperBlock.FACING);
        return getItemHandler(hopper.getLevel(), hopper, hopperFacing)
                .map(destinationResult -> {
                    IItemHandler itemHandler = destinationResult.getKey();
                    Object destination = destinationResult.getValue();
                    if (isFull(itemHandler))
                    {
                        return false;
                    }
                    else
                    {
                        for (int i = 0; i < hopper.getContainerSize(); ++i)
                        {
                            if (!hopper.getItem(i).isEmpty())
                            {
                                ItemStack originalSlotContents = hopper.getItem(i).copy();
                                ItemStack insertStack = hopper.removeItem(i, hopper.getLevel().spigotConfig.hopperAmount);
                                ItemStack stack = insertStack;
                                if (!insertStack.isEmpty()) {
                                    CraftItemStack oitemstack = CraftItemStack.asCraftMirror(insertStack);
                                    InventoryHolder owner = InventoryOwner.get((BlockEntity) destination);
                                    Inventory sourceInventory = InventoryOwner.getInventory(hopper);
                                    Inventory destinationInventory = owner != null ? owner.getInventory() : InventoryOwner.inventoryFromForge(itemHandler);
                                    if (sourceInventory != null && destinationInventory != null) {
                                        InventoryMoveItemEvent event = new InventoryMoveItemEvent(sourceInventory, oitemstack.clone(), destinationInventory, true);
                                        Bukkit.getPluginManager().callEvent(event);
                                        if (event.isCancelled()) {
                                            hopper.setItem(i, originalSlotContents);
                                            hopper.setCooldown(hopper.getLevel().spigotConfig.hopperTransfer); // Spigot
                                            return true;
                                        }
                                        stack = CraftItemStack.asNMSCopy(event.getItem());
                                    }
                                }
                                int origCount = insertStack.getCount();
                                ItemStack remainder = putStackInInventoryAllSlots(hopper, destination, itemHandler, stack);

                                if (remainder.isEmpty())
                                {
                                    return true;
                                }

                                originalSlotContents.shrink(origCount - remainder.getCount()); // Spigot
                                hopper.setItem(i, originalSlotContents);
                            }
                        }

                        return false;
                    }
                })
                .orElse(false);
    }

    private static ItemStack putStackInInventoryAllSlots(BlockEntity source, Object destination, IItemHandler destInventory, ItemStack stack)
    {
        for (int slot = 0; slot < destInventory.getSlots() && !stack.isEmpty(); slot++)
        {
            stack = insertStack(source, destination, destInventory, stack, slot);
        }
        return stack;
    }

    /**
     * Copied from TileEntityHopper#insertStack and added capability support
     */
    private static ItemStack insertStack(BlockEntity source, Object destination, IItemHandler destInventory, ItemStack stack, int slot)
    {
        ItemStack itemstack = destInventory.getStackInSlot(slot);

        if (destInventory.insertItem(slot, stack, true).isEmpty())
        {
            boolean insertedItem = false;
            boolean inventoryWasEmpty = isEmpty(destInventory);

            if (itemstack.isEmpty())
            {
                destInventory.insertItem(slot, stack, false);
                stack = ItemStack.EMPTY;
                insertedItem = true;
            }
            else if (ItemHandlerHelper.canItemStacksStack(itemstack, stack))
            {
                int originalSize = stack.getCount();
                stack = destInventory.insertItem(slot, stack, false);
                insertedItem = originalSize < stack.getCount();
            }

            if (insertedItem)
            {
                if (inventoryWasEmpty && destination instanceof HopperBlockEntity)
                {
                    HopperBlockEntity destinationHopper = (HopperBlockEntity)destination;

                    if (!destinationHopper.isOnCustomCooldown())
                    {
                        int k = 0;
                        if (source instanceof HopperBlockEntity)
                        {
                            if (destinationHopper.getLastUpdateTime() >= ((HopperBlockEntity) source).getLastUpdateTime())
                            {
                                k = 1;
                            }
                        }
                        destinationHopper.setCooldown(8 - k);
                    }
                }
            }
        }

        return stack;
    }

    private static Optional<Pair<IItemHandler, Object>> getItemHandler(Level level, Hopper hopper, Direction hopperFacing)
    {
        double x = hopper.getLevelX() + (double) hopperFacing.getStepX();
        double y = hopper.getLevelY() + (double) hopperFacing.getStepY();
        double z = hopper.getLevelZ() + (double) hopperFacing.getStepZ();
        return getItemHandler(level, x, y, z, hopperFacing.getOpposite());
    }

    private static boolean isFull(IItemHandler itemHandler)
    {
        for (int slot = 0; slot < itemHandler.getSlots(); slot++)
        {
            ItemStack stackInSlot = itemHandler.getStackInSlot(slot);
            if (stackInSlot.isEmpty() || stackInSlot.getCount() < itemHandler.getSlotLimit(slot))
            {
                return false;
            }
        }
        return true;
    }

    private static boolean isEmpty(IItemHandler itemHandler)
    {
        for (int slot = 0; slot < itemHandler.getSlots(); slot++)
        {
            ItemStack stackInSlot = itemHandler.getStackInSlot(slot);
            if (stackInSlot.getCount() > 0)
            {
                return false;
            }
        }
        return true;
    }

    public static Optional<Pair<IItemHandler, Object>> getItemHandler(Level worldIn, double x, double y, double z, final Direction side)
    {
        int i = Mth.floor(x);
        int j = Mth.floor(y);
        int k = Mth.floor(z);
        BlockPos blockpos = new BlockPos(i, j, k);
        BlockState state = worldIn.getBlockState(blockpos);

        if (state.hasBlockEntity())
        {
            BlockEntity blockEntity = worldIn.getBlockEntity(blockpos);
            if (blockEntity != null)
            {
                return blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, side)
                    .map(capability -> ImmutablePair.<IItemHandler, Object>of(capability, blockEntity));
            }
        }

        return Optional.empty();
    }
}
