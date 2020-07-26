package red.mohist.entity;

import net.minecraft.entity.passive.horse.AbstractChestedHorseEntity;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftChestedHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;

public class CraftCustomChestHorse extends CraftChestedHorse {

    public EntityType entityName;

    public CraftCustomChestHorse(CraftServer server, AbstractChestedHorseEntity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftCustomChestHorse";
    }

    @Override
    public EntityType getType() {
        EntityType type = this.entityName;
        if (type != null) {
            return type;
        } else {
            return EntityType.FORGE_MOD;
        }
    }

    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.FORGE_MOD_CHEST_HORSE;
    }
}
