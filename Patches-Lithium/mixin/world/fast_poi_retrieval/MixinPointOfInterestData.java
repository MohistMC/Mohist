package me.jellysquid.mods.lithium.mixin.world.fast_poi_retrieval;

import me.jellysquid.mods.lithium.common.poi.IExtendedPointOfInterestData;
import me.jellysquid.mods.lithium.common.util.Collector;
import net.minecraft.village.PointOfInterest;
import net.minecraft.village.PointOfInterestData;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.village.PointOfInterestType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@Mixin(PointOfInterestData.class)
public class MixinPointOfInterestData implements IExtendedPointOfInterestData {
    @Shadow
    @Final
    private Map<PointOfInterestType, Set<PointOfInterest>> field_218257_c;

    @Override
    public boolean get(Predicate<PointOfInterestType> type, PointOfInterestManager.Status status, Collector<PointOfInterest> consumer) {
        for (Map.Entry<PointOfInterestType, Set<PointOfInterest>> entry : this.field_218257_c.entrySet()) {
            if (!type.test(entry.getKey())) {
                continue;
            }

            for (PointOfInterest poi : entry.getValue()) {
                if (!status.func_221035_a().test(poi)) {
                    continue;
                }

                if (!consumer.collect(poi)) {
                    return false;
                }
            }
        }

        return true;
    }
}
