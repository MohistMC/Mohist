package me.jellysquid.mods.lithium.common.poi;

import me.jellysquid.mods.lithium.common.util.Collector;
import net.minecraft.village.PointOfInterest;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.village.PointOfInterestType;

import java.util.function.Predicate;

public interface IExtendedPointOfInterestData {
    boolean get(Predicate<PointOfInterestType> type, PointOfInterestManager.Status status, Collector<PointOfInterest> consumer);
}