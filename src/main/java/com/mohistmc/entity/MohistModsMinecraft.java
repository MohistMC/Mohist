package com.mohistmc.entity;

import com.mohistmc.api.EntityAPI;
import java.util.Objects;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftMinecart;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class MohistModsMinecraft extends CraftMinecart {

    public String entityName;

    public MohistModsMinecraft(CraftServer server, AbstractMinecart entity) {
        super(server, entity);
        this.entityName = EntityAPI.entityName(entity);
    }

    @Override
    public AbstractMinecart getHandle() {
        return (AbstractMinecart) this.entity;
    }


    @NotNull
    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        return Objects.requireNonNullElse(type, EntityType.FORGE_MOD_CHEST_HORSE);
    }

    @Override
    public String toString() {
        return "MohistModsMinecraft{" + entityName + '}';
    }
}
