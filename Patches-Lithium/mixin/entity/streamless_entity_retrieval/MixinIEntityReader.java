package me.jellysquid.mods.lithium.mixin.entity.streamless_entity_retrieval;

import me.jellysquid.mods.lithium.common.shapes.LithiumEntityCollisions;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IEntityReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Set;
import java.util.stream.Stream;

/**
 * Replaces collision testing methods with jumps to our own (faster) entity collision testing code.
 */
@Mixin(IEntityReader.class)
public interface MixinIEntityReader {
    /**
     * @reason Avoid usage of heavy stream code
     * @author JellySquid
     */
    @Overwrite
    default Stream<VoxelShape> getEmptyCollisionShapes(Entity entity, AxisAlignedBB box, Set<Entity> excluded) {
        return LithiumEntityCollisions.getEntityCollisions((IEntityReader) this, entity, box, excluded);
    }
}
