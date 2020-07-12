package me.jellysquid.mods.lithium.common.shapes;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.CubeCoordinateIterator;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ICollisionReader;
import net.minecraft.world.IEntityReader;
import net.minecraft.world.border.WorldBorder;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class LithiumEntityCollisions {
    /**
     * [VanillaCopy] CollisionView#getBlockCollisions(Entity, Box)
     * This is a much, much faster implementation which uses simple collision testing against full-cube block shapes.
     * Checks against the world border are replaced with our own optimized functions which do not go through the
     * VoxelShape system.
     */
    public static Stream<VoxelShape> getBlockCollisions(ICollisionReader world, final Entity entity, AxisAlignedBB entityBox) {
        int minX = MathHelper.floor(entityBox.minX - 1.0E-7D) - 1;
        int maxX = MathHelper.floor(entityBox.maxX + 1.0E-7D) + 1;
        int minY = MathHelper.floor(entityBox.minY - 1.0E-7D) - 1;
        int maxY = MathHelper.floor(entityBox.maxY + 1.0E-7D) + 1;
        int minZ = MathHelper.floor(entityBox.minZ - 1.0E-7D) - 1;
        int maxZ = MathHelper.floor(entityBox.maxZ + 1.0E-7D) + 1;

        final ISelectionContext context = entity == null ? ISelectionContext.dummy() : ISelectionContext.forEntity(entity);
        final CubeCoordinateIterator cuboidIt = new CubeCoordinateIterator(minX, minY, minZ, maxX, maxY, maxZ);
        final BlockPos.Mutable pos = new BlockPos.Mutable();
        final VoxelShape entityShape = VoxelShapes.create(entityBox);

        return StreamSupport.stream(new Spliterators.AbstractSpliterator<VoxelShape>(Long.MAX_VALUE, Spliterator.NONNULL | Spliterator.IMMUTABLE) {
            boolean skipWorldBorderCheck = entity == null;

            public boolean tryAdvance(Consumer<? super VoxelShape> consumer) {
                if (!this.skipWorldBorderCheck) {
                    this.skipWorldBorderCheck = true;

                    WorldBorder border = world.getWorldBorder();

                    boolean isInsideBorder = LithiumEntityCollisions.isBoxFullyWithinWorldBorder(border, entity.getBoundingBox().shrink(1.0E-7D));
                    boolean isCrossingBorder = LithiumEntityCollisions.isBoxFullyWithinWorldBorder(border, entity.getBoundingBox().grow(1.0E-7D));

                    if (!isInsideBorder && isCrossingBorder) {
                        consumer.accept(border.getShape());

                        return true;
                    }
                }

                while (cuboidIt.hasNext()) {
                    int x = cuboidIt.getX();
                    int y = cuboidIt.getY();
                    int z = cuboidIt.getZ();

                    int edgesHit = cuboidIt.numBoundariesTouched();

                    if (edgesHit == 3) {
                        continue;
                    }

                    IBlockReader chunk = world.getBlockReader(x >> 4, z >> 4);

                    if (chunk == null) {
                        continue;
                    }

                    pos.setPos(x, y, z);

                    BlockState state = chunk.getBlockState(pos);

                    if (edgesHit == 1 && !state.isCollisionShapeLargerThanFullBlock()) {
                        continue;
                    }

                    if (edgesHit == 2 && state.getBlock() != Blocks.MOVING_PISTON) {
                        continue;
                    }

                    VoxelShape blockShape = state.getCollisionShape(world, pos, context);

                    if (blockShape == VoxelShapes.empty()) {
                        continue;
                    }

                    if (blockShape == VoxelShapes.fullCube()) {
                        if (entityBox.intersects(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D)) {
                            consumer.accept(blockShape.withOffset(x, y, z));

                            return true;
                        }
                    } else {
                        VoxelShape shape = blockShape.withOffset(x, y, z);

                        if (VoxelShapes.compare(shape, entityShape, IBooleanFunction.AND)) {
                            consumer.accept(shape);

                            return true;
                        }
                    }
                }

                return false;
            }
        }, false);
    }

    /**
     * This provides a faster check for seeing if an entity is within the world border as it avoids going through
     * the slower shape system.
     * @return True if the {@param box} is fully within the {@param border}, otherwise false.
     */
    public static boolean isBoxFullyWithinWorldBorder(WorldBorder border, AxisAlignedBB box) {
        double wboxMinX = Math.floor(border.minX());
        double wboxMinZ = Math.floor(border.minZ());

        double wboxMaxX = Math.ceil(border.maxX());
        double wboxMaxZ = Math.ceil(border.maxZ());

        return box.minX >= wboxMinX && box.minX < wboxMaxX && box.minZ >= wboxMinZ && box.minZ < wboxMaxZ &&
                box.maxX >= wboxMinX && box.maxX < wboxMaxX && box.maxZ >= wboxMinZ && box.maxZ < wboxMaxZ;
    }

    /**
     * [VanillaCopy] EntityView#getEntityCollisions
     * Re-implements the function named above without stream code or unnecessary allocations. This can provide a small
     * boost in some situations (such as heavy entity crowding) and reduces the allocation rate significantly.
     */
    public static Stream<VoxelShape> getEntityCollisions(IEntityReader view, Entity entity, AxisAlignedBB box, Set<Entity> excluded) {
        if (box.getAverageEdgeLength() < 1.0E-7D) {
            return Stream.empty();
        }

        AxisAlignedBB selection = box.grow(1.0E-7D);

        List<Entity> entities = view.getEntitiesWithinAABBExcludingEntity(entity, selection);
        List<VoxelShape> shapes = new ArrayList<>();

        for (Entity otherEntity : entities) {
            if (!excluded.isEmpty() && excluded.contains(otherEntity)) {
                continue;
            }

            if (entity != null && entity.isRidingSameEntity(otherEntity)) {
                continue;
            }

            AxisAlignedBB otherEntityBox = otherEntity.getCollisionBoundingBox();

            if (otherEntityBox != null && selection.intersects(otherEntityBox)) {
                shapes.add(VoxelShapes.create(otherEntityBox));
            }

            if (entity != null) {
                AxisAlignedBB otherEntityHardBox = entity.getCollisionBox(otherEntity);

                if (otherEntityHardBox != null && selection.intersects(otherEntityHardBox)) {
                    shapes.add(VoxelShapes.create(otherEntityHardBox));
                }
            }
        }

        return shapes.stream();
    }
}