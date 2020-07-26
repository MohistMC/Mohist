package red.mohist.entity;

import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;

public class CraftCustomAbstractHorse extends CraftAbstractHorse {

    public CraftCustomAbstractHorse(CraftServer server, AbstractHorse entity) {
        super(server, (AbstractHorseEntity) entity);
    }

    @Override
    public String toString() {
        return "CraftCustomAbstractHorse";
    }

    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.getName());
        if (type != null) {
            return type;
        } else {
            return EntityType.FORGE_MOD;
        }
    }

    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.FORGE_MOD_HORSE;
    }
}
