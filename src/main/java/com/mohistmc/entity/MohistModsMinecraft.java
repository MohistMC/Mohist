package com.mohistmc.entity;

import com.mohistmc.api.ServerAPI;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftMinecart;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class MohistModsMinecraft extends CraftMinecart {

    private final EntityType entityType;

    public MohistModsMinecraft(CraftServer server, AbstractMinecart entity) {
        super(server, entity);
        this.entityType = EntityType.valueOf(ServerAPI.entityTypeMap.get(ForgeRegistries.ENTITIES.getKey(entity.getType())));
    }

    @Override
    public AbstractMinecart getHandle() {
        return (AbstractMinecart) this.entity;
    }

    @NotNull
    @Override
    public EntityType getType() {
        return entityType;
    }

    @Override
    public String toString() {
        return "CraftCustomMinecart{" + entityType + '}';
    }
}
