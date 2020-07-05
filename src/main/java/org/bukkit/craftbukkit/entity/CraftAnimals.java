package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.UUID;
import net.minecraft.entity.passive.AnimalEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Animals;

public class CraftAnimals extends CraftAgeable implements Animals {

    public CraftAnimals(CraftServer server, AnimalEntity entity) {
        super(server, entity);
    }

    @Override
    public AnimalEntity getHandle() {
        return (AnimalEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftAnimals";
    }

    @Override
    public UUID getBreedCause() {
        return getHandle().playerInLove;
    }

    @Override
    public void setBreedCause(UUID uuid) {
        getHandle().playerInLove = uuid;
    }

    @Override
    public boolean isLoveMode() {
        return getHandle().isInLove();
    }

    @Override
    public void setLoveModeTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "Love mode ticks must be positive or 0");
        getHandle().setInLove(ticks);
    }

    @Override
    public int getLoveModeTicks() {
        return getHandle().inLove;
    }
}
