package org.bukkit.craftbukkit.v1_20_R1.inventory;

import com.google.common.base.Preconditions;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CraftInventoryCustom extends CraftInventory {
    public CraftInventoryCustom(InventoryHolder owner, InventoryType type) {
        super(new MinecraftInventory(owner, type));
    }

    public CraftInventoryCustom(InventoryHolder owner, InventoryType type, String title) {
        super(new MinecraftInventory(owner, type, title));
    }

    public CraftInventoryCustom(InventoryHolder owner, int size) {
        super(new MinecraftInventory(owner, size));
    }

    // Paper start
    public CraftInventoryCustom(InventoryHolder owner, int size, net.kyori.adventure.text.Component title) {
        super(new MinecraftInventory(owner, size, title));
    }
    // Paper end

    public CraftInventoryCustom(InventoryHolder owner, int size, String title) {
        super(new MinecraftInventory(owner, size, title));
    }

    public CraftInventoryCustom(InventoryHolder owner, NonNullList<ItemStack> items) {
        super(new MinecraftInventory(owner, items));
    }

    // Paper start
    public String getTitle() {
        return ((MinecraftInventory) inventory).getTitle(); // Mohist - We don't have Paper's custom holder here
    }

    public net.kyori.adventure.text.Component title() {
        return ((MinecraftInventory) inventory).title(); // Mohist - We don't have Paper's custom holder here
    }
    // Paper end

    static class MinecraftInventory implements Container {
        private final NonNullList<ItemStack> items;
        private int maxStack = MAX_STACK;
        private final List<HumanEntity> viewers;
        private final String title;
        private final net.kyori.adventure.text.Component adventure$title; // Paper
        private InventoryType type;
        private final InventoryHolder owner;

        public MinecraftInventory(InventoryHolder owner, InventoryType type) {
            this(owner, type.getDefaultSize(), type.getDefaultTitle());
            this.type = type;
        }

        public MinecraftInventory(InventoryHolder owner, InventoryType type, String title) {
            this(owner, type.getDefaultSize(), title);
            this.type = type;
        }

        public MinecraftInventory(InventoryHolder owner, int size) {
            this(owner, size, "Chest");
        }

        public MinecraftInventory(InventoryHolder owner, int size, String title) {
            Preconditions.checkArgument(title != null, "title cannot be null");
            this.items = NonNullList.withSize(size, ItemStack.EMPTY);
            this.title = title;
            this.adventure$title = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(title);
            this.viewers = new ArrayList<HumanEntity>();
            this.owner = owner;
            this.type = InventoryType.CHEST;
        }

        public MinecraftInventory(InventoryHolder owner, NonNullList<ItemStack> items) {
            this.items = items;
            this.title = "Chest";
            this.adventure$title = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(this.title);
            this.viewers = new ArrayList<>();
            this.owner = owner;
            this.type = InventoryType.CHEST;
        }

        // Paper start
        public MinecraftInventory(final InventoryHolder owner, final int size, final net.kyori.adventure.text.Component title) {
            Preconditions.checkArgument(title != null, "Title cannot be null");
            this.items = NonNullList.withSize(size, ItemStack.EMPTY);
            this.title = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(title);
            this.adventure$title = title;
            this.viewers = new ArrayList<HumanEntity>();
            this.owner = owner;
            this.type = InventoryType.CHEST;
        }
        // Paper end

        @Override
        public int getContainerSize() {
            return items.size();
        }

        @Override
        public ItemStack getItem(int i) {
            return items.get(i);
        }

        @Override
        public ItemStack removeItem(int i, int j) {
            ItemStack stack = this.getItem(i);
            ItemStack result;
            if (stack == ItemStack.EMPTY) return stack;
            if (stack.getCount() <= j) {
                this.setItem(i, ItemStack.EMPTY);
                result = stack;
            } else {
                result = CraftItemStack.copyNMSStack(stack, j);
                stack.shrink(j);
            }
            this.setChanged();
            return result;
        }

        @Override
        public ItemStack removeItemNoUpdate(int i) {
            ItemStack stack = this.getItem(i);
            ItemStack result;
            if (stack == ItemStack.EMPTY) return stack;
            if (stack.getCount() <= 1) {
                this.setItem(i, null);
                result = stack;
            } else {
                result = CraftItemStack.copyNMSStack(stack, 1);
                stack.shrink(1);
            }
            return result;
        }

        @Override
        public void setItem(int i, ItemStack itemstack) {
            items.set(i, itemstack);
            if (itemstack != ItemStack.EMPTY && this.getMaxStackSize() > 0 && itemstack.getCount() > this.getMaxStackSize()) {
                itemstack.setCount(this.getMaxStackSize());
            }
        }

        @Override
        public int getMaxStackSize() {
            return maxStack;
        }

        @Override
        public void setMaxStackSize(int size) {
            maxStack = size;
        }

        @Override
        public void setChanged() {}

        @Override
        public boolean stillValid(net.minecraft.world.entity.player.Player entityhuman) {
            return true;
        }

        @Override
        public List<ItemStack> getContents() {
            return items;
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

        public InventoryType getType() {
            return type;
        }

        @Override
        public InventoryHolder getOwner() {
            return owner;
        }

        @Override
        public boolean canPlaceItem(int i, ItemStack itemstack) {
            return true;
        }

        @Override
        public void startOpen(net.minecraft.world.entity.player.Player entityHuman) {

        }

        @Override
        public void stopOpen(net.minecraft.world.entity.player.Player entityHuman) {

        }

        @Override
        public void clearContent() {
            items.clear();
        }

        @Override
        public Location getLocation() {
            return null;
        }

        // Paper start
        public net.kyori.adventure.text.Component title() {
            return this.adventure$title;
        }
        // Paper end

        public String getTitle() {
            return title;
        }

        @Override
        public boolean isEmpty() {
            Iterator iterator = this.items.iterator();

            ItemStack itemstack;

            do {
                if (!iterator.hasNext()) {
                    return true;
                }

                itemstack = (ItemStack) iterator.next();
            } while (itemstack.isEmpty());

            return false;
        }
    }
}
