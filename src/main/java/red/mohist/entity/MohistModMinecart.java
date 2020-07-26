package red.mohist.entity;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftMinecart;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import red.mohist.forge.util.ResourceLocationUtil;

public class MohistModMinecart extends CraftMinecart {

    private final EntityType entityType;

    public MohistModMinecart(CraftServer server, AbstractMinecartEntity entity) {
        super(server, entity);
        this.entityType = EntityType.valueOf(ResourceLocationUtil.standardize(ForgeRegistries.ENTITIES.getKey(entity.getType())));
    }

    @Override
    public @NotNull EntityType getType() {
        return entityType;
    }

    @Override
    public String toString() {
        return "MohistModMinecart{" + entityType + '}';
    }
}