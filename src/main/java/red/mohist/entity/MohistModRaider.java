package red.mohist.entity;

import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftRaider;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import red.mohist.forge.util.ResourceLocationUtil;

public class MohistModRaider extends CraftRaider {

    private final EntityType entityType;

    public MohistModRaider(CraftServer server, AbstractRaiderEntity entity) {
        super(server, entity);
        this.entityType = EntityType.valueOf(ResourceLocationUtil.standardize(ForgeRegistries.ENTITIES.getKey(entity.getType())));
    }

    @Override
    public @NotNull EntityType getType() {
        return entityType;
    }

    @Override
    public String toString() {
        return "MohistModRaider{" + entityType + '}';
    }
}
