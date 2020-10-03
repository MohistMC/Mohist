package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;

public class CraftPigZombie extends CraftZombie implements PigZombie {

    public CraftPigZombie(CraftServer server, ZombifiedPiglinEntity entity) {
        super(server, entity);
    }

    @Override
    public int getAnger() {
        return getHandle().func_230256_F__();
    }

    @Override
    public void setAnger(int level) {
        getHandle().func_230260_a__(level);
    }

    @Override
    public void setAngry(boolean angry) {
        setAnger(angry ? 400 : 0);
    }

    @Override
    public boolean isAngry() {
        return getAnger() > 0;
    }

    @Override
    public ZombifiedPiglinEntity getHandle() {
        return (ZombifiedPiglinEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftPigZombie";
    }

    @Override
    public EntityType getType() {
        return EntityType.ZOMBIFIED_PIGLIN;
    }

    @Override
    public boolean isConverting() {
        return false;
    }

    @Override
    public int getConversionTime() {
        throw new UnsupportedOperationException("Not supported by this Entity.");
    }

    @Override
    public void setConversionTime(int time) {
        throw new UnsupportedOperationException("Not supported by this Entity.");
    }
}
