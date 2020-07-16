package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.AbstractDonkeyEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ChestedHorse;

public abstract class CraftChestedHorse extends CraftAbstractHorse implements ChestedHorse {

    public CraftChestedHorse(CraftServer server, AbstractDonkeyEntity entity) {
        super(server, entity);
    }

    @Override
    public AbstractDonkeyEntity getHandle() {
        return (AbstractDonkeyEntity) super.getHandle();
    }

    @Override
    public boolean isCarryingChest() {
        return getHandle().hasChest();
    }

    @Override
    public void setCarryingChest(boolean chest) {
        if (chest == isCarryingChest()) return;
        getHandle().setHasChest(chest);
        getHandle().method_6721();
    }
}
