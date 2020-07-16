package red.mohist.mixin.block;

import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import red.mohist.extra.block.ExtraBanner;

@Mixin(BannerBlockEntity.class)
public class MixinBanner implements ExtraBanner {

    @Shadow
    private ListTag patternListTag;

    @Shadow
    private DyeColor baseColor;

    @Override
    public ListTag getPatternListTag() {
        return this.patternListTag;
    }
}
