package org.bukkit.craftbukkit.v1_16_R1.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import net.minecraft.block.entity.Hopper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.village.TraderInventory;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.DropperBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.block.entity.SmokerBlockEntity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R1.util.CraftLegacy;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import red.mohist.extra.inventory.ExtraInventory;

public class CraftInventory implements Inventory {

    protected final net.minecraft.inventory.Inventory inventory;

    public CraftInventory(net.minecraft.inventory.Inventory inventory) {
        this.inventory = inventory;
    }

    public net.minecraft.inventory.Inventory getInventory() {
        return inventory;
    }

    @Override
    public int getSize() {
        return getInventory().size();
    }

    @Override
    public ItemStack getItem(int index) {
        net.minecraft.item.ItemStack item = getInventory().getStack(index);
        return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
    }

    protected ItemStack[] asCraftMirror(List<net.minecraft.item.ItemStack> mcItems) {
        int size = mcItems.size();
        ItemStack[] items = new ItemStack[size];

        for (int i = 0; i < size; i++) {
            net.minecraft.item.ItemStack mcItem = mcItems.get(i);
            items[i] = (mcItem.isEmpty()) ? null : CraftItemStack.asCraftMirror(mcItem);
        }

        return items;
    }

    @Override
    public ItemStack[] getStorageContents() {
        return getContents();
    }

    @Override
    public void setStorageContents(ItemStack[] items) throws IllegalArgumentException {
        setContents(items);
    }

    @Override
    public ItemStack[] getContents() {
        return asCraftMirror( ((ExtraInventory)getInventory()).getContents() );
    }

    @Override
    public void setContents(ItemStack[] items) {
        if (getSize() < items.length)
            throw new IllegalArgumentException("Invalid inventory size: expected " + getSize() + " or less");

        for (int i = 0; i < getSize(); i++)
            setItem(i, i >= items.length ? null : items[i]);
    }

    @Override
    public void setItem(int index, ItemStack item) {
        getInventory().setStack(index, CraftItemStack.asNMSCopy(item));
    }

    @Override
    public boolean contains(Material material) {
        material = CraftLegacy.fromLegacy(material);
        for (ItemStack item : getStorageContents())
            if (item != null && item.getType() == material)
                return true;

        return false;
    }

    @Override
    public boolean contains(ItemStack item) {
        if (item == null)
            return false;

        for (ItemStack i : getStorageContents())
            if (item.equals(i))
                return true;

        return false;
    }

    @Override
    public boolean contains(Material material, int amount) {
        material = CraftLegacy.fromLegacy(material);
        if (amount <= 0)
            return true;

        for (ItemStack item : getStorageContents())
            if (item != null && item.getType() == material)
                if ((amount -= item.getAmount()) <= 0)
                    return true;

        return false;
    }

    @Override
    public boolean contains(ItemStack item, int amount) {
        if (item == null)
            return false;

        if (amount <= 0)
            return true;

        for (ItemStack i : getStorageContents())
            if (item.equals(i) && --amount <= 0)
                return true;

        return false;
    }

    @Override
    public boolean containsAtLeast(ItemStack item, int amount) {
        if (item == null)
            return false;

        if (amount <= 0)
            return true;

        for (ItemStack i : getStorageContents())
            if (item.isSimilar(i) && (amount -= i.getAmount()) <= 0)
                return true;

        return false;
    }

    @Override
    public HashMap<Integer, ItemStack> all(Material material) {
        material = CraftLegacy.fromLegacy(material);
        HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();

        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.getType() == material)
                slots.put(i, item);
        }
        return slots;
    }

    @Override
    public HashMap<Integer, ItemStack> all(ItemStack item) {
        HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();
        if (item != null) {
            ItemStack[] inventory = getStorageContents();
            for (int i = 0; i < inventory.length; i++) {
                if (item.equals(inventory[i]))
                    slots.put(i, inventory[i]);
            }
        }
        return slots;
    }

    @Override
    public int first(Material material) {
        material = CraftLegacy.fromLegacy(material);
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.getType() == material)
                return i;
        }
        return -1;
    }

    @Override
    public int first(ItemStack item) {
        return first(item, true);
    }

    private int first(ItemStack item, boolean withAmount) {
        if (item == null)
            return -1;
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) continue;

            if (withAmount ? item.equals(inventory[i]) : item.isSimilar(inventory[i]))
                return i;
        }
        return -1;
    }

    @Override
    public int firstEmpty() {
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++)
            if (inventory[i] == null)
                return i;

        return -1;
    }

    public int firstPartial(Material material) {
        material = CraftLegacy.fromLegacy(material);
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.getType() == material && item.getAmount() < item.getMaxStackSize())
                return i;
        }
        return -1;
    }

    private int firstPartial(ItemStack item) {
        ItemStack[] inventory = getStorageContents();
        ItemStack filteredItem = CraftItemStack.asCraftCopy(item);
        if (item == null)
            return -1;

        for (int i = 0; i < inventory.length; i++) {
            ItemStack cItem = inventory[i];
            if (cItem != null && cItem.getAmount() < cItem.getMaxStackSize() && cItem.isSimilar(filteredItem))
                return i;
        }
        return -1;
    }

    @Override
    public HashMap<Integer, ItemStack> addItem(ItemStack... items) {
        HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            while (true) {
                // Do we already have a stack of it?
                int firstPartial = firstPartial(item);

                // Drat! no partial stack
                if (firstPartial == -1) {
                    int firstFree = firstEmpty(); // Find a free spot!

                    if (firstFree == -1) {
                        // No space at all!
                        leftover.put(i, item);
                        break;
                    } else {
                        // More than a single stack!
                        if (item.getAmount() > getMaxItemStack()) {
                            CraftItemStack stack = CraftItemStack.asCraftCopy(item);
                            stack.setAmount(getMaxItemStack());
                            setItem(firstFree, stack);
                            item.setAmount(item.getAmount() - getMaxItemStack());
                        } else {
                            // Just store it
                            setItem(firstFree, item);
                            break;
                        }
                    }
                } else {
                    // So, apparently it might only partially fit, well lets do just that
                    ItemStack partialItem = getItem(firstPartial);

                    int amount = item.getAmount();
                    int partialAmount = partialItem.getAmount();
                    int maxAmount = partialItem.getMaxStackSize();

                    // Check if it fully fits
                    if (amount + partialAmount <= maxAmount) {
                        partialItem.setAmount(amount + partialAmount);
                        // To make sure the packet is sent to the client
                        setItem(firstPartial, partialItem);
                        break;
                    }

                    // It fits partially
                    partialItem.setAmount(maxAmount);
                    // To make sure the packet is sent to the client
                    setItem(firstPartial, partialItem);
                    item.setAmount(amount + partialAmount - maxAmount);
                }
            }
        }
        return leftover;
    }

    @Override
    public HashMap<Integer, ItemStack> removeItem(ItemStack... items) {
        HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();

        // TODO: optimization

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            int toDelete = item.getAmount();

            while (true) {
                int first = first(item, false);

                // Drat! we don't have this type in the inventory
                if (first == -1) {
                    item.setAmount(toDelete);
                    leftover.put(i, item);
                    break;
                } else {
                    ItemStack itemStack = getItem(first);
                    int amount = itemStack.getAmount();

                    if (amount <= toDelete) {
                        toDelete -= amount;
                        // clear the slot, all used up
                        clear(first);
                    } else {
                        // split the stack and store
                        itemStack.setAmount(amount - toDelete);
                        setItem(first, itemStack);
                        toDelete = 0;
                    }
                }

                // Bail when done
                if (toDelete <= 0)
                    break;
            }
        }
        return leftover;
    }

    private int getMaxItemStack() {
        return getInventory().getMaxCountPerStack();
    }

    @Override
    public void remove(Material material) {
        material = CraftLegacy.fromLegacy(material);
        ItemStack[] items = getStorageContents();
        for (int i = 0; i < items.length; i++)
            if (items[i] != null && items[i].getType() == material)
                clear(i);
    }

    @Override
    public void remove(ItemStack item) {
        ItemStack[] items = getStorageContents();
        for (int i = 0; i < items.length; i++)
            if (items[i] != null && items[i].equals(item))
                clear(i);
    }

    @Override
    public void clear(int index) {
        setItem(index, null);
    }

    @Override
    public void clear() {
        for (int i = 0; i < getSize(); i++)
            clear(i);
    }

    @Override
    public ListIterator<ItemStack> iterator() {
        return new InventoryIterator(this);
    }

    @Override
    public ListIterator<ItemStack> iterator(int index) {
        if (index < 0)
            index += getSize() + 1; // ie, with -1, previous() will return the last element
        return new InventoryIterator(this, index);
    }

    @Override
    public List<HumanEntity> getViewers() {
        return ((ExtraInventory)this.inventory).getViewers();
    }

    @Override
    public InventoryType getType() {
        // Order is important.
        if (inventory instanceof CraftingInventory) {
            return inventory.size() >= 9 ? InventoryType.WORKBENCH : InventoryType.CRAFTING;
        } else if (inventory instanceof PlayerInventory) {
            return InventoryType.PLAYER;
        } else if (inventory instanceof DropperBlockEntity) {
            return InventoryType.DROPPER;
        } else if (inventory instanceof DispenserBlockEntity) {
            return InventoryType.DISPENSER;
        } else if (inventory instanceof BlastFurnaceBlockEntity) {
            return InventoryType.BLAST_FURNACE;
        } else if (inventory instanceof SmokerBlockEntity) {
            return InventoryType.SMOKER;
        } else if (inventory instanceof FurnaceBlockEntity) {
            return InventoryType.FURNACE;
         // TODO } else if (this instanceof CraftInventoryEnchanting) {
         // TODO    return InventoryType.ENCHANTING;
        } else if (inventory instanceof BrewingStandBlockEntity) {
            return InventoryType.BREWING;
         // TODO } else if (inventory instanceof CraftInventoryCustom.MinecraftInventory) {
         // TODO     return ((CraftInventoryCustom.MinecraftInventory) inventory).getType();
        } else if (inventory instanceof EnderChestInventory) {
            return InventoryType.ENDER_CHEST;
        } else if (inventory instanceof TraderInventory) {
            return InventoryType.MERCHANT;
         // TODO } else if (this instanceof CraftInventoryBeacon) {
         // TODO     return InventoryType.BEACON;
         // TODO } else if (this instanceof CraftInventoryAnvil) {
         // TODO     return InventoryType.ANVIL;
        } else if (inventory instanceof Hopper) {
            return InventoryType.HOPPER;
        } else if (inventory instanceof ShulkerBoxBlockEntity) {
            return InventoryType.SHULKER_BOX;
        } else if (inventory instanceof BarrelBlockEntity) {
            return InventoryType.BARREL;
        } else if (inventory instanceof LecternBlockEntity) { // todo
            return InventoryType.LECTERN;
         // TODO } else if (this instanceof CraftInventoryLoom) {
         // TODO     return InventoryType.LOOM;
         // TODO } else if (this instanceof CraftInventoryCartography) {
         // TODO     return InventoryType.CARTOGRAPHY;
         // TODO  } else if (this instanceof CraftInventoryGrindstone) {
         // TODO     return InventoryType.GRINDSTONE;
         // TODO } else if (this instanceof CraftInventoryStonecutter) {
         // TODO     return InventoryType.STONECUTTER;
        } else {
            return InventoryType.CHEST;
        }
    }

    @Override
    public InventoryHolder getHolder() {
        return ((ExtraInventory)inventory).getOwner();
    }

    @Override
    public int getMaxStackSize() {
        return ((ExtraInventory)inventory).getMaxStackSize();
    }

    @Override
    public void setMaxStackSize(int size) {
        ((ExtraInventory)inventory).setMaxStackSize(size);
    }

    @Override
    public int hashCode() {
        return inventory.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof CraftInventory && ((CraftInventory) obj).inventory.equals(this.inventory);
    }

    @Override
    public Location getLocation() {
        return ((ExtraInventory)inventory).getLocation();
    }

}