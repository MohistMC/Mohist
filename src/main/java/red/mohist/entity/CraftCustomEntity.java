package red.mohist.entity;

import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import red.mohist.forge.util.ResourceLocationUtil;

public class CraftCustomEntity extends CraftEntity {

    public final EntityType entityName;

    public CraftCustomEntity(CraftServer server, net.minecraft.entity.Entity entity) {
        super(server, entity);
        this.entityName = EntityType.valueOf(ResourceLocationUtil.standardize(ForgeRegistries.ENTITIES.getKey(entity.getType())));
    }

    @Override
    public net.minecraft.entity.Entity getHandle() {
        return this.entity;
    }

    @Override
    public String toString() {
        return "CraftCustomEntity{" + entityName + '}';
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

}