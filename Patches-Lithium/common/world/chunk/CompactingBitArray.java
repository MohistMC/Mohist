package me.jellysquid.mods.lithium.common.world.chunk;

import net.minecraft.util.palette.IPalette;

public interface CompactingBitArray {
    /**
     * Copies the data out of this array into a new non-packed array. The returned array contains a copy of this array
     * re-mapped using {@param destPalette}.
     */
    <T> short[] compact(IPalette<T> srcPalette, IPalette<T> destPalette, T def);
}
