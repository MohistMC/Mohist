package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.util.Identifier;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;

public abstract class CraftMinecartContainer extends CraftMinecart implements Lootable {

    public CraftMinecartContainer(CraftServer server, AbstractMinecartEntity entity) {
        super(server, entity);
    }

    @Override
    public StorageMinecartEntity getHandle() {
        return (StorageMinecartEntity) entity;
    }

    @Override
    public void setLootTable(LootTable table) {
        setLootTable(table, getSeed());
    }

    @Override
    public LootTable getLootTable() {
        Identifier nmsTable = getHandle().lootTableId;
        if (nmsTable == null) {
            return null; // return empty loot table?
        }

        NamespacedKey key = CraftNamespacedKey.fromMinecraft(nmsTable);
        return Bukkit.getLootTable(key);
    }

    @Override
    public void setSeed(long seed) {
        setLootTable(getLootTable(), seed);
    }

    @Override
    public long getSeed() {
        return getHandle().lootSeed;
    }

    private void setLootTable(LootTable table, long seed) {
        Identifier newKey = (table == null) ? null : CraftNamespacedKey.toMinecraft(table.getKey());
        getHandle().setLootTable(newKey, seed);
    }
}
