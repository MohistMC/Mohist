package org.bukkit.entity;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.*;
import org.bukkit.permissions.Permissible;

/**
 * Represents a human entity, such as an NPC or a player
 */
public interface HumanEntity extends LivingEntity, AnimalTamer, Permissible, InventoryHolder {

    /**
     * Returns the name of this player
     *
     * @return Player name
     */
    String getName();

    /**
     * Get the player's inventory.
     *
     * @return The inventory of the player, this also contains the armor
     * slots.
     */
    PlayerInventory getInventory();

    /**
     * Get the player's EnderChest inventory
     *
     * @return The EnderChest of the player
     */
    Inventory getEnderChest();

    /**
     * Gets the player's selected main hand
     *
     * @return the players main hand
     */
    MainHand getMainHand();

    /**
     * If the player currently has an inventory window open, this method will
     * set a property of that window, such as the state of a progress bar.
     *
     * @param prop  The property.
     * @param value The value to set the property to.
     * @return True if the property was successfully set.
     */
    boolean setWindowProperty(InventoryView.Property prop, int value);

    /**
     * Gets the inventory view the player is currently viewing. If they do not
     * have an inventory window open, it returns their internal crafting view.
     *
     * @return The inventory view.
     */
    InventoryView getOpenInventory();

    /**
     * Opens an inventory window with the specified inventory on the top and
     * the player's inventory on the bottom.
     *
     * @param inventory The inventory to open
     * @return The newly opened inventory view
     */
    InventoryView openInventory(Inventory inventory);

    /**
     * Opens an empty workbench inventory window with the player's inventory
     * on the bottom.
     *
     * @param location The location to attach it to. If null, the player's
     *                 location is used.
     * @param force    If false, and there is no workbench block at the location,
     *                 no inventory will be opened and null will be returned.
     * @return The newly opened inventory view, or null if it could not be
     * opened.
     */
    InventoryView openWorkbench(Location location, boolean force);

    /**
     * Opens an empty enchanting inventory window with the player's inventory
     * on the bottom.
     *
     * @param location The location to attach it to. If null, the player's
     *                 location is used.
     * @param force    If false, and there is no enchanting table at the
     *                 location, no inventory will be opened and null will be returned.
     * @return The newly opened inventory view, or null if it could not be
     * opened.
     */
    InventoryView openEnchanting(Location location, boolean force);

    /**
     * Opens an inventory window to the specified inventory view.
     *
     * @param inventory The view to open
     */
    void openInventory(InventoryView inventory);

    /**
     * Starts a trade between the player and the villager.
     * <p>
     * Note that only one player may trade with a villager at once. You must use
     * the force parameter for this.
     *
     * @param trader The merchant to trade with. Cannot be null.
     * @param force  whether to force the trade even if another player is trading
     * @return The newly opened inventory view, or null if it could not be
     * opened.
     */
    InventoryView openMerchant(Villager trader, boolean force);

    /**
     * Starts a trade between the player and the merchant.
     * <p>
     * Note that only one player may trade with a merchant at once. You must use
     * the force parameter for this.
     *
     * @param merchant The merchant to trade with. Cannot be null.
     * @param force    whether to force the trade even if another player is trading
     * @return The newly opened inventory view, or null if it could not be
     * opened.
     */
    InventoryView openMerchant(Merchant merchant, boolean force);

    /**
     * Force-closes the currently open inventory view for this player, if any.
     */
    void closeInventory();

    /**
     * Returns the ItemStack currently in your hand, can be empty.
     *
     * @return The ItemStack of the item you are currently holding.
     * @deprecated Humans may now dual wield in their off hand, use explicit
     * methods in {@link PlayerInventory}.
     */
    @Deprecated
    ItemStack getItemInHand();

    /**
     * Sets the item to the given ItemStack, this will replace whatever the
     * user was holding.
     *
     * @param item The ItemStack which will end up in the hand
     * @deprecated Humans may now dual wield in their off hand, use explicit
     * methods in {@link PlayerInventory}.
     */
    @Deprecated
    void setItemInHand(ItemStack item);

    /**
     * Returns the ItemStack currently on your cursor, can be empty. Will
     * always be empty if the player currently has no open window.
     *
     * @return The ItemStack of the item you are currently moving around.
     */
    ItemStack getItemOnCursor();

    /**
     * Sets the item to the given ItemStack, this will replace whatever the
     * user was moving. Will always be empty if the player currently has no
     * open window.
     *
     * @param item The ItemStack which will end up in the hand
     */
    void setItemOnCursor(ItemStack item);

    /**
     * Check whether a cooldown is active on the specified material.
     *
     * @param material the material to check
     * @return if a cooldown is active on the material
     */
    boolean hasCooldown(Material material);

    /**
     * Get the cooldown time in ticks remaining for the specified material.
     *
     * @param material the material to check
     * @return the remaining cooldown time in ticks
     */
    int getCooldown(Material material);

    /**
     * Set a cooldown on the specified material for a certain amount of ticks.
     * ticks. 0 ticks will result in the removal of the cooldown.
     * <p>
     * Cooldowns are used by the server for items such as ender pearls and
     * shields to prevent them from being used repeatedly.
     * <p>
     * Note that cooldowns will not by themselves stop an item from being used
     * for attacking.
     *
     * @param material the material to set the cooldown for
     * @param ticks    the amount of ticks to set or 0 to remove
     */
    void setCooldown(Material material, int ticks);

    /**
     * Returns whether this player is slumbering.
     *
     * @return slumber state
     */
    boolean isSleeping();

    /**
     * Get the sleep ticks of the player. This value may be capped.
     *
     * @return slumber ticks
     */
    int getSleepTicks();

    /**
     * Gets this human's current {@link GameMode}
     *
     * @return Current game mode
     */
    GameMode getGameMode();

    /**
     * Sets this human's current {@link GameMode}
     *
     * @param mode New game mode
     */
    void setGameMode(GameMode mode);

    /**
     * Check if the player is currently blocking (ie with a shield).
     *
     * @return Whether they are blocking.
     */
    boolean isBlocking();

    /**
     * Check if the player currently has their hand raised (ie about to begin
     * blocking).
     *
     * @return Whether their hand is raised
     */
    boolean isHandRaised();

    /**
     * Get the total amount of experience required for the player to level
     *
     * @return Experience required to level up
     */
    int getExpToLevel();

    /**
     * Gets the entity currently perched on the left shoulder or null if no
     * entity.
     * <br>
     * The returned entity will not be spawned within the world, so most
     * operations are invalid unless the entity is first spawned in.
     *
     * @return left shoulder entity
     * @deprecated There are currently no well defined semantics regarding
     * serialized entities in Bukkit. Use with care.
     */
    @Deprecated
    Entity getShoulderEntityLeft();

    /**
     * Sets the entity currently perched on the left shoulder, or null to
     * remove. This method will remove the entity from the world.
     * <br>
     * Note that only a copy of the entity will be set to display on the
     * shoulder.
     * <br>
     * Also note that the client will currently only render {@link Parrot}
     * entities.
     *
     * @param entity left shoulder entity
     * @deprecated There are currently no well defined semantics regarding
     * serialized entities in Bukkit. Use with care.
     */
    @Deprecated
    void setShoulderEntityLeft(Entity entity);

    /**
     * Gets the entity currently perched on the right shoulder or null if no
     * entity.
     * <br>
     * The returned entity will not be spawned within the world, so most
     * operations are invalid unless the entity is first spawned in.
     *
     * @return right shoulder entity
     * @deprecated There are currently no well defined semantics regarding
     * serialized entities in Bukkit. Use with care.
     */
    @Deprecated
    Entity getShoulderEntityRight();

    /**
     * Sets the entity currently perched on the right shoulder, or null to
     * remove. This method will remove the entity from the world.
     * <br>
     * Note that only a copy of the entity will be set to display on the
     * shoulder.
     * <br>
     * Also note that the client will currently only render {@link Parrot}
     * entities.
     *
     * @param entity right shoulder entity
     * @deprecated There are currently no well defined semantics regarding
     * serialized entities in Bukkit. Use with care.
     */
    @Deprecated
    void setShoulderEntityRight(Entity entity);
}
