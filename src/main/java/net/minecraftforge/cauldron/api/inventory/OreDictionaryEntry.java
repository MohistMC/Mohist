package net.minecraftforge.cauldron.api.inventory;

import net.minecraftforge.cauldron.api.Cauldron;

import java.util.ArrayList;
import java.util.List;

/**
 * An OreDictionaryEntry is an opaque reference to an entry in the Forge Ore
 * Dictionary. This class has reference equality.
 */
public class OreDictionaryEntry {
    private static List<OreDictionaryEntry> oreDictionaryEntries = new ArrayList<OreDictionaryEntry>();

    /**
     * Get an OreDictionaryEntry instance, using an instance cache to preserve
     * reference equality.
     *
     * @param id opaque ore dictionary id
     * @return object wrapper around id
     */
    public static OreDictionaryEntry valueOf(int id) {
        if (id < 0) throw new IllegalArgumentException("ore dictionary IDs are not negative");

        while (oreDictionaryEntries.size() < id + 1) {
            oreDictionaryEntries.add(new OreDictionaryEntry(oreDictionaryEntries.size()));
        }
        return oreDictionaryEntries.get(id);
    }

    private int id;
    private OreDictionaryEntry(int id) {
        this.id = id;
    }

    /**
     * Get the opaque ID of this ore-dictionary entry.
     *
     * Plugins should not inspect the results of this call. Results may not be
     * the same across multiple server startups.
     *
     * @return Opaque id number.
     */
    public int getId() {
        return id;
    }

    /**
     * Request the Ore Dictionary for the string identifier of this entry.
     *
     * @return ore dictionary string
     * @see net.minecraftforge.cauldron.api.inventory.BukkitOreDictionary#getOreName(OreDictionaryEntry)
     */
    public String getName() {
        return Cauldron.getOreDictionary().getOreName(this);
    }

    @Override
    public String toString() {
        return String.format("OreDictionary{id=%d,name=%s}", id, getName());
    }
}
