package org.bukkit.craftbukkit.v1_12_R1.entity;

import net.minecraft.entity.EntityCreature;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;

public class CraftCreature extends CraftLivingEntity implements Creature {
    public CraftCreature(CraftServer server, EntityCreature entity) {
        super(server, entity);
    }

    public CraftLivingEntity getTarget() {
        if (getHandle().getAttackTarget() == null) {
            return null;
        }

        return (CraftLivingEntity) getHandle().getAttackTarget().getBukkitEntity();
    }

    public void setTarget(LivingEntity target) {
        EntityCreature entity = getHandle();
        if (target == null) {
            entity.setGoalTarget(null, null, false);
        } else if (target instanceof CraftLivingEntity) {
            entity.setGoalTarget(((CraftLivingEntity) target).getHandle(), null, false);
        }
    }

    @Override
    public EntityCreature getHandle() {
        return (EntityCreature) entity;
    }

    @Override
    public String toString() {
        return "CraftCreature{name=" + this.entityName + "}";
    }
}
