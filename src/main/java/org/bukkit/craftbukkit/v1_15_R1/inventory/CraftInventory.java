package org.bukkit.craftbukkit.v1_15_R1.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import net.minecraft.tileentity.IHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.MerchantInventory;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.BarrelTileEntity;
import net.minecraft.tileentity.BlastFurnaceTileEntity;
import net.minecraft.tileentity.BrewingStandTileEntity;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.DropperTileEntity;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.LecternTileEntity;
import net.minecraft.tileentity.ShulkerBoxTileEntity;
import net.minecraft.tileentity.SmokerTileEntity;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftLegacy;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class CraftInventory implements Inventory {
    protected final IInventory inventory;

    public CraftInventory(IInventory inventory) {
        this.inventory = inventory;
    }

    public IInventory getInventory() {
        return inventory;
    }

    @Override
    public int getSize() {
        return getInventory().getSizeInventory();
    }

    @Override
    public ItemStack getItem(int index) {
        net.minecraft.item.ItemStack item = getInventory().getStackInSlot(index);
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
        List<net.minecraft.item.ItemStack> mcItems = getInventory().getContents();

        return asCraftMirror(mcItems);
    }

    @Override
    public void setContents(ItemStack[] items) {
        if (getSize() < items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + getSize() + " or less");
        }

        for (int i = 0; i < getSize(); i++) {
            if (i >= items.length) {
                setItem(i, null);
            } else {
                setItem(i, items[i]);
            }
        }
    }

    @Override
    public void setItem(int index, ItemStack item) {
        getInventory().setInventorySlotContents(index, CraftItemStack.asNMSCopy(item));
    }

    @Override
    public boolean contains(Material material) {
        Validate.notNull(material, "Material cannot be null");
        material = CraftLegacy.fromLegacy(material);
        for (ItemStack item : getStorageContents()) {
            if (item != null && item.getType() == material) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(ItemStack item) {
        if (item == null) {
            return false;
        }
        for (ItemStack i : getStorageContents()) {
            if (item.equals(i)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Material material, int amount) {
        Validate.notNull(material, "Material cannot be null");
        material = CraftLegacy.fromLegacy(material);
        if (amount <= 0) {
            return true;
        }
        for (ItemStack item : getStorageContents()) {
            if (item != null && item.getType()== material) {
                if ((amount -= item.getAmount()) <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean contains(ItemStack item, int amount) {
        if (item == null) {
            return false;
        }
        if (amount <= 0) {
            return true;
        }
        for (ItemStack i : getStorageContents()) {
            if (item.equals(i) && --amount <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAtLeast(ItemStack item, int amount) {
        if (item == null) {
            return false;
        }
        if (amount <= 0) {
            return true;
        }
        for (ItemStack i : getStorageContents()) {
            if (item.isSimilar(i) && (amount -= i.getAmount()) <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public HashMap<Integer, ItemStack> all(Material material) {
        Validate.notNull(material, "Material cannot be null");
        material = CraftLegacy.fromLegacy(material);
        HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();

        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.getType()== material) {
                slots.put(i, item);
            }
        }
        return slots;
    }

    @Override
    public HashMap<Integer, ItemStack> all(ItemStack item) {
        HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();
        if (item != null) {
            ItemStack[] inventory = getStorageContents();
            for (int i = 0; i < inventory.length; i++) {
                if (item.equals(inventory[i])) {
                    slots.put(i, inventory[i]);
                }
            }
        }
        return slots;
    }

    @Override
    public int first(Material material) {
        Validate.notNull(material, "Material cannot be null");
        material = CraftLegacy.fromLegacy(material);
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.getType()== material) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int first(ItemStack item) {
        return first(item, true);
    }

    private int first(ItemStack item, boolean withAmount) {
        if (item == null) {
            return -1;
        }
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) continue;

            if (withAmount ? item.equals(inventory[i]) : item.isSimilar(inventory[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int firstEmpty() {
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public int firstPartial(Material material) {
        Validate.notNull(material, "Material cannot be null");
        material = CraftLegacy.fromLegacy(material);
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.getType()== material && item.getAmount() < item.getMaxStackSize()) {
                return i;
            }
        }
        return -1;
    }

    private int firstPartial(ItemStack item) {
        ItemStack[] inventory = getStorageContents();
        ItemStack filteredItem = CraftItemStack.asCraftCopy(item);
        if (item == null) {
            return -1;
        }
        for (int i = 0; i < inventory.length; i++) {
            ItemStack cItem = inventory[i];
            if (cItem != null && cItem.getAmount() < cItem.getMaxStackSize() && cItem.isSimilar(filteredItem)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public HashMap<Integer, ItemStack> addItem(ItemStack... items) {
        Validate.noNullElements(items, "Item cannot be null");
        HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();

        /* TODO: some optimization
         *  - Create a 'firstPartial' with a 'fromIndex'
         *  - Record the lastPartial per Material
         *  - Cache firstEmpty result
         */

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            while (true) {
                // Do we already have a stack of it?
                int firstPartial = firstPartial(item);

                // Drat! no partial stack
                if (firstPartial == -1) {
                    // Find a free spot!
                    int firstFree = firstEmpty();

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
        Validate.notNull(items, "Items cannot be null");
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
                if (toDelete <= 0) {
                    break;
                }
            }
        }
        return leftover;
    }

    private int getMaxItemStack() {
        return getInventory().getInventoryStackLimit();
    }

    @Override
    public void remove(Material material) {
        Validate.notNull(material, "Material cannot be null");
        material = CraftLegacy.fromLegacy(material);
        ItemStack[] items = getStorageContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].getType()== material) {
                clear(i);
            }
        }
    }

    @Override
    public void remove(ItemStack item) {
        ItemStack[] items = getStorageContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].equals(item)) {
                clear(i);
            }
        }
    }

    @Override
    public void clear(int index) {
        setItem(index, null);
    }

    @Override
    public void clear() {
        for (int i = 0; i < getSize(); i++) {
            clear(i);
        }
    }

    @Override
    public ListIterator<ItemStack> iterator() {
        return new InventoryIterator(this);
    }

    @Override
    public ListIterator<ItemStack> iterator(int index) {
        if (index < 0) {
            index += getSize() + 1; // ie, with -1, previous() will return the last element
        }
        return new InventoryIterator(this, index);
    }

    @Override
    public List<HumanEntity> getViewers() {
        return this.inventory.getViewers();
    }

    @Override
    public InventoryType getType() {
        // Thanks to Droppers extending Dispensers, Blast Furnaces & Smokers extending Furnace, order is important.
        if (inventory instanceof CraftingInventory) {
            return inventory.getSizeInventory() >= 9 ? InventoryType.WORKBENCH : InventoryType.CRAFTING;
        } else if (inventory instanceof PlayerInventory) {
            return InventoryType.PLAYER;
        } else if (inventory instanceof DropperTileEntity) {
            return InventoryType.DROPPER;
        } else if (inventory instanceof DispenserTileEntity) {
            return InventoryType.DISPENSER;
        } else if (inventory instanceof BlastFurnaceTileEntity) {
            return InventoryType.BLAST_FURNACE;
        } else if (inventory instanceof SmokerTileEntity) {
            return InventoryType.SMOKER;
        } else if (inventory instanceof AbstractFurnaceTileEntity) {
            return InventoryType.FURNACE;
        } else if (this instanceof CraftInventoryEnchanting) {
           return InventoryType.ENCHANTING;
        } else if (inventory instanceof BrewingStandTileEntity) {
            return InventoryType.BREWING;
        } else if (inventory instanceof CraftInventoryCustom.MinecraftInventory) {
            return ((CraftInventoryCustom.MinecraftInventory) inventory).getType();
        } else if (inventory instanceof EnderChestInventory) {
            return InventoryType.ENDER_CHEST;
        } else if (inventory instanceof MerchantInventory) {
            return InventoryType.MERCHANT;
        } else if (this instanceof CraftInventoryBeacon) {
            return InventoryType.BEACON;
        } else if (this instanceof CraftInventoryAnvil) {
           return InventoryType.ANVIL;
        } else if (inventory instanceof IHopper) {
            return InventoryType.HOPPER;
        } else if (inventory instanceof ShulkerBoxTileEntity) {
            return InventoryType.SHULKER_BOX;
        } else if (inventory instanceof BarrelTileEntity) {
            return InventoryType.BARREL;
        } else if (inventory instanceof LecternTileEntity.LecternInventory) {
            return InventoryType.LECTERN;
        } else if (this instanceof CraftInventoryLoom) {
            return InventoryType.LOOM;
        } else if (this instanceof CraftInventoryCartography) {
            return InventoryType.CARTOGRAPHY;
        } else if (this instanceof CraftInventoryGrindstone) {
            return InventoryType.GRINDSTONE;
        } else if (this instanceof CraftInventoryStonecutter) {
            return InventoryType.STONECUTTER;
        } else {
            return InventoryType.CHEST;
        }
    }

    @Override
    public InventoryHolder getHolder() {
        return inventory.getOwner();
    }

    @Override
    public int getMaxStackSize() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public void setMaxStackSize(int size) {
        inventory.setMaxStackSize(size);
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
        return inventory.getLocation();
    }
}
