package org.bukkit.craftbukkit.v1_20_R2.entity;

import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.entity.ExperienceOrb;

public class CraftExperienceOrb extends CraftEntity implements ExperienceOrb {
    public CraftExperienceOrb(CraftServer server, net.minecraft.world.entity.ExperienceOrb entity) {
        super(server, entity);
    }

    @Override
    public int getExperience() {
        return getHandle().value;
    }

    @Override
    public void setExperience(int value) {
        getHandle().value = value;
    }

    // Paper start
    public java.util.UUID getTriggerEntityId() {
        return getHandle().triggerEntityId;
    }
    public java.util.UUID getSourceEntityId() {
        return getHandle().sourceEntityId;
    }
    public SpawnReason getSpawnReason() {
        return getHandle().spawnReason;
    }
    // Paper end

    @Override
    public net.minecraft.world.entity.ExperienceOrb getHandle() {
        return (net.minecraft.world.entity.ExperienceOrb) entity;
    }

    @Override
    public String toString() {
        return "CraftExperienceOrb";
    }
}
