package me.jellysquid.mods.lithium.mixin.small_tag_arrays;

import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Set;

@Mixin(Tag.class)
public class MixinTag<T> {
    @Shadow
    @Final
    private Set<T> taggedItems;

    private T[] valuesSmallArray;

    @Inject(method = "<init>(Lnet/minecraft/util/ResourceLocation;Ljava/util/Collection;ZZ)V", at = @At("RETURN"))
    private void postConstructed(ResourceLocation id, Collection<Tag.ITagEntry<T>> entries, boolean linked, boolean replace, CallbackInfo ci) {
        if (this.taggedItems.size() < 6) {
            //noinspection unchecked
            this.valuesSmallArray = (T[]) this.taggedItems.toArray();
        }
    }

    /**
     * Makes use of the small values array for quicker indexing if the number of elements is small. This can improve
     * tag matching performance significantly for tags with only one or two objects.
     *
     * @reason Use array scanning when the number of elements is small
     * @author JellySquid
     */
    @Overwrite
    public boolean contains(T obj) {
        if (this.valuesSmallArray != null) {
            for (T other : this.valuesSmallArray) {
                if (other == obj) {
                    return true;
                }
            }

            return false;
        } else {
            return this.taggedItems.contains(obj);
        }
    }

}
