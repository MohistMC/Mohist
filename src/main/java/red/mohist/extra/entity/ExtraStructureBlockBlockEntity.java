package red.mohist.extra.entity;

import net.minecraft.util.BlockMirror;
import net.minecraft.util.math.BlockPos;

public interface ExtraStructureBlockBlockEntity {

    BlockPos getSize();

    BlockMirror getMirror();

    boolean getShowBoundingBox();

}
