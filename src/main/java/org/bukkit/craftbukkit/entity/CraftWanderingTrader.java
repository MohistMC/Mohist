package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WanderingTrader;

public class CraftWanderingTrader extends CraftAbstractVillager implements WanderingTrader {

    public CraftWanderingTrader(CraftServer server, VillagerEntityTrader entity) {
        super(server, entity);
    }

    @Override
    public VillagerEntityTrader getHandle() {
        return (VillagerEntityTrader) entity;
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
