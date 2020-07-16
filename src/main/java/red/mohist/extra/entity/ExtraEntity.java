package red.mohist.extra.entity;

import net.minecraft.entity.Entity;

public interface ExtraEntity {

    Entity getBukkitEntity();

    int getFireTicks();

    int bridge$getBurningDuration();

}
