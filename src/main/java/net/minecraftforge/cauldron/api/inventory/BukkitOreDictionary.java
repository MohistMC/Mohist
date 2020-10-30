package net.minecraftforge.cauldron.api.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * The Forge Ore Dictionary provides a way for multiple items to share a common
 * identifier - for instance, "ingotCopper" - and be used interchangeably in
 * crafting recipes.
 * <p/>
 * This class provides a read-only interface to the ore dictionary using Bukkit
 * classes, instead of NMS classes.
 */
public interface BukkitOreDictionary {
    /**
     * If an item's damage is a wildcard, this will be returned for the item
     * damage.
     */
    public static final int WILDCARD_VALUE = Short.MAX_VALUE;

    /**
     * Check the OreDictionaryEntry for a given name. The name can be later
     * retrieved using {@link #getOreName(OreDictionaryEntry)}, and having an
     * entry is required to call {@link #getDefinitions(OreDictionaryEntry)}.
     *
     * @param name name in the ore dictionary
     * @return ore dictionary entry, or null if name is not present
     */
    public OreDictionaryEntry getOreEntry(String name);

    /**
     * Get the string name defined for the given OreDictionaryEntry.
     * <p/>
     * This is called by {@link OreDictionaryEntry#getName()}.
     *
     * @param entry ore dictionary entry
     * @return ore dictionary name
     */
    public String getOreName(OreDictionaryEntry entry);

    /**
     * Get all of the OreDictionaryEntry objects registered to the given
     * ItemStack.
     *
     * @param itemStack itemstack to check - amount is ignored
     * @return immutable list of ore dictionary entries
     */
    public List<OreDictionaryEntry> getOreEntries(ItemStack itemStack);

    /**
     * Get all of the OreDictionaryEntry objects registered to the given
     * material and no item damage.
     *
     * @param material material to check
     * @return immutable list of ore dictionary entries
     */
    public List<OreDictionaryEntry> getOreEntries(Material material);

    /**
     * Get all of the ItemStacks registered to the given ore dictionary entry.
     * <p/>
     * Quantity should be ignored.
     *
     * @param entry ore dictionary entry
     * @return immutable list of itemstacks
     */
    public List<ItemStack> getDefinitions(OreDictionaryEntry entry);

    /**
     * Get all ore names in the dictionary. The returned list may contain
     * multiple null values or duplicates.
     *
     * @return all ore dictionary names
     */
    public List<String> getAllOreNames();
}
