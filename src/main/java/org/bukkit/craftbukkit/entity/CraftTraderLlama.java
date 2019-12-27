package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.horse.TraderLlamaEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TraderLlama;

public class CraftTraderLlama extends CraftLlama implements TraderLlama {

    public CraftTraderLlama(CraftServer server, LlamaEntityTrader entity) {
        super(server, entity);
    }

    @Override
    public LlamaEntityTrader getHandle() {
        return (LlamaEntityTrader) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftTraderLlama";
    }

    @Override
    public EntityType getType() {
        return EntityType.TRADER_LLAMA;
    }
}
