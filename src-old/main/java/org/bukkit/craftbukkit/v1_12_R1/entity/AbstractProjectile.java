package org.bukkit.craftbukkit.v1_12_R1.entity;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Projectile;

public abstract class AbstractProjectile extends CraftEntity implements Projectile {

    private boolean doesBounce;

    public AbstractProjectile(CraftServer server, net.minecraft.entity.Entity entity) {
        super(server, entity);
        doesBounce = false;
    }

    public boolean doesBounce() {
        return doesBounce;
    }

    public void setBounce(boolean doesBounce) {
        this.doesBounce = doesBounce;
    }

}
