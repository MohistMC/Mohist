package org.bukkit.inventory;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * Interface to the various inventories. Behavior relating to {@link
 * Material#AIR} is unspecified.
 */
public interface Inventory extends Iterable<ItemStack> {

    /**
     * Returns the size of the inventory
     *
     * @return The size of the inventory
     */
    int getSize();

    /**
     * Returns the maximum stack size for an ItemStack in this inventory.
     *
     * @return The maximum size for an ItemStack in this inventory.
     */
    int getMaxStackSize();

    /**
     * This method allows you to change the maximum stack size for an
     * inventory.
     * <p>
     * <b>Caveats:</b>
     * <ul>
     * <li>Not all inventories respect this value.
     * <li>Stacks larger than 127 may be clipped when the world is saved.
     * <li>This value is not guaranteed to be preserved; be sure to set it
     *     before every time you want to set a slot over the max stack size.
     * <li>Stacks larger than the default max size for this type of inventory
     *     may not display correctly in the client.
     * </ul>
     *
     * @param size The new maximum stack size for items in this inventory.
     */
    void setMaxStackSize(int size);

    /**
     * Returns the name of the inventory
     *
     * @return The String with the name of the inventory
     */
    String getName();

    /**
     * Returns the ItemStack found in the slot at the given index
     *
     * @param index The index of the Slot's ItemStack to return
     * @return The ItemStack in the slot
     */
    ItemStack getItem(int index);

    /**
     * Stores the ItemStack at the given index of the inventory.
     *
     * @param index The index where to put the ItemStack
     * @param item  The ItemStack to set
     */
    void setItem(int index, ItemStack item);

    /**
     * Stores the given ItemStacks in the inventory. This will try to fill
     * existing stacks and empty slots as well as it can.
     * <p>
     * The returned HashMap contains what it couldn't store, where the key is
     * the index of the parameter, and the value is the ItemStack at that
     * index of the varargs parameter. If all items are stored, it will return
     * an empty HashMap.
     * <p>
     * If you pass in ItemStacks which exceed the maximum stack size for the
     * Material, first they will be added to partial stacks where
     * Material.getMaxStackSize() is not exceeded, up to
     * Material.getMaxStackSize(). When there are no partial stacks left
     * stacks will be split on Inventory.getMaxStackSize() allowing you to
     * exceed the maximum stack size for that material.
     * <p>
     * It is known that in some implementations this method will also set
     * the inputted argument amount to the number of that item not placed in
     * slots.
     *
     * @param items The ItemStacks to add
     * @return A HashMap containing items that didn't fit.
     * @throws IllegalArgumentException if items or any element in it is null
     */
    HashMap<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException;

    /**
     * Removes the given ItemStacks from the inventory.
     * <p>
     * It will try to remove 'as much as possible' from the types and amounts
     * you give as arguments.
     * <p>
     * The returned HashMap contains what it couldn't remove, where the key is
     * the index of the parameter, and the value is the ItemStack at that
     * index of the varargs parameter. If all the given ItemStacks are
     * removed, it will return an empty HashMap.
     * <p>
     * It is known that in some implementations this method will also set the
     * inputted argument amount to the number of that item not removed from
     * slots.
     *
     * @param items The ItemStacks to remove
     * @return A HashMap containing items that couldn't be removed.
     * @throws IllegalArgumentException if items is null
     */
    HashMap<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException;

    /**
     * Returns all ItemStacks from the inventory
     *
     * @return An array of ItemStacks from the inventory.
     */
    ItemStack[] getContents();

    /**
     * Completely replaces the inventory's contents. Removes all existing
     * contents and replaces it with the ItemStacks given in the array.
     *
     * @param items A complete replacement for the contents; the length must
     *              be less than or equal to {@link #getSize()}.
     * @throws IllegalArgumentException If the array has more items than the
     *                                  inventory.
     */
    void setContents(ItemStack[] items) throws IllegalArgumentException;

    /**
     * Return the contents from the section of the inventory where items can
     * reasonably be expected to be stored. In most cases this will represent
     * the entire inventory, but in some cases it may exclude armor or result
     * slots.
     * <br>
     * It is these contents which will be used for add / contains / remove
     * methods which look for a specific stack.
     *
     * @return inventory storage contents
     */
    ItemStack[] getStorageContents();

    /**
     * Put the given ItemStacks into the storage slots
     *
     * @param items The ItemStacks to use as storage contents
     * @throws IllegalArgumentException If the array has more items than the
     *                                  inventory.
     */
    void setStorageContents(ItemStack[] items) throws IllegalArgumentException;

    /**
     * Checks if the inventory contains any ItemStacks with the given
     * materialId
     *
     * @param materialId The materialId to check for
     * @return true if an ItemStack in this inventory contains the materialId
     * @deprecated Magic value
     */
    @Deprecated
    boolean contains(int materialId);

    /**
     * Checks if the inventory contains any ItemStacks with the given
     * material.
     *
     * @param material The material to check for
     * @return true if an ItemStack is found with the given Material
     * @throws IllegalArgumentException if material is null
     */
    boolean contains(Material material) throws IllegalArgumentException;

    /**
     * Checks if the inventory contains any ItemStacks matching the given
     * ItemStack.
     * <p>
     * This will only return true if both the type and the amount of the stack
     * match.
     *
     * @param item The ItemStack to match against
     * @return false if item is null, true if any exactly matching ItemStacks
     * were found
     */
    boolean contains(ItemStack item);

    /**
     * Checks if the inventory contains any ItemStacks with the given
     * materialId, adding to at least the minimum amount specified.
     *
     * @param materialId The materialId to check for
     * @param amount     The minimum amount to look for
     * @return true if this contains any matching ItemStack with the given
     * materialId and amount
     * @deprecated Magic value
     */
    @Deprecated
    boolean contains(int materialId, int amount);

    /**
     * Checks if the inventory contains any ItemStacks with the given
     * material, adding to at least the minimum amount specified.
     *
     * @param material The material to check for
     * @param amount   The minimum amount
     * @return true if amount is less than 1, true if enough ItemStacks were
     * found to add to the given amount
     * @throws IllegalArgumentException if material is null
     */
    boolean contains(Material material, int amount) throws IllegalArgumentException;

    /**
     * Checks if the inventory contains at least the minimum amount specified
     * of exactly matching ItemStacks.
     * <p>
     * An ItemStack only counts if both the type and the amount of the stack
     * match.
     *
     * @param item   the ItemStack to match against
     * @param amount how many identical stacks to check for
     * @return false if item is null, true if amount less than 1, true if
     * amount of exactly matching ItemStacks were found
     * @see #containsAtLeast(ItemStack, int)
     */
    boolean contains(ItemStack item, int amount);

    /**
     * Checks if the inventory contains ItemStacks matching the given
     * ItemStack whose amounts sum to at least the minimum amount specified.
     *
     * @param item   the ItemStack to match against
     * @param amount the minimum amount
     * @return false if item is null, true if amount less than 1, true if
     * enough ItemStacks were found to add to the given amount
     */
    boolean containsAtLeast(ItemStack item, int amount);

    /**
     * Returns a HashMap with all slots and ItemStacks in the inventory with
     * given materialId.
     * <p>
     * The HashMap contains entries where, the key is the slot index, and the
     * value is the ItemStack in that slot. If no matching ItemStack with the
     * given materialId is found, an empty map is returned.
     *
     * @param materialId The materialId to look for
     * @return A HashMap containing the slot index, ItemStack pairs
     * @deprecated Magic value
     */
    @Deprecated
    HashMap<Integer, ? extends ItemStack> all(int materialId);

    /**
     * Returns a HashMap with all slots and ItemStacks in the inventory with
     * the given Material.
     * <p>
     * The HashMap contains entries where, the key is the slot index, and the
     * value is the ItemStack in that slot. If no matching ItemStack with the
     * given Material is found, an empty map is returned.
     *
     * @param material The material to look for
     * @return A HashMap containing the slot index, ItemStack pairs
     * @throws IllegalArgumentException if material is null
     */
    HashMap<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException;

    /**
     * Finds all slots in the inventory containing any ItemStacks with the
     * given ItemStack. This will only match slots if both the type and the
     * amount of the stack match
     * <p>
     * The HashMap contains entries where, the key is the slot index, and the
     * value is the ItemStack in that slot. If no matching ItemStack with the
     * given Material is found, an empty map is returned.
     *
     * @param item The ItemStack to match against
     * @return A map from slot indexes to item at index
     */
    HashMap<Integer, ? extends ItemStack> all(ItemStack item);

    /**
     * Finds the first slot in the inventory containing an ItemStack with the
     * given materialId.
     *
     * @param materialId The materialId to look for
     * @return The slot index of the given materialId or -1 if not found
     * @deprecated Magic value
     */
    @Deprecated
    int first(int materialId);

    /**
     * Finds the first slot in the inventory containing an ItemStack with the
     * given material
     *
     * @param material The material to look for
     * @return The slot index of the given Material or -1 if not found
     * @throws IllegalArgumentException if material is null
     */
    int first(Material material) throws IllegalArgumentException;

    /**
     * Returns the first slot in the inventory containing an ItemStack with
     * the given stack. This will only match a slot if both the type and the
     * amount of the stack match
     *
     * @param item The ItemStack to match against
     * @return The slot index of the given ItemStack or -1 if not found
     */
    int first(ItemStack item);

    /**
     * Returns the first empty Slot.
     *
     * @return The first empty Slot found, or -1 if no empty slots.
     */
    int firstEmpty();

    /**
     * Removes all stacks in the inventory matching the given materialId.
     *
     * @param materialId The material to remove
     * @deprecated Magic value
     */
    @Deprecated
    void remove(int materialId);

    /**
     * Removes all stacks in the inventory matching the given material.
     *
     * @param material The material to remove
     * @throws IllegalArgumentException if material is null
     */
    void remove(Material material) throws IllegalArgumentException;

    /**
     * Removes all stacks in the inventory matching the given stack.
     * <p>
     * This will only match a slot if both the type and the amount of the
     * stack match
     *
     * @param item The ItemStack to match against
     */
    void remove(ItemStack item);

    /**
     * Clears out a particular slot in the index.
     *
     * @param index The index to empty.
     */
    void clear(int index);

    /**
     * Clears out the whole Inventory.
     */
    void clear();

    /**
     * Gets a list of players viewing the inventory. Note that a player is
     * considered to be viewing their own inventory and internal crafting
     * screen even when said inventory is not open. They will normally be
     * considered to be viewing their inventory even when they have a
     * different inventory screen open, but it's possible for customized
     * inventory screens to exclude the viewer's inventory, so this should
     * never be assumed to be non-empty.
     *
     * @return A list of HumanEntities who are viewing this Inventory.
     */
    List<HumanEntity> getViewers();

    /**
     * Returns the title of this inventory.
     *
     * @return A String with the title.
     */
    String getTitle();

    /**
     * Returns what type of inventory this is.
     *
     * @return The InventoryType representing the type of inventory.
     */
    InventoryType getType();

    /**
     * Gets the block or entity belonging to the open inventory
     *
     * @return The holder of the inventory; null if it has no holder.
     */
    InventoryHolder getHolder();

    @Override
    ListIterator<ItemStack> iterator();

    /**
     * Returns an iterator starting at the given index. If the index is
     * positive, then the first call to next() will return the item at that
     * index; if it is negative, the first call to previous will return the
     * item at index (getSize() + index).
     *
     * @param index The index.
     * @return An iterator.
     */
    ListIterator<ItemStack> iterator(int index);

    /**
     * Get the location of the block or entity which corresponds to this inventory. May return null if this container
     * was custom created or is a virtual / subcontainer.
     *
     * @return location or null if not applicable.
     */
    Location getLocation();
}
