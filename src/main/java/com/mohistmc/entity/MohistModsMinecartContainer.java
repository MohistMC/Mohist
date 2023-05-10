package com.mohistmc.entity;

import com.mohistmc.api.EntityAPI;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftMinecartContainer;
import org.bukkit.entity.EntityType;

import java.util.Objects;

public class MohistModsMinecartContainer extends CraftMinecartContainer {

    public String entityName;

    public MohistModsMinecartContainer(CraftServer server, AbstractMinecartContainer entity) {
        super(server, entity);
        this.entityName = EntityAPI.entityName(entity);
    }

    @Override
    public AbstractMinecartContainer getHandle() {
        return (AbstractMinecartContainer) this.entity;
    }

    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        return Objects.requireNonNullElse(type, EntityType.FORGE_MOD_MINECART_CONTAINER);
    }

    @Override
    public String toString() {
        return "MohistModsMinecartContainer{" + getType() + '}';
    }
}
