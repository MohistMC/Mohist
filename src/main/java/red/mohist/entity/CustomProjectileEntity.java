package red.mohist.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CustomProjectileEntity extends CraftCustomEntity implements Projectile {

    private ProjectileSource shooter = null;
    private boolean doesBounce;

    public CustomProjectileEntity(CraftServer server, Entity entity) {
        super(server, entity);
    }

    public static final GameProfile dropper =  new GameProfile(UUID.nameUUIDFromBytes("[Dropper]".getBytes()), "[Dropper]");

    @Override
    public EntityType getType() {
        EntityType type = this.entityName;
        if (type != null) {
            return type;
        } else {
            return EntityType.FORGE_MOD_PROJECTILE;
        }
    }

    @Override
    public String toString()
    {
        return "CraftCustomProjectile";
    }

    @Override
    public @Nullable ProjectileSource getShooter() {
        return shooter;
    }

    @Override
    public void setShooter(@Nullable ProjectileSource source) {
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


