package org.bukkit.craftbukkit.v1_15_R1.entity;

import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WanderingTrader;

public class CraftWanderingTrader extends CraftAbstractVillager implements WanderingTrader {

    public CraftWanderingTrader(CraftServer server, WanderingTraderEntity entity) {
        super(server, entity);
    }

    @Override
    public WanderingTraderEntity getHandle() {
        return (WanderingTraderEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftWanderingTrader";
    }

    @Override
    public EntityType getType() {
        return EntityType.WANDERING_TRADER;
    }
}
