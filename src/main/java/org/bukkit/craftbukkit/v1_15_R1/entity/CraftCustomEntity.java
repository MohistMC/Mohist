package org.bukkit.craftbukkit.v1_15_R1.entity;

import net.minecraft.entity.Entity;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

/**
 * CraftCustomEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 08/06/2020 - 07:23 pm
 */
public class CraftCustomEntity extends CraftEntity {

    public CraftCustomEntity(CraftServer server, Entity entity) {
        super(server, entity);
    }

    @Override
    public @NotNull EntityType getType() {
        return null;
    }
}
