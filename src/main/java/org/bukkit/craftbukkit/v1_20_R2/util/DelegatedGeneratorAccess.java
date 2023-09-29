package org.bukkit.craftbukkit.v1_20_R2.util;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.LevelTickAccess;
import net.minecraft.world.ticks.ScheduledTick;
import net.minecraft.world.ticks.TickPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;

public abstract class DelegatedGeneratorAccess implements WorldGenLevel {

    private WorldGenLevel handle;

    public void setHandle(WorldGenLevel worldAccess) {
        this.handle = worldAccess;
    }

    public WorldGenLevel getHandle() {
        return handle;
    }

    @Override
    public long getSeed() {
        return handle.getSeed();
    }

    @Override
    public void setCurrentlyGenerating(Supplier<String> arg0) {
        handle.setCurrentlyGenerating(arg0);
    }

    @Override
    public boolean ensureCanWrite(BlockPos arg0) {
        return handle.ensureCanWrite(arg0);
    }

    @Override
    public ServerLevel getLevel() {
        return handle.getLevel();
    }

    @Override
    public void addFreshEntityWithPassengers(Entity arg0, CreatureSpawnEvent.SpawnReason arg1) {
        handle.addFreshEntityWithPassengers(arg0, arg1);
    }

    @Override
    public void addFreshEntityWithPassengers(Entity arg0) {
        handle.addFreshEntityWithPassengers(arg0);
    }

    @Override
    public ServerLevel getMinecraftWorld() {
        return handle.getMinecraftWorld();
    }

    @Override
    public DifficultyInstance getCurrentDifficultyAt(BlockPos arg0) {
        return handle.getCurrentDifficultyAt(arg0);
    }

    @Override
    public void neighborShapeChanged(Direction arg0, BlockState arg1, BlockPos arg2, BlockPos arg3, int arg4, int arg5) {
        handle.neighborShapeChanged(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @Override
    public long dayTime() {
        return handle.dayTime();
    }

    @Override
    public LevelData getLevelData() {
        return handle.getLevelData();
    }

    @Override
    public boolean hasChunk(int arg0, int arg1) {
        return handle.hasChunk(arg0, arg1);
    }

    @Override
    public ChunkSource getChunkSource() {
        return handle.getChunkSource();
    }

    @Override
    public void scheduleTick(BlockPos arg0, Block arg1, int arg2, TickPriority arg3) {
        handle.scheduleTick(arg0, arg1, arg2, arg3);
    }

    @Override
    public void scheduleTick(BlockPos arg0, Block arg1, int arg2) {
        handle.scheduleTick(arg0, arg1, arg2);
    }

    @Override
    public void scheduleTick(BlockPos arg0, Fluid arg1, int arg2, TickPriority arg3) {
        handle.scheduleTick(arg0, arg1, arg2, arg3);
    }

    @Override
    public void scheduleTick(BlockPos arg0, Fluid arg1, int arg2) {
        handle.scheduleTick(arg0, arg1, arg2);
    }

    @Override
    public Difficulty getDifficulty() {
        return handle.getDifficulty();
    }

    @Override
    public void blockUpdated(BlockPos arg0, Block arg1) {
        handle.blockUpdated(arg0, arg1);
    }

    @Override
    public MinecraftServer getServer() {
        return handle.getServer();
    }

    @Override
    public RandomSource getRandom() {
        return handle.getRandom();
    }

    @Override
    public LevelTickAccess<Block> getBlockTicks() {
        return handle.getBlockTicks();
    }

    @Override
    public long nextSubTickCount() {
        return handle.nextSubTickCount();
    }

    @Override
    public <T> ScheduledTick<T> createTick(BlockPos arg0, T arg1, int arg2) {
        return handle.createTick(arg0, arg1, arg2);
    }

    @Override
    public <T> ScheduledTick<T> createTick(BlockPos arg0, T arg1, int arg2, TickPriority arg3) {
        return handle.createTick(arg0, arg1, arg2, arg3);
    }

    @Override
    public LevelTickAccess<Fluid> getFluidTicks() {
        return handle.getFluidTicks();
    }

    @Override
    public void playSound(Player arg0, BlockPos arg1, SoundEvent arg2, SoundSource arg3) {
        handle.playSound(arg0, arg1, arg2, arg3);
    }

    @Override
    public void playSound(Player arg0, BlockPos arg1, SoundEvent arg2, SoundSource arg3, float arg4, float arg5) {
        handle.playSound(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @Override
    public void levelEvent(int arg0, BlockPos arg1, int arg2) {
        handle.levelEvent(arg0, arg1, arg2);
    }

    @Override
    public void levelEvent(Player arg0, int arg1, BlockPos arg2, int arg3) {
        handle.levelEvent(arg0, arg1, arg2, arg3);
    }

    @Override
    public void addParticle(ParticleOptions arg0, double arg1, double arg2, double arg3, double arg4, double arg5, double arg6) {
        handle.addParticle(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    @Override
    public void gameEvent(GameEvent arg0, Vec3 arg1, GameEvent.Context arg2) {
        handle.gameEvent(arg0, arg1, arg2);
    }

    @Override
    public void gameEvent(GameEvent arg0, BlockPos arg1, GameEvent.Context arg2) {
        handle.gameEvent(arg0, arg1, arg2);
    }

    @Override
    public void gameEvent(Entity arg0, GameEvent arg1, BlockPos arg2) {
        handle.gameEvent(arg0, arg1, arg2);
    }

    @Override
    public void gameEvent(Entity arg0, GameEvent arg1, Vec3 arg2) {
        handle.gameEvent(arg0, arg1, arg2);
    }

    @Override
    public List<VoxelShape> getEntityCollisions(Entity arg0, AABB arg1) {
        return handle.getEntityCollisions(arg0, arg1);
    }

    @Override
    public <T extends BlockEntity> Optional<T> getBlockEntity(BlockPos arg0, BlockEntityType<T> arg1) {
        return handle.getBlockEntity(arg0, arg1);
    }

    @Override
    public BlockPos getHeightmapPos(Heightmap.Types arg0, BlockPos arg1) {
        return handle.getHeightmapPos(arg0, arg1);
    }

    @Override
    public boolean isUnobstructed(Entity arg0, VoxelShape arg1) {
        return handle.isUnobstructed(arg0, arg1);
    }

    @Override
    public boolean hasNearbyAlivePlayer(double arg0, double arg1, double arg2, double arg3) {
        return handle.hasNearbyAlivePlayer(arg0, arg1, arg2, arg3);
    }

    @Override
    public List<? extends Player> players() {
        return handle.players();
    }

    @Override
    public List<Entity> getEntities(Entity arg0, AABB arg1, Predicate<? super Entity> arg2) {
        return handle.getEntities(arg0, arg1, arg2);
    }

    @Override
    public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> arg0, AABB arg1, Predicate<? super T> arg2) {
        return handle.getEntities(arg0, arg1, arg2);
    }

    @Override
    public List<Entity> getEntities(Entity arg0, AABB arg1) {
        return handle.getEntities(arg0, arg1);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesOfClass(Class<T> arg0, AABB arg1) {
        return handle.getEntitiesOfClass(arg0, arg1);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesOfClass(Class<T> arg0, AABB arg1, Predicate<? super T> arg2) {
        return handle.getEntitiesOfClass(arg0, arg1, arg2);
    }

    @Override
    public Player getNearestPlayer(TargetingConditions arg0, LivingEntity arg1, double arg2, double arg3, double arg4) {
        return handle.getNearestPlayer(arg0, arg1, arg2, arg3, arg4);
    }

    @Override
    public Player getNearestPlayer(TargetingConditions arg0, double arg1, double arg2, double arg3) {
        return handle.getNearestPlayer(arg0, arg1, arg2, arg3);
    }

    @Override
    public Player getNearestPlayer(Entity arg0, double arg1) {
        return handle.getNearestPlayer(arg0, arg1);
    }

    @Override
    public Player getNearestPlayer(double arg0, double arg1, double arg2, double arg3, Predicate<Entity> arg4) {
        return handle.getNearestPlayer(arg0, arg1, arg2, arg3, arg4);
    }

    @Override
    public Player getNearestPlayer(double arg0, double arg1, double arg2, double arg3, boolean arg4) {
        return handle.getNearestPlayer(arg0, arg1, arg2, arg3, arg4);
    }

    @Override
    public Player getNearestPlayer(TargetingConditions arg0, LivingEntity arg1) {
        return handle.getNearestPlayer(arg0, arg1);
    }

    @Override
    public <T extends LivingEntity> T getNearestEntity(Class<? extends T> arg0, TargetingConditions arg1, LivingEntity arg2, double arg3, double arg4, double arg5, AABB arg6) {
        return handle.getNearestEntity(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    @Override
    public <T extends LivingEntity> T getNearestEntity(List<? extends T> arg0, TargetingConditions arg1, LivingEntity arg2, double arg3, double arg4, double arg5) {
        return handle.getNearestEntity(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @Override
    public Player getPlayerByUUID(UUID arg0) {
        return handle.getPlayerByUUID(arg0);
    }

    @Override
    public List<Player> getNearbyPlayers(TargetingConditions arg0, LivingEntity arg1, AABB arg2) {
        return handle.getNearbyPlayers(arg0, arg1, arg2);
    }

    @Override
    public <T extends LivingEntity> List<T> getNearbyEntities(Class<T> arg0, TargetingConditions arg1, LivingEntity arg2, AABB arg3) {
        return handle.getNearbyEntities(arg0, arg1, arg2, arg3);
    }

    @Override
    @Deprecated
    public float getLightLevelDependentMagicValue(BlockPos arg0) {
        return handle.getLightLevelDependentMagicValue(arg0);
    }

    @Override
    public BlockGetter getChunkForCollisions(int arg0, int arg1) {
        return handle.getChunkForCollisions(arg0, arg1);
    }

    @Override
    public int getMaxLocalRawBrightness(BlockPos arg0) {
        return handle.getMaxLocalRawBrightness(arg0);
    }

    @Override
    public int getMaxLocalRawBrightness(BlockPos arg0, int arg1) {
        return handle.getMaxLocalRawBrightness(arg0, arg1);
    }

    @Override
    public boolean canSeeSkyFromBelowWater(BlockPos arg0) {
        return handle.canSeeSkyFromBelowWater(arg0);
    }

    @Override
    public float getPathfindingCostFromLightLevels(BlockPos arg0) {
        return handle.getPathfindingCostFromLightLevels(arg0);
    }

    @Override
    public Stream<BlockState> getBlockStatesIfLoaded(AABB arg0) {
        return handle.getBlockStatesIfLoaded(arg0);
    }

    @Override
    public Holder<Biome> getUncachedNoiseBiome(int arg0, int arg1, int arg2) {
        return handle.getUncachedNoiseBiome(arg0, arg1, arg2);
    }

    @Override
    @Deprecated
    public int getSeaLevel() {
        return handle.getSeaLevel();
    }

    @Override
    public boolean containsAnyLiquid(AABB arg0) {
        return handle.containsAnyLiquid(arg0);
    }

    @Override
    public int getMinBuildHeight() {
        return handle.getMinBuildHeight();
    }

    @Override
    public boolean isWaterAt(BlockPos arg0) {
        return handle.isWaterAt(arg0);
    }

    @Override
    public boolean isEmptyBlock(BlockPos arg0) {
        return handle.isEmptyBlock(arg0);
    }

    @Override
    public boolean isClientSide() {
        return handle.isClientSide();
    }

    @Override
    public DimensionType dimensionType() {
        return handle.dimensionType();
    }

    @Override
    public FeatureFlagSet enabledFeatures() {
        return handle.enabledFeatures();
    }

    @Override
    @Deprecated
    public boolean hasChunkAt(int arg0, int arg1) {
        return handle.hasChunkAt(arg0, arg1);
    }

    @Override
    @Deprecated
    public boolean hasChunkAt(BlockPos arg0) {
        return handle.hasChunkAt(arg0);
    }

    @Override
    public <T> HolderLookup<T> holderLookup(ResourceKey<? extends Registry<? extends T>> arg0) {
        return handle.holderLookup(arg0);
    }

    @Override
    public RegistryAccess registryAccess() {
        return handle.registryAccess();
    }

    @Override
    public Holder<Biome> getNoiseBiome(int arg0, int arg1, int arg2) {
        return handle.getNoiseBiome(arg0, arg1, arg2);
    }

    @Override
    public int getBlockTint(BlockPos arg0, ColorResolver arg1) {
        return handle.getBlockTint(arg0, arg1);
    }

    @Override
    @Deprecated
    public boolean hasChunksAt(BlockPos arg0, BlockPos arg1) {
        return handle.hasChunksAt(arg0, arg1);
    }

    @Override
    @Deprecated
    public boolean hasChunksAt(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
        return handle.hasChunksAt(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @Override
    @Deprecated
    public boolean hasChunksAt(int arg0, int arg1, int arg2, int arg3) {
        return handle.hasChunksAt(arg0, arg1, arg2, arg3);
    }

    @Override
    public ChunkAccess getChunk(int arg0, int arg1, ChunkStatus arg2, boolean arg3) {
        return handle.getChunk(arg0, arg1, arg2, arg3);
    }

    @Override
    public ChunkAccess getChunk(int arg0, int arg1, ChunkStatus arg2) {
        return handle.getChunk(arg0, arg1, arg2);
    }

    @Override
    public ChunkAccess getChunk(BlockPos arg0) {
        return handle.getChunk(arg0);
    }

    @Override
    public ChunkAccess getChunk(int arg0, int arg1) {
        return handle.getChunk(arg0, arg1);
    }

    @Override
    public int getHeight(Heightmap.Types arg0, int arg1, int arg2) {
        return handle.getHeight(arg0, arg1, arg2);
    }

    @Override
    public int getHeight() {
        return handle.getHeight();
    }

    @Override
    public Holder<Biome> getBiome(BlockPos arg0) {
        return handle.getBiome(arg0);
    }

    @Override
    public int getSkyDarken() {
        return handle.getSkyDarken();
    }

    @Override
    public BiomeManager getBiomeManager() {
        return handle.getBiomeManager();
    }

    @Override
    public boolean canSeeSky(BlockPos arg0) {
        return handle.canSeeSky(arg0);
    }

    @Override
    public int getRawBrightness(BlockPos arg0, int arg1) {
        return handle.getRawBrightness(arg0, arg1);
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return handle.getLightEngine();
    }

    @Override
    public int getBrightness(LightLayer arg0, BlockPos arg1) {
        return handle.getBrightness(arg0, arg1);
    }

    @Override
    public float getShade(Direction arg0, boolean arg1) {
        return handle.getShade(arg0, arg1);
    }

    @Override
    public BlockEntity getBlockEntity(BlockPos arg0) {
        return handle.getBlockEntity(arg0);
    }

    @Override
    public double getBlockFloorHeight(VoxelShape arg0, Supplier<VoxelShape> arg1) {
        return handle.getBlockFloorHeight(arg0, arg1);
    }

    @Override
    public double getBlockFloorHeight(BlockPos arg0) {
        return handle.getBlockFloorHeight(arg0);
    }

    @Override
    public BlockHitResult clipWithInteractionOverride(Vec3 arg0, Vec3 arg1, BlockPos arg2, VoxelShape arg3, BlockState arg4) {
        return handle.clipWithInteractionOverride(arg0, arg1, arg2, arg3, arg4);
    }

    @Override
    public BlockState getBlockState(BlockPos arg0) {
        return handle.getBlockState(arg0);
    }

    @Override
    public FluidState getFluidState(BlockPos arg0) {
        return handle.getFluidState(arg0);
    }

    @Override
    public int getLightEmission(BlockPos arg0) {
        return handle.getLightEmission(arg0);
    }

    @Override
    public BlockHitResult clip(ClipContext arg0) {
        return handle.clip(arg0);
    }

    @Override
    public BlockHitResult clip(ClipContext arg0, BlockPos arg1) {
        return handle.clip(arg0, arg1);
    }

    @Override
    public int getMaxLightLevel() {
        return handle.getMaxLightLevel();
    }

    @Override
    public BlockHitResult isBlockInLine(ClipBlockStateContext arg0) {
        return handle.isBlockInLine(arg0);
    }

    @Override
    public Stream<BlockState> getBlockStates(AABB arg0) {
        return handle.getBlockStates(arg0);
    }

    @Override
    public boolean isOutsideBuildHeight(int arg0) {
        return handle.isOutsideBuildHeight(arg0);
    }

    @Override
    public boolean isOutsideBuildHeight(BlockPos arg0) {
        return handle.isOutsideBuildHeight(arg0);
    }

    @Override
    public int getSectionIndexFromSectionY(int arg0) {
        return handle.getSectionIndexFromSectionY(arg0);
    }

    @Override
    public int getSectionYFromSectionIndex(int arg0) {
        return handle.getSectionYFromSectionIndex(arg0);
    }

    @Override
    public int getMaxSection() {
        return handle.getMaxSection();
    }

    @Override
    public int getMinSection() {
        return handle.getMinSection();
    }

    @Override
    public int getSectionIndex(int arg0) {
        return handle.getSectionIndex(arg0);
    }

    @Override
    public int getSectionsCount() {
        return handle.getSectionsCount();
    }

    @Override
    public int getMaxBuildHeight() {
        return handle.getMaxBuildHeight();
    }

    @Override
    public boolean isUnobstructed(BlockState arg0, BlockPos arg1, CollisionContext arg2) {
        return handle.isUnobstructed(arg0, arg1, arg2);
    }

    @Override
    public boolean isUnobstructed(Entity arg0) {
        return handle.isUnobstructed(arg0);
    }

    @Override
    public WorldBorder getWorldBorder() {
        return handle.getWorldBorder();
    }

    @Override
    public Optional<Vec3> findFreePosition(Entity arg0, VoxelShape arg1, Vec3 arg2, double arg3, double arg4, double arg5) {
        return handle.findFreePosition(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @Override
    public Iterable<VoxelShape> getCollisions(Entity arg0, AABB arg1) {
        return handle.getCollisions(arg0, arg1);
    }

    @Override
    public Iterable<VoxelShape> getBlockCollisions(Entity arg0, AABB arg1) {
        return handle.getBlockCollisions(arg0, arg1);
    }

    @Override
    public boolean noCollision(AABB arg0) {
        return handle.noCollision(arg0);
    }

    @Override
    public boolean noCollision(Entity arg0) {
        return handle.noCollision(arg0);
    }

    @Override
    public boolean noCollision(Entity arg0, AABB arg1) {
        return handle.noCollision(arg0, arg1);
    }

    @Override
    public boolean collidesWithSuffocatingBlock(Entity arg0, AABB arg1) {
        return handle.collidesWithSuffocatingBlock(arg0, arg1);
    }

    @Override
    public Optional<BlockPos> findSupportingBlock(Entity arg0, AABB arg1) {
        return handle.findSupportingBlock(arg0, arg1);
    }

    @Override
    public int getBestNeighborSignal(BlockPos arg0) {
        return handle.getBestNeighborSignal(arg0);
    }

    @Override
    public int getControlInputSignal(BlockPos arg0, Direction arg1, boolean arg2) {
        return handle.getControlInputSignal(arg0, arg1, arg2);
    }

    @Override
    public int getDirectSignal(BlockPos arg0, Direction arg1) {
        return handle.getDirectSignal(arg0, arg1);
    }

    @Override
    public int getDirectSignalTo(BlockPos arg0) {
        return handle.getDirectSignalTo(arg0);
    }

    @Override
    public boolean hasNeighborSignal(BlockPos arg0) {
        return handle.hasNeighborSignal(arg0);
    }

    @Override
    public boolean hasSignal(BlockPos arg0, Direction arg1) {
        return handle.hasSignal(arg0, arg1);
    }

    @Override
    public int getSignal(BlockPos arg0, Direction arg1) {
        return handle.getSignal(arg0, arg1);
    }

    @Override
    public boolean isStateAtPosition(BlockPos arg0, Predicate<BlockState> arg1) {
        return handle.isStateAtPosition(arg0, arg1);
    }

    @Override
    public boolean isFluidAtPosition(BlockPos arg0, Predicate<FluidState> arg1) {
        return handle.isFluidAtPosition(arg0, arg1);
    }

    @Override
    public boolean addFreshEntity(Entity arg0, CreatureSpawnEvent.SpawnReason arg1) {
        return handle.addFreshEntity(arg0, arg1);
    }

    @Override
    public boolean addFreshEntity(Entity arg0) {
        return handle.addFreshEntity(arg0);
    }

    @Override
    public boolean removeBlock(BlockPos arg0, boolean arg1) {
        return handle.removeBlock(arg0, arg1);
    }

    @Override
    public boolean destroyBlock(BlockPos arg0, boolean arg1, Entity arg2, int arg3) {
        return handle.destroyBlock(arg0, arg1, arg2, arg3);
    }

    @Override
    public boolean destroyBlock(BlockPos arg0, boolean arg1, Entity arg2) {
        return handle.destroyBlock(arg0, arg1, arg2);
    }

    @Override
    public boolean destroyBlock(BlockPos arg0, boolean arg1) {
        return handle.destroyBlock(arg0, arg1);
    }

    @Override
    public boolean setBlock(BlockPos arg0, BlockState arg1, int arg2) {
        return handle.setBlock(arg0, arg1, arg2);
    }

    @Override
    public boolean setBlock(BlockPos arg0, BlockState arg1, int arg2, int arg3) {
        return handle.setBlock(arg0, arg1, arg2, arg3);
    }

    @Override
    public float getTimeOfDay(float arg0) {
        return handle.getTimeOfDay(arg0);
    }

    @Override
    public float getMoonBrightness() {
        return handle.getMoonBrightness();
    }

    @Override
    public int getMoonPhase() {
        return handle.getMoonPhase();
    }
}
