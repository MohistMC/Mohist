package me.jellysquid.mods.lithium.mixin.avoid_allocations;

import me.jellysquid.mods.lithium.common.world.ExtendedWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(World.class)
public class MixinWorld implements ExtendedWorld {
    @Shadow
    protected int updateLCG;

    /**
     * {@inheritDoc}
     */
    @Override
    public void getRandomPosInChunk(int x, int y, int z, int mask, BlockPos.Mutable out) {
        this.updateLCG = this.updateLCG * 3 + 1013904223;
        int rand = this.updateLCG >> 2;
        out.setPos(x + (rand & 15), y + (rand >> 16 & mask), z + (rand >> 8 & 15));
    }
}
