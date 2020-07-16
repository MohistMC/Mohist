package red.mohist.mixin.entity;

import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import red.mohist.extra.entity.ExtraStructureBlockBlockEntity;

@Mixin(StructureBlockBlockEntity.class)
public class MixinStructureBlockBlockEntity implements ExtraStructureBlockBlockEntity {

    @Shadow
    private BlockPos size;

    @Shadow
    private boolean showBoundingBox;

    @Shadow
    private BlockMirror mirror;

    @Override
    public BlockPos getSize() {
        return this.size;
    }

    @Override
    public BlockMirror getMirror() {
        return this.mirror;
    }

    @Override
    public boolean getShowBoundingBox() {
        return this.showBoundingBox;
    }
}
