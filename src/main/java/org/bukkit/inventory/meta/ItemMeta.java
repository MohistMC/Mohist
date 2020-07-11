package org.bukkit.inventory.meta;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This type represents the storage mechanism for auxiliary item data.
 * <p>
 * An implementation will handle the creation and application for ItemMeta.
 * This class should not be implemented by a plugin in a live environment.
 */
public interface ItemMeta extends Cloneable, ConfigurationSerializable {

    /**
     * Checks for existence of a display name.
     *
     * @return true if this has a display name
     */
    boolean hasDisplayName();

    /**
     * Gets the display name that is set.
     * <p>
     * Plugins should check that hasDisplayName() returns <code>true</code>
     * before calling this method.
     *
     * @return the display name that is set
     */
    String getDisplayName();

    /**
     * Sets the display name.
     *
     * @param name the name to set
     */
    void setDisplayName(String name);

    /**
     * Checks for existence of a localized name.
     *
     * @return true if this has a localized name
     */
    boolean hasLocalizedName();

    /**
     * Gets the localized display name that is set.
     * <p>
     * Plugins should check that hasLocalizedName() returns <code>true</code>
     * before calling this method.
     *
     * @return the localized name that is set
     */
    String getLocalizedName();

    /**
     * Sets the localized name.
     *
     * @param name the name to set
     */
    void setLocalizedName(String name);

    /**
     * Checks for existence of lore.
     *
     * @return true if this has lore
     */
    boolean hasLore();

    /**
     * Gets the lore that is set.
     * <p>
     * Plugins should check if hasLore() returns <code>true</code> before
     * calling this method.
     *
     * @return a list of lore that is set
     */
    List<String> getLore();

    /**
     * Sets the lore for this item.
     * Removes lore when given null.
     *
     * @param lore the lore that will be set
     */
    void setLore(List<String> lore);

    /**
     * Checks for the existence of any enchantments.
     *
     * @return true if an enchantment exists on this meta
     */
    boolean hasEnchants();

    /**
     * Checks for existence of the specified enchantment.
     *
     * @param ench enchantment to check
     * @return true if this enchantment exists for this meta
     */
    boolean hasEnchant(Enchantment ench);

    /**
     * Checks for the level of the specified enchantment.
     *
     * @param ench enchantment to check
     * @return The level that the specified enchantment has, or 0 if none
     */
    int getEnchantLevel(Enchantment ench);

    /**
     * Returns a copy the enchantments in this ItemMeta. <br>
     * Returns an empty map if none.
     *
     * @return An immutable copy of the enchantments
     */
    Map<Enchantment, Integer> getEnchants();

    /**
     * Adds the specified enchantment to this item meta.
     *
     * @param ench                   Enchantment to add
     * @param level                  Level for the enchantment
     * @param ignoreLevelRestriction this indicates the enchantment should be
     *                               applied, ignoring the level limit
     * @return true if the item meta changed as a result of this call, false
     * otherwise
     */
    boolean addEnchant(Enchantment ench, int level, boolean ignoreLevelRestriction);

    /**
     * Removes the specified enchantment from this item meta.
     *
     * @param ench Enchantment to remove
     * @return true if the item meta changed as a result of this call, false
     * otherwise
     */
    boolean removeEnchant(Enchantment ench);

    /**
     * Checks if the specified enchantment conflicts with any enchantments in
     * this ItemMeta.
     *
     * @param ench enchantment to test
     * @return true if the enchantment conflicts, false otherwise
     */
    boolean hasConflictingEnchant(Enchantment ench);

    /**
     * Set itemflags which should be ignored when rendering a ItemStack in the Client. This Method does silently ignore double set itemFlags.
     *
     * @param itemFlags The hideflags which shouldn't be rendered
     */
    void addItemFlags(ItemFlag... itemFlags);

    /**
     * Remove specific set of itemFlags. This tells the Client it should render it again. This Method does silently ignore double removed itemFlags.
     *
     * @param itemFlags Hideflags which should be removed
     */
    void removeItemFlags(ItemFlag... itemFlags);

    /**
     * Get current set itemFlags. The collection returned is unmodifiable.
     *
     * @return A set of all itemFlags set
     */
    Set<ItemFlag> getItemFlags();

    /**
     * Check if the specified flag is present on this item.
     *
     * @param flag the flag to check
     * @return if it is present
     */
    boolean hasItemFlag(ItemFlag flag);

    /**
     * Return if the unbreakable tag is true. An unbreakable item will not lose
     * durability.
     *
     * @return true if the unbreakable tag is true
     */
    boolean isUnbreakable();

    /**
     * Sets the unbreakable tag. An unbreakable item will not lose durability.
     *
     * @param unbreakable true if set unbreakable
     */
    void setUnbreakable(boolean unbreakable);

    @SuppressWarnings("javadoc")
    ItemMeta clone();

    Spigot spigot();

    // Spigot start
    class Spigot {
        /**
         * Return if the unbreakable tag is true
         *
         * @return true if the unbreakable tag is true
         * @deprecated see {@link ItemMeta#isUnbreakable()}
         */
        @Deprecated
        public boolean isUnbreakable() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Sets the unbreakable tag
         *
         * @param unbreakable true if set unbreakable
         * @deprecated see {@link ItemMeta#setUnbreakable(boolean)}
         */
        @Deprecated
        public void setUnbreakable(boolean unbreakable) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    // Spigot end
}
