package org.bukkit.craftbukkit.v1_12_R1.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraftforge.cauldron.entity.CraftCustomEntity;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

import java.util.UUID;

public class CraftCustomProjectile extends CraftCustomEntity implements Projectile {
    public static final GameProfile dropper = new GameProfile(UUID.nameUUIDFromBytes("[Dropper]".getBytes()), "[Dropper]");
    private ProjectileSource shooter = null;
    private boolean doesBounce;

    public CraftCustomProjectile(CraftServer server, Entity entity) {
        super(server, entity);
    }

    @Override
    public ProjectileSource getShooter() {
        return shooter;
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        this.shooter = shooter;
    }

    @Override
    public boolean doesBounce() {
        return doesBounce;
    }

    @Override
    public void setBounce(boolean doesBounce) {
        this.doesBounce = doesBounce;
    }

    @Override
    public String toString() {
        return "CraftCustomProjectile";
    }
}