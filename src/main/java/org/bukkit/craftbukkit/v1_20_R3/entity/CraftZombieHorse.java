package org.bukkit.craftbukkit.v1_20_R3.entity;

import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.ZombieHorse;

public class CraftZombieHorse extends CraftAbstractHorse implements ZombieHorse {

    public CraftZombieHorse(CraftServer server, net.minecraft.world.entity.animal.horse.ZombieHorse entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftZombieHorse";
    }

    @Override
    public Variant getVariant() {
        return Variant.UNDEAD_HORSE;
    }
}
