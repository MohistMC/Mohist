package me.jellysquid.mods.lithium.mixin.chunk.fast_chunk_serialization;

import me.jellysquid.mods.lithium.common.world.chunk.CompactingBitArray;
import net.minecraft.util.BitArray;
import net.minecraft.util.palette.IPalette;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Extends {@link BitArray} with a special compaction method defined in {@link CompactingBitArray}.
 */
@Mixin(BitArray.class)
public class MixinBitArray implements CompactingBitArray {
    @Shadow
    @Final
    private long[] longArray;

    @Shadow
    @Final
    private int arraySize;

    @Shadow
    @Final
    private int bitsPerEntry;

    @Shadow
    @Final
    private long maxEntryValue;

    @Override
    public <T> short[] compact(IPalette<T> srcPalette, IPalette<T> destPalette, T def) {
        if (this.arraySize >= Short.MAX_VALUE) {
            throw new IllegalStateException("Array too large");
        }

        short[] flattened = new short[this.arraySize];
        short[] unique = new short[this.arraySize];

        int len = this.longArray.length;

        if (len == 0) {
            return flattened;
        }

        int prevWord = 0;

        long word = this.longArray[0];
        long nextWord = (len > 1) ? this.longArray[1] : 0L;

        int bits = 0;
        int i = 0;

        while (i < this.arraySize) {
            int wordIdx = bits >> 6;
            int nextWordIdx = ((bits + this.bitsPerEntry) - 1) >> 6;
            int bitIdx = bits ^ (wordIdx << 6);

            if (wordIdx != prevWord) {
                word = nextWord;
                nextWord = ((wordIdx + 1) < len) ? this.longArray[wordIdx + 1] : 0L;
                prevWord = wordIdx;
            }

            int j;

            if (wordIdx == nextWordIdx) {
                j = (int) ((word >>> bitIdx) & this.maxEntryValue);
            } else {
                j = (int) (((word >>> bitIdx) | (nextWord << (64 - bitIdx))) & this.maxEntryValue);
            }

            if (j != 0) {
                int remappedPalettedId = unique[j];

                if (remappedPalettedId == 0) {
                    T obj = srcPalette.get(j);
                    int id = destPalette.idFor(obj);

                    remappedPalettedId = id + 1;

                    unique[j] = (short) remappedPalettedId;
                }

                flattened[i] = (short) (remappedPalettedId - 1);
            }

            bits += this.bitsPerEntry;
            ++i;
        }

        return flattened;
    }
}
