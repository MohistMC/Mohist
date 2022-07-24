package com.mohistmc.entity;

import com.mohistmc.api.EntityAPI;
import com.mohistmc.api.ServerAPI;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftMinecart;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;


public class MohistModsMinecart extends CraftMinecart {

    public String entityName;

    public MohistModsMinecart(CraftServer server, AbstractMinecart entity) {
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
        if (type != null) {
            return type;
        } else {
            return EntityType.FORGE_MOD_CHEST_HORSE;
        }
    }

    @Override
    public String toString() {
        return "MohistModsMinecart{" + entityName + '}';
    }
}
