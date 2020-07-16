package red.mohist.extra;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.collection.TypeFilterableList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

import java.util.Map;

public interface ExtraWorldChunk {

    World getWorld();

    TypeFilterableList<Entity>[] getEntitySections();

    Map<BlockPos, BlockEntity> getBlockEntities();

    Map<Heightmap.Type, Heightmap> getHeightmaps();

}
