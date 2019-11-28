package cc.uraniummc.entity;

import net.minecraft.entity.Entity;
import net.minecraftforge.cauldron.entity.CraftCustomEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

public class CustomProjectileEntity extends CraftCustomEntity implements Projectile {
    private ProjectileSource shooter;
    private boolean doesBounce;

    public CustomProjectileEntity(CraftServer server, Entity entity) {
        super(server, entity);
    }

    @Override
    public LivingEntity _INVALID_getShooter() {
        if (shooter instanceof LivingEntity)
            return (LivingEntity) shooter;
        return null;
    }

    @Override
    public ProjectileSource getShooter() {
        return shooter;
    }

    @Override
    public void _INVALID_setShooter(LivingEntity shooter) {
        this.shooter = shooter instanceof ProjectileSource ? (ProjectileSource) shooter : null;
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
}
