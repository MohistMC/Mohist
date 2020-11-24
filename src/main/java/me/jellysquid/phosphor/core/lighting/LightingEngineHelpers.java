package me.jellysquid.phosphor.core.lighting;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class LightingEngineHelpers {
    private static final IBlockState DEFAULT_BLOCK_STATE = Blocks.AIR.getDefaultState();

    // Avoids some additional logic in Chunk#getBlockState... 0 is always air
    static IBlockState posToState(final BlockPos pos, final Chunk chunk) {
        return posToState(pos, chunk.getBlockStorageArray()[pos.getY() >> 4]);
    }

    static IBlockState posToState(final BlockPos pos, final ExtendedBlockStorage section) {
        final int x = pos.getX();
        final int y = pos.getY();
        final int z = pos.getZ();

        if (section != Chunk.NULL_BLOCK_STORAGE)
        {
            int i = section.getData().getStorage().getAt((y & 15) << 8 | (z & 15) << 4 | x & 15);

            if (i != 0) {
                IBlockState state = section.getData().getPalette().getBlockState(i);

                if (state != null) {
                    return state;
                }
            }
        }

        return DEFAULT_BLOCK_STATE;
    }
}