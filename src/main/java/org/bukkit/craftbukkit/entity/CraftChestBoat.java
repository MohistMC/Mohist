package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.vehicle.ChestBoat;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.loot.LootTable;

public class CraftChestBoat extends CraftBoat implements org.bukkit.entity.ChestBoat {

    private final Inventory inventory;

    public CraftChestBoat(CraftServer server, ChestBoat entity) {
        super(server, entity);
        this.inventory = new CraftInventory(entity);
    }

    @Override
    public ChestBoat getHandle() {
        return (ChestBoat) this.entity;
    }

    @Override
    public String toString() {
        return "CraftChestBoat";
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public void setLootTable(LootTable table) {
        this.setLootTable(table, this.getSeed());
    }

    @Override
    public LootTable getLootTable() {
        return CraftLootTable.minecraftToBukkit(this.getHandle().getLootTable());
    }

    @Override
    public void setSeed(long seed) {
        this.setLootTable(this.getLootTable(), seed);
    }

    @Override
    public long getSeed() {
        return this.getHandle().getLootTableSeed();
    }

    private void setLootTable(LootTable table, long seed) {
        this.getHandle().setLootTable(CraftLootTable.bukkitToMinecraft(table));
        this.getHandle().setLootTableSeed(seed);
    }
}
