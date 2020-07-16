package red.mohist.mixin.entity;

import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import red.mohist.extra.entity.ExtraBeehiveBlockEntity;

@Mixin(BeehiveBlockEntity.class)
public class MixinBeehiveBlockEntity implements ExtraBeehiveBlockEntity {

    @Shadow
    private BlockPos flowerPos = null;

    @Override
    public BlockPos getFlowerPos() {
        return this.flowerPos;
    }
}
