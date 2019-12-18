package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.item.EntityMinecartHopper;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.inventory.Inventory;

final class CraftMinecartHopper extends CraftMinecart implements HopperMinecart {
    private final CraftInventory inventory;

    CraftMinecartHopper(CraftServer server, EntityMinecartHopper entity) {
        super(server, entity);
        inventory = new CraftInventory(entity);
    }

    @Override
    public String toString() {
        return "CraftMinecartHopper{" + "inventory=" + inventory + '}';
    }

    public EntityType getType() {
        return EntityType.MINECART_HOPPER;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public boolean isEnabled() {
        return ((EntityMinecartHopper) getHandle()).getBlocked();
    }

    @Override
    public void setEnabled(boolean enabled) {
        ((EntityMinecartHopper) getHandle()).setBlocked(enabled);
    }
}
