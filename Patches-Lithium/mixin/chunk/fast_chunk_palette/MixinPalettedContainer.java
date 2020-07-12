package me.jellysquid.mods.lithium.mixin.chunk.fast_chunk_palette;

import me.jellysquid.mods.lithium.common.world.chunk.palette.LithiumPaletteHashMap;
import me.jellysquid.mods.lithium.common.world.chunk.palette.LithiumResizeCallback;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.BitArray;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.palette.IPalette;
import net.minecraft.util.palette.PaletteArray;
import net.minecraft.util.palette.PalettedContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Function;

/**
 * Patches {@link PalettedContainer} to make use of {@link LithiumPaletteHashMap}.
 */
@Mixin(value = PalettedContainer.class, priority = 999)
public abstract class MixinPalettedContainer<T> implements LithiumResizeCallback<T> {
    @Shadow
    private IPalette<T> palette;

    @Shadow
    protected BitArray storage;

    @Shadow
    public abstract void unlock();

    @Shadow
    protected abstract void set(int int_1, T object_1);

    @Shadow
    private int bits;

    @Shadow
    @Final
    private Function<CompoundNBT, T> deserializer;

    @Shadow
    @Final
    private Function<T, CompoundNBT> serializer;

    @Shadow
    @Final
    private ObjectIntIdentityMap<T> registry;

    @Shadow
    @Final
    private IPalette<T> registryPalette;

    @Shadow
    @Final
    private T defaultState;

    @Shadow
    protected abstract T get(int int_1);

    @Shadow
    public abstract void lock();

    @Shadow
    protected abstract void setBits(int bitsIn);

    /**
     * [VanillaCopy] PalettedContainer#onPaletteResized(int, T)
     * TODO: Use ATs to work around needing to re-implement this
     */
    @Override
    public int onLithiumPaletteResized(int size, T obj) {
        this.lock();

        if (size > this.bits) {
            BitArray oldData = this.storage;
            IPalette<T> oldPalette = this.palette;

            this.setBits(size);

            for (int i = 0; i < oldData.size(); ++i) {
                T oldObj = oldPalette.get(oldData.getAt(i));

                if (oldObj != null) {
                    this.set(i, oldObj);
                }
            }
        }

        int ret = this.palette.idFor(obj);

        this.unlock();

        return ret;
    }

    /**
     * TODO: Replace this with something that doesn't overwrite.
     *
     * @reason Replace the hash palette from vanilla with our own and change the threshold for usage to only 3 bits,
     * as our implementation performs better at smaller key ranges.
     * @author JellySquid
     */
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Overwrite(remap = false)
    private void setBits(int size, boolean forceBits) {
        if (size != this.bits) {
            this.bits = size;
            if (this.bits <= 2) {
                this.bits = 2;
                this.palette = new PaletteArray<>(this.registry, this.bits, (PalettedContainer<T>) (Object) this, this.deserializer);
            } else if (this.bits <= 8) {
                this.palette = new LithiumPaletteHashMap<>(this.registry, this.bits, this, this.deserializer, this.serializer);
            } else {
                this.bits = MathHelper.log2DeBruijn(this.registry.size());
                this.palette = this.registryPalette;
                // FORGE: Used during deserialization to fix some network issues
                if (forceBits) {
                    this.bits = size;
                }
            }

            this.palette.idFor(this.defaultState);
            this.storage = new BitArray(this.bits, 4096);
        }
    }

}
