package org.bukkit.craftbukkit.v1_16_R3.inventory.util;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public final class CraftInventoryCreator {

    public static final CraftInventoryCreator INSTANCE = new CraftInventoryCreator();
    //
    private final CraftCustomInventoryConverter DEFAULT_CONVERTER = new CraftCustomInventoryConverter();
    private final Map<InventoryType, InventoryConverter> converterMap = new HashMap<>();

    private CraftInventoryCreator() {
        converterMap.put(InventoryType.CHEST, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.DISPENSER, new CraftTileInventoryConverter.Dispenser());
        converterMap.put(InventoryType.DROPPER, new CraftTileInventoryConverter.Dropper());
        converterMap.put(InventoryType.FURNACE, new CraftTileInventoryConverter.Furnace());
        converterMap.put(InventoryType.WORKBENCH, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.ENCHANTING, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.BREWING, new CraftTileInventoryConverter.BrewingStand());
        converterMap.put(InventoryType.PLAYER, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.MERCHANT, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.ENDER_CHEST, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.ANVIL, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.SMITHING, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.BEACON, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.HOPPER, new CraftTileInventoryConverter.Hopper());
        converterMap.put(InventoryType.SHULKER_BOX, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.BARREL, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.BLAST_FURNACE, new CraftTileInventoryConverter.BlastFurnace());
        converterMap.put(InventoryType.LECTERN, new CraftTileInventoryConverter.Lectern());
        converterMap.put(InventoryType.SMOKER, new CraftTileInventoryConverter.Smoker());
        converterMap.put(InventoryType.LOOM, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.CARTOGRAPHY, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.GRINDSTONE, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.STONECUTTER, DEFAULT_CONVERTER);
    }

    public Inventory createInventory(InventoryHolder holder, InventoryType type) {
        return converterMap.get(type).createInventory(holder, type);
    }

    // Paper start
    public Inventory createInventory(InventoryHolder holder, InventoryType type, net.kyori.adventure.text.Component title) {
        // Paper start
        if (holder != null) {
            return DEFAULT_CONVERTER.createInventory(holder, type, title);
        }
        //noinspection ConstantConditions // Paper end
        return converterMap.get(type).createInventory(holder, type, title);
    }
    // Paper end

    public Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
        return converterMap.get(type).createInventory(holder, type, title);
    }

    public Inventory createInventory(InventoryHolder holder, int size) {
        return DEFAULT_CONVERTER.createInventory(holder, size);
    }

    // Paper start
    public Inventory createInventory(InventoryHolder holder, int size, net.kyori.adventure.text.Component title) {
        return DEFAULT_CONVERTER.createInventory(holder, size, title);
    }
    // Paper end
    
    public Inventory createInventory(InventoryHolder holder, int size, String title) {
        return DEFAULT_CONVERTER.createInventory(holder, size, title);
    }

    public interface InventoryConverter {

        Inventory createInventory(InventoryHolder holder, InventoryType type);

        // Paper start
        Inventory createInventory(InventoryHolder holder, InventoryType type, net.kyori.adventure.text.Component title);
        // Paper end

        Inventory createInventory(InventoryHolder holder, InventoryType type, String title);
    }
}
