package red.mohist.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.collection.TypeFilterableList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import red.mohist.extra.ExtraWorldChunk;

import java.util.Map;

@Mixin(WorldChunk.class)
public class MixinWorldChunk implements ExtraWorldChunk {

    @Shadow
    @Final
    private World world;

    @Shadow
    @Final
    private TypeFilterableList<Entity>[] entitySections;

    @Shadow
    @Final
    private Map<BlockPos, BlockEntity> blockEntities;

    @Shadow
    @Final
    private Map<Heightmap.Type, Heightmap> heightmaps;

    @Override
    public World getWorld(){
        return this.world;
    }

    @Override
    public TypeFilterableList<Entity>[] getEntitySections() {
        return this.entitySections;
    }

    @Override
    public Map<BlockPos, BlockEntity> getBlockEntities() {
        return this.blockEntities;
    }

    @Override
    public Map<Heightmap.Type, Heightmap> getHeightmaps() {
        return this.heightmaps;
    }
}
