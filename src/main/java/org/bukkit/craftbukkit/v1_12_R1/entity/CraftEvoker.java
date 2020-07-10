package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.entity.passive.EntitySheep;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.jetbrains.annotations.Nullable;

public class CraftEvoker extends CraftSpellcaster implements Evoker {

    public CraftEvoker(CraftServer server, EntityEvoker entity) {
        super(server, entity);
    }

    @Override
    public EntityEvoker getHandle() {
        return (EntityEvoker) super.getHandle();
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
        getHandle().setSpellType(spell == null ? EntitySpellcasterIllager.SpellType.NONE : EntitySpellcasterIllager.SpellType.getFromId(spell.ordinal()));
    }

    // Purpur start
    @Override
    @Nullable
    public org.bukkit.entity.Sheep getWololoTarget() {
        EntitySheep target = getHandle().getWololoTarget();
        return target == null ? null : (org.bukkit.entity.Sheep) target.getBukkitEntity();
    }

    @Override
    public void setWololoTarget(@Nullable org.bukkit.entity.Sheep sheep) {
        getHandle().setWololoTarget(sheep == null ? null : (EntitySheep) ((CraftEntity) sheep).getHandle());
    }
    // Purpur end
}
