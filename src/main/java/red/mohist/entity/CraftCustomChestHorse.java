package red.mohist.entity;

import net.minecraft.entity.passive.AbstractChestHorse;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftChestedHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;

public class CraftCustomChestHorse extends CraftChestedHorse {

    public CraftCustomChestHorse(CraftServer server, AbstractChestHorse entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftCustomChestHorse";
    }

    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
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
