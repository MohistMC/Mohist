package red.mohist.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import red.mohist.extra.entity.ExtraServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity implements ExtraServerPlayerEntity {

    public Entity entity;

    @Override
    public Entity getBukkitEntity() {
        return this.entity;
    }
}
