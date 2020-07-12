package me.jellysquid.mods.lithium.mixin.voxelshape.block_shape_cache;

import me.jellysquid.mods.lithium.common.block.BlockStateWithShapeCache;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockState.class)
public class MixinBlockState implements BlockStateWithShapeCache {
    @Shadow
    private BlockState.Cache cache;

    @Override
    public BlockState.Cache bridge$getShapeCache() {
        return this.cache;
    }
}
