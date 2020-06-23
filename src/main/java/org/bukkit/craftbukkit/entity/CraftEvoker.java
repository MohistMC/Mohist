package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;

public class CraftEvoker extends CraftSpellcaster implements Evoker {

    public CraftEvoker(CraftServer server, EvokerEntity entity) {
        super(server, entity);
    }

    @Override
    public EvokerEntity getHandle() {
        return (EvokerEntity) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftEvoker";
    }

    @Override
    public EntityType getType() {
        return EntityType.EVOKER;
    }

    @Override
    public Evoker.Spell getCurrentSpell() {
        return Evoker.Spell.values()[getHandle().getSpellType().ordinal()];
    }

    @Override
    public void setCurrentSpell(Evoker.Spell spell) {
        getHandle().setSpellType(spell == null ? SpellcastingIllagerEntity.SpellType.NONE : SpellcastingIllagerEntity.SpellType.getFromId(spell.ordinal()));
    }
}
