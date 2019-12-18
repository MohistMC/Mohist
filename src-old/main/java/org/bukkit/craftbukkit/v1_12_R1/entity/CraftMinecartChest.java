package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.item.EntityMinecartChest;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.inventory.Inventory;

public class CraftMinecartChest extends CraftMinecart implements StorageMinecart {
    private final CraftInventory inventory;

    public CraftMinecartChest(CraftServer server, EntityMinecartChest entity) {
        super(server, entity);
        inventory = new CraftInventory(entity);
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public String toString() {
        return "CraftMinecartChest{" + "inventory=" + inventory + '}';
    }

    public EntityType getType() {
        return EntityType.MINECART_CHEST;
    }
}
