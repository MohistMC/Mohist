package red.mohist.entity;

import net.minecraft.entity.Entity;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftProjectile;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import red.mohist.forge.util.ResourceLocationUtil;

public class MohistModProjectile extends CraftProjectile {

    private final EntityType entityType;

    public MohistModProjectile(CraftServer server, Entity entity) {
        super(server, entity);
        this.entityType = EntityType.valueOf(ResourceLocationUtil.standardize(ForgeRegistries.ENTITIES.getKey(entity.getType())));
    }

    @Override
    public @NotNull EntityType getType() {
        return entityType;
    }

    @Override
    public String toString() {
        return "MohistModProjectile{" + entityType + '}';
    }
}

