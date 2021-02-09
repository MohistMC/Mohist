package com.mohistmc.entity;

import com.mohistmc.api.ServerAPI;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftMinecart;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class CraftCustomMinecraft extends CraftMinecart {

    private final EntityType entityType;

    public CraftCustomMinecraft(CraftServer server, AbstractMinecartEntity entity) {
        super(server, entity);
        this.entityType = EntityType.valueOf(ServerAPI.entityTypeMap.get(ForgeRegistries.ENTITIES.getKey(entity.getType())));
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
