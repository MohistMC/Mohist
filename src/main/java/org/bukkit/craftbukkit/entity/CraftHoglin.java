package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.monster.HoglinEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hoglin;

public class CraftHoglin extends CraftAnimals implements Hoglin {

    public CraftHoglin(CraftServer server, HoglinEntity entity) {
        super(server, entity);
    }

    @Override
    public boolean isImmuneToZombification() {
        return getHandle().func_234368_eV_();
    }

    @Override
    public void setImmuneToZombification(boolean flag) {
        getHandle().func_234370_t_(flag);
    }

    @Override
    public boolean isAbleToBeHunted() {
        return getHandle().field_234359_bz_;
    }

    @Override
    public void setIsAbleToBeHunted(boolean flag) {
        getHandle().field_234359_bz_ = flag;
    }

    @Override
    public int getConversionTime() {
        Preconditions.checkState(isConverting(), "Entity not converting");
        return getHandle().field_234358_by_;
    }

    @Override
    public void setConversionTime(int time) {
        if (time < 0) {
            getHandle().field_234358_by_ = -1;
            getHandle().func_234370_t_(false);
        } else {
            getHandle().field_234358_by_ = time;
        }
    }

    @Override
    public boolean isConverting() {
        return getHandle().func_234364_eK_();
    }

    @Override
    public HoglinEntity getHandle() {
        return (HoglinEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftHoglin";
    }

    @Override
    public EntityType getType() {
        return EntityType.HOGLIN;
    }
}
