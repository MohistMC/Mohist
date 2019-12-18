package org.bukkit.craftbukkit.v1_12_R1.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Spellcaster;

public class CraftSpellcaster extends CraftIllager implements Spellcaster {

    public CraftSpellcaster(CraftServer server, EntitySpellcasterIllager entity) {
        super(server, entity);
    }

    @Override
    public EntitySpellcasterIllager getHandle() {
        return (EntitySpellcasterIllager) super.getHandle();
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

        getHandle().setSpellType(EntitySpellcasterIllager.SpellType.getFromId(spell.ordinal()));
    }
}
