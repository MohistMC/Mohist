package red.mohist.entity;

import net.minecraft.entity.item.minecart.ContainerMinecartEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftMinecartContainer;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import red.mohist.forge.util.ResourceLocationUtil;

public class MohistModMinecartContainer extends CraftMinecartContainer {

    private final EntityType entityType;

    public MohistModMinecartContainer(CraftServer server, ContainerMinecartEntity entity) {
        super(server, entity);
        this.entityType = EntityType.valueOf(ResourceLocationUtil.standardize(ForgeRegistries.ENTITIES.getKey(entity.getType())));
    }

    @Override
    public @NotNull EntityType getType() {
        return entityType;
    }

    @Override
    public String toString() {
        return "MohistModMinecartContainer{" + entityType + '}';
    }
}
