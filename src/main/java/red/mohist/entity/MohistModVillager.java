package red.mohist.entity;

import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftAbstractVillager;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import red.mohist.forge.util.ResourceLocationUtil;

public class MohistModVillager extends CraftAbstractVillager {

    private final EntityType entityType;

    public MohistModVillager(CraftServer server, AbstractVillagerEntity entity) {
        super(server, entity);
        this.entityType = EntityType.valueOf(ResourceLocationUtil.standardize(ForgeRegistries.ENTITIES.getKey(entity.getType())));
    }

    @Override
    public @NotNull EntityType getType() {
        return this.entityType;
    }

    @Override
    public String toString() {
        return "MohistModVillager{" + entityType + '}';
    }
}

