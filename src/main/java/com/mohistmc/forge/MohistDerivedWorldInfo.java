package com.mohistmc.forge;

import com.mojang.serialization.Lifecycle;
import java.util.UUID;
import net.minecraft.command.TimerCallbackManager;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.IServerWorldInfo;
import net.minecraft.world.storage.ServerWorldInfo;

public class MohistDerivedWorldInfo extends ServerWorldInfo {

    private final DerivedWorldInfo derivedWorldInfo;

    public MohistDerivedWorldInfo(DerivedWorldInfo derivedWorldInfo, WorldSettings p_i232158_1_, DimensionGeneratorSettings p_i232158_2_, Lifecycle p_i232158_3_) {
        super(p_i232158_1_, p_i232158_2_, p_i232158_3_);
        this.derivedWorldInfo = derivedWorldInfo;
    }

    @Override
    public int getXSpawn() {
        return derivedWorldInfo.getXSpawn();
    }

    @Override
    public int getYSpawn() {
        return derivedWorldInfo.getYSpawn();
    }

    @Override
    public int getZSpawn() {
        return derivedWorldInfo.getZSpawn();
    }

    @Override
    public float getSpawnAngle() {
        return derivedWorldInfo.getSpawnAngle();
    }

    @Override
    public long getGameTime() {
        return derivedWorldInfo.getGameTime();
    }

    @Override
    public long getDayTime() {
        return derivedWorldInfo.getDayTime();
    }

    @Override
    public String getLevelName() {
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
    public int getThunderTime() {
        return derivedWorldInfo.getThunderTime();
    }

    @Override
    public boolean isRaining() {
        return derivedWorldInfo.isRaining();
    }

    @Override
    public int getRainTime() {
        return derivedWorldInfo.getRainTime();
    }

    @Override
    public GameType getGameType() {
        return derivedWorldInfo.getGameType();
    }

    @Override
    public void setXSpawn(int x) {
        derivedWorldInfo.setXSpawn(x);
    }

    @Override
    public void setYSpawn(int y) {
        derivedWorldInfo.setYSpawn(y);
    }

    @Override
    public void setZSpawn(int z) {
        derivedWorldInfo.setZSpawn(z);
    }

    @Override
    public void setSpawnAngle(float angle) {
        derivedWorldInfo.setSpawnAngle(angle);
    }

    @Override
    public void setGameTime(long time) {
        derivedWorldInfo.setGameTime(time);
    }

    @Override
    public void setDayTime(long time) {
        derivedWorldInfo.setDayTime(time);
    }

    @Override
    public void setSpawn(BlockPos spawnPoint, float angle) {
        derivedWorldInfo.setSpawn(spawnPoint, angle);
    }

    @Override
    public void setThundering(boolean thunderingIn) {
        derivedWorldInfo.setThundering(thunderingIn);
    }

    @Override
    public void setThunderTime(int time) {
        derivedWorldInfo.setThunderTime(time);
    }

    @Override
    public void setRaining(boolean isRaining) {
        derivedWorldInfo.setRaining(isRaining);
    }

    @Override
    public void setRainTime(int time) {
        derivedWorldInfo.setRainTime(time);
    }

    @Override
    public void setGameType(GameType type) {
        derivedWorldInfo.setGameType(type);
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
    public GameRules getGameRules() {
        return derivedWorldInfo.getGameRules();
    }

    @Override
    public WorldBorder.Serializer getWorldBorder() {
        return derivedWorldInfo.getWorldBorder();
    }

    @Override
    public void setWorldBorder(WorldBorder.Serializer serializer) {
        derivedWorldInfo.setWorldBorder(serializer);
    }

    @Override
    public Difficulty getDifficulty() {
        return derivedWorldInfo.getDifficulty();
    }

    @Override
    public boolean isDifficultyLocked() {
        return derivedWorldInfo.isDifficultyLocked();
    }

    @Override
    public TimerCallbackManager<MinecraftServer> getScheduledEvents() {
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
    public void setWanderingTraderId(UUID id) {
        derivedWorldInfo.setWanderingTraderId(id);
    }

    @Override
    public void fillCrashReportCategory(CrashReportCategory category) {
        derivedWorldInfo.fillCrashReportCategory(category);
    }

    public static MohistDerivedWorldInfo create(DerivedWorldInfo worldInfo) {
        return new MohistDerivedWorldInfo(worldInfo, worldSettings(worldInfo), generatorSettings(worldInfo), lifecycle(worldInfo));
    }

    private static WorldSettings worldSettings(IServerWorldInfo worldInfo) {
        if (worldInfo instanceof ServerWorldInfo) {
            return ((ServerWorldInfo) worldInfo).worldSettings();
        } else {
            return worldSettings(((DerivedWorldInfo) worldInfo).wrapped());
        }
    }

    private static DimensionGeneratorSettings generatorSettings(IServerWorldInfo worldInfo) {
        if (worldInfo instanceof ServerWorldInfo) {
            return ((ServerWorldInfo) worldInfo).worldGenSettings();
        } else {
            return generatorSettings(((DerivedWorldInfo) worldInfo).wrapped());
        }
    }

    private static Lifecycle lifecycle(IServerWorldInfo worldInfo) {
        if (worldInfo instanceof ServerWorldInfo) {
            return ((ServerWorldInfo)worldInfo).worldGenSettingsLifecycle();
        } else {
            return lifecycle(((DerivedWorldInfo) worldInfo).wrapped());
        }
    }
}
