package org.bukkit.craftbukkit.v1_15_R1.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.Spellcaster;

public class CraftSpellcaster extends CraftIllager implements Spellcaster {

    public CraftSpellcaster(CraftServer server, SpellcastingIllagerEntity entity) {
        super(server, entity);
    }

    @Override
    public SpellcastingIllagerEntity getHandle() {
        return (SpellcastingIllagerEntity) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftSpellcaster";
    }

    @Override
    public Spell getSpell() {
        return Spell.valueOf(getHandle().getSpellType().name());
    }

    @Override
    public void setSpell(Spell spell) {
        Preconditions.checkArgument(spell != null, "Use Spell.NONE");

        getHandle().setSpellType(SpellcastingIllagerEntity.SpellType.getFromId(spell.ordinal()));
    }
}
