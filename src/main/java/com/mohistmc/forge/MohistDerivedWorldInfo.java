package com.mohistmc.forge;

import com.mojang.serialization.Lifecycle;
import java.util.UUID;
import net.minecraft.CrashReportCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.timers.TimerQueue;
import org.jetbrains.annotations.NotNull;

public class MohistDerivedWorldInfo extends PrimaryLevelData {

    private final DerivedLevelData derivedWorldInfo;

    public MohistDerivedWorldInfo(DerivedLevelData derivedWorldInfo, LevelSettings p_78470_, WorldOptions p_78471_, SpecialWorldProperty p_252268_, Lifecycle p_78472_) {
        super(p_78470_, p_78471_, p_252268_, p_78472_);
        this.derivedWorldInfo = derivedWorldInfo;
    }

    public static MohistDerivedWorldInfo create(DerivedLevelData worldInfo) {
        return new MohistDerivedWorldInfo(worldInfo, worldSettings(worldInfo), generatorSettings(worldInfo), null, lifecycle(worldInfo));
    }

    private static LevelSettings worldSettings(ServerLevelData worldInfo) {
        if (worldInfo instanceof PrimaryLevelData) {
            return ((PrimaryLevelData) worldInfo).settings;
        } else {
            return worldSettings(((DerivedLevelData) worldInfo).wrapped);
        }
    }

    private static WorldOptions generatorSettings(ServerLevelData worldInfo) {
        if (worldInfo instanceof PrimaryLevelData) {
            return ((PrimaryLevelData) worldInfo).worldGenOptions();
        } else {
            return generatorSettings(((DerivedLevelData) worldInfo).wrapped);
        }
    }

    private static Lifecycle lifecycle(ServerLevelData worldInfo) {
        if (worldInfo instanceof PrimaryLevelData) {
            return ((PrimaryLevelData) worldInfo).worldGenSettingsLifecycle();
        } else {
            return lifecycle(((DerivedLevelData) worldInfo).wrapped);
        }
    }

    @Override
    public int getXSpawn() {
        return derivedWorldInfo.getXSpawn();
    }

    @Override
    public void setXSpawn(int x) {
        derivedWorldInfo.setXSpawn(x);
    }

    @Override
    public int getYSpawn() {
        return derivedWorldInfo.getYSpawn();
    }

    @Override
    public void setYSpawn(int y) {
        derivedWorldInfo.setYSpawn(y);
    }

    @Override
    public int getZSpawn() {
        return derivedWorldInfo.getZSpawn();
    }

    @Override
    public void setZSpawn(int z) {
        derivedWorldInfo.setZSpawn(z);
    }

    @Override
    public float getSpawnAngle() {
        return derivedWorldInfo.getSpawnAngle();
    }

    @Override
    public void setSpawnAngle(float angle) {
        derivedWorldInfo.setSpawnAngle(angle);
    }

    @Override
    public long getGameTime() {
        return derivedWorldInfo.getGameTime();
    }

    @Override
    public void setGameTime(long time) {
        derivedWorldInfo.setGameTime(time);
    }

    @Override
    public long getDayTime() {
        return derivedWorldInfo.getDayTime();
    }

    @Override
    public void setDayTime(long time) {
        derivedWorldInfo.setDayTime(time);
    }

    @Override
    public @NotNull String getLevelName() {
        return derivedWorldInfo.getLevelName();
    }

    @Override
    public int getClearWeatherTime() {
        return derivedWorldInfo.getClearWeatherTime();
    }

    @Override
    public void setClearWeatherTime(int time) {
        derivedWorldInfo.setClearWeatherTime(time);
    }

    @Override
    public boolean isThundering() {
        return derivedWorldInfo.isThundering();
    }

    @Override
    public void setThundering(boolean thunderingIn) {
        derivedWorldInfo.setThundering(thunderingIn);
    }

    @Override
    public int getThunderTime() {
        return derivedWorldInfo.getThunderTime();
    }

    @Override
    public void setThunderTime(int time) {
        derivedWorldInfo.setThunderTime(time);
    }

    @Override
    public boolean isRaining() {
        return derivedWorldInfo.isRaining();
    }

    @Override
    public void setRaining(boolean isRaining) {
        derivedWorldInfo.setRaining(isRaining);
    }

    @Override
    public int getRainTime() {
        return derivedWorldInfo.getRainTime();
    }

    @Override
    public void setRainTime(int time) {
        derivedWorldInfo.setRainTime(time);
    }

    @Override
    public @NotNull GameType getGameType() {
        return derivedWorldInfo.getGameType();
    }

    @Override
    public void setGameType(@NotNull GameType type) {
        derivedWorldInfo.setGameType(type);
    }

    @Override
    public void setSpawn(@NotNull BlockPos spawnPoint, float angle) {
        derivedWorldInfo.setSpawn(spawnPoint, angle);
    }

    @Override
    public boolean isHardcore() {
        return derivedWorldInfo.isHardcore();
    }

    @Override
    public boolean getAllowCommands() {
        return derivedWorldInfo.getAllowCommands();
    }

    @Override
    public boolean isInitialized() {
        return derivedWorldInfo.isInitialized();
    }

    @Override
    public void setInitialized(boolean initializedIn) {
        derivedWorldInfo.setInitialized(initializedIn);
    }

    @Override
    public @NotNull GameRules getGameRules() {
        return derivedWorldInfo.getGameRules();
    }

    @Override
    public @NotNull WorldBorder.Settings getWorldBorder() {
        return derivedWorldInfo.getWorldBorder();
    }

    @Override
    public void setWorldBorder(@NotNull WorldBorder.Settings serializer) {
        derivedWorldInfo.setWorldBorder(serializer);
    }

    @Override
    public @NotNull Difficulty getDifficulty() {
        return derivedWorldInfo.getDifficulty();
    }

    @Override
    public boolean isDifficultyLocked() {
        return derivedWorldInfo.isDifficultyLocked();
    }

    @Override
    public @NotNull TimerQueue<MinecraftServer> getScheduledEvents() {
        return derivedWorldInfo.getScheduledEvents();
    }

    @Override
    public int getWanderingTraderSpawnDelay() {
        return derivedWorldInfo.getWanderingTraderSpawnDelay();
    }

    @Override
    public void setWanderingTraderSpawnDelay(int delay) {
        derivedWorldInfo.setWanderingTraderSpawnDelay(delay);
    }

    @Override
    public int getWanderingTraderSpawnChance() {
        return derivedWorldInfo.getWanderingTraderSpawnChance();
    }

    @Override
    public void setWanderingTraderSpawnChance(int chance) {
        derivedWorldInfo.setWanderingTraderSpawnChance(chance);
    }

    @Override
    public void setWanderingTraderId(@NotNull UUID id) {
        derivedWorldInfo.setWanderingTraderId(id);
    }

    @Override
    public void fillCrashReportCategory(@NotNull CrashReportCategory p_164972_, @NotNull LevelHeightAccessor p_164973_) {
        derivedWorldInfo.fillCrashReportCategory(p_164972_, p_164973_);
    }
}
