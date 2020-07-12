package me.jellysquid.mods.lithium.mixin.world.fast_poi_retrieval;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.Dynamic;
import me.jellysquid.mods.lithium.common.poi.IExtendedRegionSectionCache;
import me.jellysquid.mods.lithium.common.util.Collector;
import me.jellysquid.mods.lithium.common.poi.PointOfInterestActions;
import net.minecraft.util.datafix.DefaultTypeReferences;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.village.PointOfInterest;
import net.minecraft.village.PointOfInterestData;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.chunk.storage.RegionSectionCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.File;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(PointOfInterestManager.class)
public abstract class MixinPointOfInterestManager extends RegionSectionCache<PointOfInterestData> {
    public MixinPointOfInterestManager(File file, BiFunction<Runnable, Dynamic<?>, PointOfInterestData> deserializer, Function<Runnable, PointOfInterestData> factory, DataFixer fixer, DefaultTypeReferences type) {
        super(file, deserializer, factory, fixer, type);
    }

    /**
     * @reason Retrieve all points of interest in one operation
     * @author JellySquid
     */
    @SuppressWarnings("unchecked")
    @Overwrite
    public Stream<PointOfInterest> func_219137_a(Predicate<PointOfInterestType> predicate, ChunkPos pos, PointOfInterestManager.Status status) {
        return ((IExtendedRegionSectionCache<PointOfInterestData>) this)
                .getWithinChunkColumn(pos.x, pos.z)
                .flatMap((set) -> set.func_218247_a(predicate, status));
    }

    /**
     * @reason Avoid stream-heavy code, use a faster iterator and callback-based approach
     * @author JellySquid
     */
    @Overwrite
    public Optional<BlockPos> func_219163_a(Predicate<PointOfInterestType> predicate, Predicate<BlockPos> posPredicate, PointOfInterestManager.Status status, BlockPos pos, int radius, Random rand) {
        List<PointOfInterest> list = this.getAllWithinCircle(predicate, pos, radius, status);

        Collections.shuffle(list, rand);

        for (PointOfInterest point : list) {
            if (posPredicate.test(point.getPos())) {
                return Optional.of(point.getPos());
            }
        }

        return Optional.empty();
    }

    /**
     * @reason Avoid stream-heavy code, use a faster iterator and callback-based approach
     * @author JellySquid
     */
    @Overwrite
    public Optional<BlockPos> find(Predicate<PointOfInterestType> pointPredicate, Predicate<BlockPos> posPredicate, BlockPos pos, int radius, PointOfInterestManager.Status status) {
        List<PointOfInterest> points = this.getAllWithinCircle(pointPredicate, pos, radius, status);

        BlockPos nearest = null;
        double nearestDistance = Double.POSITIVE_INFINITY;

        for (PointOfInterest point : points) {
            double distance = point.getPos().distanceSq(pos);

            if (distance < nearestDistance) {
                if (posPredicate.test(point.getPos())) {
                    nearest = point.getPos();
                    nearestDistance = distance;
                }
            }
        }

        return Optional.ofNullable(nearest);
    }

    /**
     * @reason Avoid stream-heavy code, use a faster iterator and callback-based approach
     * @author JellySquid
     */
    @Overwrite
    public long getCountInRange(Predicate<PointOfInterestType> predicate, BlockPos pos, int radius, PointOfInterestManager.Status status) {
        return this.getAllWithinCircle(predicate, pos, radius, status).size();
    }

    private List<PointOfInterest> getAllWithinCircle(Predicate<PointOfInterestType> predicate, BlockPos pos, int radius, PointOfInterestManager.Status status) {
        List<PointOfInterest> points = new ArrayList<>();

        this.collectWithinCircle(predicate, pos, radius, status, points::add);

        return points;
    }

    private void collectWithinCircle(Predicate<PointOfInterestType> predicate, BlockPos pos, int radius, PointOfInterestManager.Status status, Collector<PointOfInterest> collector) {
        Collector<PointOfInterest> filter = PointOfInterestActions.collectAllWithinRadius(pos, radius, collector);
        Collector<PointOfInterestData> consumer = PointOfInterestActions.collectAllMatching(predicate, status, filter);

        int minChunkX = (pos.getX() - radius - 1) >> 4;
        int minChunkZ = (pos.getZ() - radius - 1) >> 4;

        int maxChunkX = (pos.getX() + radius + 1) >> 4;
        int maxChunkZ = (pos.getZ() + radius + 1) >> 4;

        // noinspection unchecked
        IExtendedRegionSectionCache<PointOfInterestData> storage = ((IExtendedRegionSectionCache<PointOfInterestData>) this);

        for (int x = minChunkX; x <= maxChunkX; x++) {
            for (int z = minChunkZ; z <= maxChunkZ; z++) {
                if (!storage.collectWithinChunkColumn(x, z, consumer)) {
                    return;
                }
            }
        }
    }
}
