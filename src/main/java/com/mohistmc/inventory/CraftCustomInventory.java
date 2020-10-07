package com.mohistmc.inventory;

import javax.annotation.Nullable;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.PlayerArmorInvWrapper;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryCustom;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CraftCustomInventory implements InventoryHolder {

    private final CraftInventory container;

    public CraftCustomInventory(IInventory inventory) {
        this.container = new CraftInventory(inventory);
    }

    public CraftCustomInventory(ItemStackHandler handler) {
        this.container = new CraftInventoryCustom(this, handler.getStacks());
    }

    @Nullable
    public static InventoryHolder holderFromForge(IItemHandler handler) {
        if (handler == null) {
            return null;
        }
        if (handler instanceof ItemStackHandler) {
            return new CraftCustomInventory((ItemStackHandler) handler);
        }
        if (handler instanceof SlotItemHandler) {
            return new CraftCustomInventory(((SlotItemHandler) handler).inventory);
        }
        if (handler instanceof InvWrapper) {
            return new CraftCustomInventory(((InvWrapper) handler).getInv());
        }
        if (handler instanceof SidedInvWrapper) {
            return new CraftCustomInventory(((SidedInvWrapper) handler).getInv());
        }
        if (handler instanceof PlayerInvWrapper) {
            IItemHandlerModifiable[] piw = ObfuscationReflectionHelper.getPrivateValue(CombinedInvWrapper.class, (PlayerInvWrapper) handler, "itemHandler");
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
        InventoryHolder holder = holderFromForge(handler);
        return holder != null ? holder.getInventory() : null;
    }

    @Override
    public Inventory getInventory() {
        return this.container;
    }
}
