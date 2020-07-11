package org.bukkit.inventory;

import org.bukkit.entity.HumanEntity;

/**
 * Interface to the inventory of a Player, including the four armor slots and any extra slots.
 */
public interface PlayerInventory extends Inventory {

    /**
     * Get all ItemStacks from the armor slots
     *
     * @return All the ItemStacks from the armor slots
     */
    ItemStack[] getArmorContents();

    /**
     * Put the given ItemStacks into the armor slots
     *
     * @param items The ItemStacks to use as armour
     */
    void setArmorContents(ItemStack[] items);

    /**
     * Get all additional ItemStacks stored in this inventory.
     * <br>
     * NB: What defines an extra slot is up to the implementation, however it
     * will not be contained within {@link #getStorageContents()} or
     * {@link #getArmorContents()}
     *
     * @return All additional ItemStacks
     */
    ItemStack[] getExtraContents();

    /**
     * Put the given ItemStacks into the extra slots
     * <br>
     * See {@link #getExtraContents()} for an explanation of extra slots.
     *
     * @param items The ItemStacks to use as extra
     */
    void setExtraContents(ItemStack[] items);

    /**
     * Return the ItemStack from the helmet slot
     *
     * @return The ItemStack in the helmet slot
     */
    ItemStack getHelmet();

    /**
     * Put the given ItemStack into the helmet slot. This does not check if
     * the ItemStack is a helmet
     *
     * @param helmet The ItemStack to use as helmet
     */
    void setHelmet(ItemStack helmet);

    /**
     * Return the ItemStack from the chestplate slot
     *
     * @return The ItemStack in the chestplate slot
     */
    ItemStack getChestplate();

    /**
     * Put the given ItemStack into the chestplate slot. This does not check
     * if the ItemStack is a chestplate
     *
     * @param chestplate The ItemStack to use as chestplate
     */
    void setChestplate(ItemStack chestplate);

    /**
     * Return the ItemStack from the leg slot
     *
     * @return The ItemStack in the leg slot
     */
    ItemStack getLeggings();

    /**
     * Put the given ItemStack into the leg slot. This does not check if the
     * ItemStack is a pair of leggings
     *
     * @param leggings The ItemStack to use as leggings
     */
    void setLeggings(ItemStack leggings);

    /**
     * Return the ItemStack from the boots slot
     *
     * @return The ItemStack in the boots slot
     */
    ItemStack getBoots();

    /**
     * Put the given ItemStack into the boots slot. This does not check if the
     * ItemStack is a boots
     *
     * @param boots The ItemStack to use as boots
     */
    void setBoots(ItemStack boots);

    /**
     * Stores the ItemStack at the given index of the inventory.
     * <p>
     * Indexes 0 through 8 refer to the hotbar. 9 through 35 refer to the main inventory, counting up from 9 at the top
     * left corner of the inventory, moving to the right, and moving to the row below it back on the left side when it
     * reaches the end of the row. It follows the same path in the inventory like you would read a book.
     * <p>
     * Indexes 36 through 39 refer to the armor slots. Though you can set armor with this method using these indexes,
     * you are encouraged to use the provided methods for those slots.
     * <p>
     * If you attempt to use this method with an index less than 0 or greater than 39, an ArrayIndexOutOfBounds
     * exception will be thrown.
     *
     * @param index The index where to put the ItemStack
     * @param item  The ItemStack to set
     * @throws ArrayIndexOutOfBoundsException when index &lt; 0 || index &gt; 39
     * @see #setBoots(ItemStack)
     * @see #setChestplate(ItemStack)
     * @see #setHelmet(ItemStack)
     * @see #setLeggings(ItemStack)
     */
    @Override
    void setItem(int index, ItemStack item);

    /**
     * Gets a copy of the item the player is currently holding
     * in their main hand.
     *
     * @return the currently held item
     */
    ItemStack getItemInMainHand();

    /**
     * Sets the item the player is holding in their main hand.
     *
     * @param item The item to put into the player's hand
     */
    void setItemInMainHand(ItemStack item);

    /**
     * Gets a copy of the item the player is currently holding
     * in their off hand.
     *
     * @return the currently held item
     */
    ItemStack getItemInOffHand();

    /**
     * Sets the item the player is holding in their off hand.
     *
     * @param item The item to put into the player's hand
     */
    void setItemInOffHand(ItemStack item);

    /**
     * Gets a copy of the item the player is currently holding
     *
     * @return the currently held item
     * @see #getItemInMainHand()
     * @see #getItemInOffHand()
     * @deprecated players can duel wield now use the methods for the
     * specific hand instead
     */
    @Deprecated
    ItemStack getItemInHand();

    /**
     * Sets the item the player is holding
     *
     * @param stack The item to put into the player's hand
     * @see #setItemInMainHand(ItemStack)
     * @see #setItemInOffHand(ItemStack)
     * @deprecated players can duel wield now use the methods for the
     * specific hand instead
     */
    @Deprecated
    void setItemInHand(ItemStack stack);

    /**
     * Get the slot number of the currently held item
     *
     * @return Held item slot number
     */
    int getHeldItemSlot();

    /**
     * Set the slot number of the currently held item.
     * <p>
     * This validates whether the slot is between 0 and 8 inclusive.
     *
     * @param slot The new slot number
     * @throws IllegalArgumentException Thrown if slot is not between 0 and 8
     *                                  inclusive
     */
    void setHeldItemSlot(int slot);

    /**
     * Clears all matching items from the inventory. Setting either value to
     * -1 will skip it's check, while setting both to -1 will clear all items
     * in your inventory unconditionally.
     *
     * @param id   the id of the item you want to clear from the inventory
     * @param data the data of the item you want to clear from the inventory
     * @return The number of items cleared
     * @deprecated Magic value
     */
    @Deprecated
    int clear(int id, int data);

    HumanEntity getHolder();
}
