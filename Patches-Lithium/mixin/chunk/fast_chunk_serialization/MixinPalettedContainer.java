package me.jellysquid.mods.lithium.mixin.chunk.fast_chunk_serialization;

import me.jellysquid.mods.lithium.common.world.chunk.CompactingBitArray;
import me.jellysquid.mods.lithium.common.world.chunk.palette.LithiumPaletteHashMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.BitArray;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.palette.PaletteArray;
import net.minecraft.util.palette.PalettedContainer;
import net.minecraft.util.palette.PaletteHashMap;
import net.minecraft.util.palette.IPalette;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

/**
 * Makes a number of patches to {@link PalettedContainer} to improve the performance of chunk serialization. While I/O
 * operations in Minecraft 1.15+ are handled off-thread, NBT serialization is not and happens on the main server thread.
 */
@Mixin(PalettedContainer.class)
public abstract class MixinPalettedContainer<T> {
    @Shadow
    public abstract void lock();

    @Shadow
    public abstract void unlock();

    @Shadow
    protected abstract T get(int index);

    @Shadow
    @Final
    private T defaultState;

    @Shadow
    @Final
    private ObjectIntIdentityMap<T> registry;

    @Shadow
    private int bits;

    @Shadow
    @Final
    private Function<CompoundNBT, T> deserializer;

    @Shadow
    @Final
    private Function<T, CompoundNBT> serializer;

    @Shadow
    protected BitArray storage;

    @Shadow
    private IPalette<T> palette;

    /**
     * This patch incorporates a number of changes to significantly reduce the time needed to serialize.
     * - Iterate over the packed integer array using a specialized consumer instead of a naive for-loop.
     * - Maintain a temporary list of int->int mappings between the working data array and compacted data array. If a
     * mapping doesn't exist yet, create it in the compacted palette. This allows us to avoid the many lookups through
     * the palette and saves considerable time.
     * - If the palette didn't change size after compaction, avoid the step of copying all the data into a new packed
     * integer array and simply use a memcpy to clone the working array (storing it alongside the current working palette.)
     *
     * @reason Optimize serialization
     * @author JellySquid
     */
    @Inject(method = "writeChunkPalette", at = @At("HEAD"), cancellable = true)
    public void write(CompoundNBT tag, String paletteKey, String dataKey, CallbackInfo ci) {
        // We're using a fallback map and it doesn't need compaction!
        if (this.bits > 8) {
            return;
        }

        this.lock();

        LithiumPaletteHashMap<T> compactedPalette = new LithiumPaletteHashMap<>(this.registry, this.bits, null, this.deserializer, this.serializer);
        compactedPalette.idFor(this.defaultState);

        short[] remapped = ((CompactingBitArray) this.storage)
                .compact(this.palette, compactedPalette, this.defaultState);

        int originalIntSize = this.storage.bitsPerEntry();
        int copyIntSize = Math.max(4, MathHelper.log2DeBruijn(compactedPalette.getSize()));

        // If the palette didn't change sizes, there's no reason to copy anything
        if (this.palette instanceof LithiumPaletteHashMap && originalIntSize == copyIntSize) {
            long[] array = this.storage.getBackingLongArray();
            long[] copy = new long[array.length];

            System.arraycopy(array, 0, copy, 0, array.length);

            ListNBT paletteTag = new ListNBT();
            ((LithiumPaletteHashMap<T>) this.palette).toTag(paletteTag);

            tag.put(paletteKey, paletteTag);
            tag.putLongArray(dataKey, copy);
        } else {
            BitArray copy = new BitArray(copyIntSize, 4096);

            for (int i = 0; i < remapped.length; ++i) {
                copy.setAt(i, remapped[i]);
            }

            ListNBT paletteTag = new ListNBT();
            compactedPalette.toTag(paletteTag);

            tag.put(paletteKey, paletteTag);
            tag.putLongArray(dataKey, copy.getBackingLongArray());
        }

        this.unlock();

        ci.cancel();
    }

    /**
     * If we know the palette will contain a fixed number of elements, we can make a significant optimization by counting
     * blocks with a simple array instead of a integer map. Since palettes make no guarantee that they are bounded,
     * we have to try and determine for each implementation type how many elements there are.
     *
     * @author JellySquid
     */
    @Inject(method = "count", at = @At("HEAD"), cancellable = true)
    public void count(PalettedContainer.ICountConsumer<T> consumer, CallbackInfo ci) {
        int size = getPaletteSize(this.palette);

        // We don't know how many items are in the palette, so this optimization cannot be done
        if (size < 0) {
            return;
        }

        int[] counts = new int[size];

        this.storage.getAll(i -> counts[i]++);

        for (int i = 0; i < counts.length; i++) {
            consumer.accept(this.palette.get(i), counts[i]);
        }

        ci.cancel();
    }

    /**
     * Try to determine the number of elements in a palette, otherwise return -1 to indicate that it is unknown.
     */
    private static int getPaletteSize(IPalette<?> palette) {
        if (palette instanceof PaletteHashMap<?>) {
            return ((PaletteHashMap<?>) palette).getPaletteSize();
        } else if (palette instanceof LithiumPaletteHashMap<?>) {
            return ((LithiumPaletteHashMap<?>) palette).getSize();
        } else if (palette instanceof PaletteArray<?>) {
            return ((PaletteArray<?>) palette).getPaletteSize();
        }

        return -1;
    }
}
