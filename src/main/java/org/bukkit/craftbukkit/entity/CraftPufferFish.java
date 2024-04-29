package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.PufferFish;

public class CraftPufferFish extends CraftFish implements PufferFish {

    public CraftPufferFish(CraftServer server, net.minecraft.world.entity.animal.Pufferfish entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.Pufferfish getHandle() {
        return (net.minecraft.world.entity.animal.Pufferfish) super.getHandle();
    }

    @Override
    public int getPuffState() {
        return getHandle().getPuffState();
    }

    @Override
    public void setPuffState(int state) {
        getHandle().setPuffState(state);
    }

    @Override
    public String toString() {
        return "CraftPufferFish";
    }
}
