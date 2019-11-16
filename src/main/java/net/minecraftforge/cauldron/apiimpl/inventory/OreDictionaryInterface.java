package net.minecraftforge.cauldron.apiimpl.inventory;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.cauldron.api.inventory.BukkitOreDictionary;
import net.minecraftforge.cauldron.api.inventory.OreDictionaryEntry;
import net.minecraftforge.oredict.OreDictionary;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OreDictionaryInterface implements BukkitOreDictionary {
    private Map<String, String> normalizedToCanonicalMap = null;

    private void initializeMap() {
        normalizedToCanonicalMap = new HashMap<String, String>();

        for (String str : getAllOreNames()) {
            if (str == null || str.isEmpty()) {
                continue;
            }
            normalizedToCanonicalMap.put(Material.normalizeName(str), str);
        }
    }

    @Override
    public OreDictionaryEntry getOreEntry(String name) {
        if (normalizedToCanonicalMap == null) {
            initializeMap();
        }

        String canonical = normalizedToCanonicalMap.get(Material.normalizeName(name));
        if (canonical == null) {
            return null;
        }

        return OreDictionaryEntry.valueOf(OreDictionary.getOreID(canonical));
    }

    @Override
    public List<OreDictionaryEntry> getOreEntries(ItemStack itemStack) {
        int[] ids = OreDictionary.getOreIDs(CraftItemStack.asNMSCopy(itemStack));

        ImmutableList.Builder<OreDictionaryEntry> builder = ImmutableList.builder();
        for (int id : ids) {
            builder.add(OreDictionaryEntry.valueOf(id));
        }

        return builder.build();
    }

    @Override
    public List<OreDictionaryEntry> getOreEntries(Material material) {
        return getOreEntries(new ItemStack(material));
    }

    @Override
    public String getOreName(OreDictionaryEntry entry) {
        return OreDictionary.getOreName(entry.getId());
    }

    @Override
    public List<ItemStack> getDefinitions(OreDictionaryEntry entry) {
        @SuppressWarnings("deprecation")
        List<net.minecraft.item.ItemStack> items = OreDictionary.getOres(entry.getId());

        ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
        for (net.minecraft.item.ItemStack nmsItem : items) {
            builder.add(CraftItemStack.asCraftMirror(nmsItem));
        }

        return builder.build();
    }

    @Override
    public List<String> getAllOreNames() {
        return Arrays.asList(OreDictionary.getOreNames());
    }
}
