package red.mohist.entity;

import net.minecraft.entity.passive.horse.AbstractChestedHorseEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftChestedHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.jetbrains.annotations.NotNull;
import red.mohist.forge.util.ResourceLocationUtil;

public class MohistModChestedHorse extends CraftChestedHorse {

    private final EntityType entityType;

    public MohistModChestedHorse(CraftServer server, AbstractChestedHorseEntity entity) {
        super(server, entity);
        this.entityType = EntityType.valueOf(ResourceLocationUtil.standardize(ForgeRegistries.ENTITIES.getKey(entity.getType())));
    }

    @Override
    public @NotNull EntityType getType() {
        return this.entityType;
    }

    @Override
    public Horse.@NotNull Variant getVariant() {
        return Horse.Variant.HORSE;
    }

    @Override
    public String toString() {
        return "MohistModChestedHorse{" + entityType + '}';
    }
}
