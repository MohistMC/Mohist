package com.mohistmc.entity;

import com.mohistmc.api.ServerAPI;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftMinecartContainer;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class MohistModsMinecartContainer extends CraftMinecartContainer {

    private final EntityType entityType;

    public MohistModsMinecartContainer(CraftServer server, AbstractMinecartContainer entity) {
        super(server, entity);
        this.entityType = EntityType.valueOf(ServerAPI.entityTypeMap.get(ForgeRegistries.ENTITIES.getKey(entity.getType())));
    }

    @Override
    public AbstractMinecartContainer getHandle() {
        return (AbstractMinecartContainer) this.entity;
    }


    @NotNull
    @Override
    public EntityType getType() {
        return entityType;
    }

    @Override
    public String toString() {
        return "CraftCustomMinecartContainer{" + entityType + '}';
    }
}
