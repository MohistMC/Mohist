package com.mohistmc.bukkit.entity;

import com.mohistmc.api.EntityAPI;
import java.util.Objects;
import net.minecraft.world.entity.raid.Raider;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftRaider;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;

public class MohistModsRaider extends CraftRaider {

    public String entityName;

    public MohistModsRaider(CraftServer server, Raider entity) {
        super(server, entity);
        this.entityName = EntityAPI.entityName(entity);
    }

    @Override
    public Raider getHandle() {
        return (Raider) this.entity;
    }


    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        return Objects.requireNonNullElse(type, EntityType.FORGE_MOD_PROJECTILE);
    }

    @Override
    public String toString() {
        return "MohistModsRaider{" + entityName + '}';
    }

    @Override
    public EntityCategory getCategory() {
        return EntityCategory.ILLAGER;
    }
}
