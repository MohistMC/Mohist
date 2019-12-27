package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.horse.AbstractChestedHorseEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ChestedHorse;

public abstract class CraftChestedHorse extends CraftAbstractHorse implements ChestedHorse {

    public CraftChestedHorse(CraftServer server, HorseEntityChestedAbstract entity) {
        super(server, entity);
    }

    @Override
    public HorseEntityChestedAbstract getHandle() {
        return (HorseEntityChestedAbstract) super.getHandle();
    }

    @Override
    public boolean isCarryingChest() {
        return getHandle().isCarryingChest();
    }

    @Override
    public void setCarryingChest(boolean chest) {
        if (chest == isCarryingChest()) return;
        getHandle().setCarryingChest(chest);
        getHandle().loadChest();
    }
}
