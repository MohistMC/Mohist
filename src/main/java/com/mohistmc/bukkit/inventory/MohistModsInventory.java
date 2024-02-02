package com.mohistmc.bukkit.inventory;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/10 22:30:10
 */
public class MohistModsInventory implements Container {

    private final AbstractContainerMenu container;
    private final InventoryHolder owner;
    private final List<HumanEntity> viewers = new ArrayList<>();

    public MohistModsInventory(AbstractContainerMenu container, Player owner) {
        this.container = container;
        this.owner = owner.getBukkitEntity();
    }

    public AbstractContainerMenu getContainer() {
        return container;
    }

    @Override
    public void clearContent() {
        for (Slot slot : this.container.slots) {
            slot.remove(Integer.MAX_VALUE);
        }
    }

    @Override
    public int getContainerSize() {
        return this.container.lastSlots.size();
    }

    @Override
    public int getMaxStackSize() {
        if (getContainerSize() <= 0)
            return 0;
        return container.getSlot(0).getMaxStackSize();
    }

    @Override
    public boolean isEmpty() {
        for (Slot slot : container.slots) {
            if (!slot.getItem().isEmpty())
                return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int p_18941_) {
        if (p_18941_ >= getContainerSize())
            return ItemStack.EMPTY;
        return container.getSlot(p_18941_).getItem();
    }

    @Override
    public ItemStack removeItem(int p_18942_, int p_18943_) {
        if (p_18942_ >= getContainerSize())
            return ItemStack.EMPTY;
        return container.getSlot(p_18942_).remove(p_18943_);
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_18951_) {
        if (p_18951_ >= getContainerSize())
            return ItemStack.EMPTY;
        return container.getSlot(p_18951_).remove(Integer.MAX_VALUE);
    }

    @Override
    public void setItem(int p_18944_, ItemStack p_18945_) {
        if (p_18944_ >= getContainerSize())
            return;
        container.getSlot(p_18944_).set(p_18945_);
    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(Player p_18946_) {
        return this.container.stillValid(p_18946_);
    }

    @Override
    public List<ItemStack> getContents() {
        container.broadcastChanges();
        return container.lastSlots.subList(0, getContainerSize());
    }

    @Override
    public void onOpen(CraftHumanEntity who) {
        viewers.add(who);
    }

    @Override
    public void onClose(CraftHumanEntity who) {
        viewers.remove(who);
    }

    @Override
    public List<HumanEntity> getViewers() {
        return viewers;
    }

    @Override
    public InventoryHolder getOwner() {
        return owner;
    }

    @Override
    public void setMaxStackSize(int size) {
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public Recipe getCurrentRecipe() {
        return null;
    }

    @Override
    public void setCurrentRecipe(Recipe recipe) {
    }
}
