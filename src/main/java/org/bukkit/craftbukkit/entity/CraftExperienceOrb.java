package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.item.ExperienceOrbEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;

public class CraftExperienceOrb extends CraftEntity implements ExperienceOrb {
    public CraftExperienceOrb(CraftServer server, ExperienceOrbEntity entity) {
        super(server, entity);
    }

    @Override
    public int getExperience() {
        return getHandle().xpValue;
    }

    @Override
    public void setExperience(int value) {
        getHandle().xpValue = value;
    }

    @Override
    public ExperienceOrbEntity getHandle() {
        return (ExperienceOrbEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftExperienceOrb";
    }

    @Override
    public EntityType getType() {
        return EntityType.EXPERIENCE_ORB;
    }
}
