package red.mohist.mixin.entity;

import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import red.mohist.extra.entity.ExtraEnderDragonFight;

@Mixin(EnderDragonFight.class)
public class MixinEnderDragonFight implements ExtraEnderDragonFight {

    @Shadow
    @Final
    private ServerWorld world;

    @Override
    public ServerWorld getWorld() {
        return this.world;
    }
}
