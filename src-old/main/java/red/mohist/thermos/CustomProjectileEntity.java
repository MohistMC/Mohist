package red.mohist.thermos;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraftforge.cauldron.entity.CraftCustomEntity;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

public class CustomProjectileEntity extends CraftCustomEntity implements Projectile {
    private ProjectileSource shooter = null;
    private boolean doesBounce;

    public CustomProjectileEntity(CraftServer server, Entity entity) {
        super(server, entity);
    }

    public static final GameProfile dropper =  new GameProfile(UUID.nameUUIDFromBytes("[Dropper]".getBytes()), "[Dropper]");

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
    public String toString()
    {
        return "CraftCustomProjectile";
    }
}

