package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.passive.StriderEntity;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Strider;

public class CraftStrider extends CraftAnimals implements Strider {

    public CraftStrider(CraftServer server, StriderEntity entity) {
        super(server, entity);
    }

    @Override
    public boolean isShivering() {
        return getHandle().func_234315_eI_();
    }

    @Override
    public void setShivering(boolean shivering) {
        this.getHandle().func_234319_t_(shivering);
    }

    @Override
    public boolean hasSaddle() {
        return getHandle().isHorseSaddled();
    }

    @Override
    public void setSaddle(boolean saddled) {
        getHandle().field_234313_bz_.func_233619_a_(saddled);
    }

    @Override
    public int getBoostTicks() {
        return getHandle().field_234313_bz_.field_233610_a_ ? getHandle().field_234313_bz_.field_233611_b_ : 0;
    }

    @Override
    public void setBoostTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks must be >= 0");

        getHandle().field_234313_bz_.setBoostTicks(ticks);
    }

    @Override
    public int getCurrentBoostTicks() {
        return getHandle().field_234313_bz_.field_233610_a_ ? getHandle().field_234313_bz_.field_233611_b_ : 0;
    }

    @Override
    public void setCurrentBoostTicks(int ticks) {
        if (!getHandle().field_234313_bz_.field_233610_a_) {
            return;
        }

        int max = getHandle().field_234313_bz_.field_233611_b_;
        Preconditions.checkArgument(ticks >= 0 && ticks <= max, "boost ticks must not exceed 0 or %d (inclusive)", max);

        this.getHandle().field_234313_bz_.field_233611_b_ = ticks;
    }

    @Override
    public Material getSteerMaterial() {
        return Material.WARPED_FUNGUS_ON_A_STICK;
    }

    @Override
    public StriderEntity getHandle() {
        return (StriderEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftStrider";
    }

    @Override
    public EntityType getType() {
        return EntityType.STRIDER;
    }
}
