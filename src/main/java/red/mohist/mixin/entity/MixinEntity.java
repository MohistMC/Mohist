package red.mohist.mixin.entity;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import red.mohist.extra.entity.ExtraEntity;

@Mixin(Entity.class)
public class MixinEntity implements ExtraEntity {

    @Shadow
    private int fireTicks;

    @Shadow
    protected int getBurningDuration() {
        return 1;
    }

    @Override
    public Entity getBukkitEntity() {
        return null;
    }

    @Override
    public int getFireTicks() {
        return this.fireTicks;
    }

    @Override
    public int bridge$getBurningDuration() {
        return this.getBurningDuration();
    }
}
