package org.bukkit.craftbukkit.util;

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
import net.minecraft.world.level.chunk.status.ChunkStatus;
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
import net.minecraft.world.ticks.TickPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;

public abstract class DelegatedGeneratorAccess implements WorldGenLevel {

    private WorldGenLevel handle;

    public void setHandle(WorldGenLevel worldAccess) {
        this.handle = worldAccess;
    }

    public WorldGenLevel getHandle() {
        return this.handle;
    }

    @Override
    public long getSeed() {
        return this.handle.getSeed();
    }

    @Override
    public void setCurrentlyGenerating(Supplier<String> structureName) {
        this.handle.setCurrentlyGenerating(structureName);
    }

    @Override
    public boolean ensureCanWrite(BlockPos pos) {
        return this.handle.ensureCanWrite(pos);
    }

    @Override
    public ServerLevel getLevel() {
        return this.handle.getLevel();
    }

    @Override
    public void addFreshEntityWithPassengers(Entity arg0, CreatureSpawnEvent.SpawnReason arg1) {
        this.handle.addFreshEntityWithPassengers(arg0, arg1);
    }

    @Override
    public void addFreshEntityWithPassengers(Entity entity) {
        this.handle.addFreshEntityWithPassengers(entity);
    }

    @Override
    public ServerLevel getMinecraftWorld() {
        return this.handle.getMinecraftWorld();
    }

    @Override
    public DifficultyInstance getCurrentDifficultyAt(BlockPos pos) {
        return this.handle.getCurrentDifficultyAt(pos);
    }

    @Override
    public void neighborShapeChanged(Direction direction, BlockState neighborState, BlockPos pos, BlockPos neighborPos, int flags, int maxUpdateDepth) {
        this.handle.neighborShapeChanged(direction, neighborState, pos, neighborPos, flags, maxUpdateDepth);
    }

    @Override
    public long dayTime() {
        return this.handle.dayTime();
    }

    @Override
    public LevelData getLevelData() {
        return this.handle.getLevelData();
    }

    @Override
    public boolean hasChunk(int chunkX, int chunkZ) {
        return this.handle.hasChunk(chunkX, chunkZ);
    }

    @Override
    public ChunkSource getChunkSource() {
        return this.handle.getChunkSource();
    }

    @Override
    public void scheduleTick(BlockPos pos, Block block, int delay, TickPriority priority) {
        this.handle.scheduleTick(pos, block, delay, priority);
    }

    @Override
    public void scheduleTick(BlockPos pos, Block block, int delay) {
        this.handle.scheduleTick(pos, block, delay);
    }

    @Override
    public void scheduleTick(BlockPos pos, Fluid fluid, int delay, TickPriority priority) {
        this.handle.scheduleTick(pos, fluid, delay, priority);
    }

    @Override
    public void scheduleTick(BlockPos pos, Fluid fluid, int delay) {
        this.handle.scheduleTick(pos, fluid, delay);
    }

    @Override
    public Difficulty getDifficulty() {
        return this.handle.getDifficulty();
    }

    @Override
    public void blockUpdated(BlockPos pos, Block block) {
        this.handle.blockUpdated(pos, block);
    }

    @Override
    public MinecraftServer getServer() {
        return this.handle.getServer();
    }

    @Override
    public RandomSource getRandom() {
        return this.handle.getRandom();
    }

    @Override
    public LevelTickAccess<Block> getBlockTicks() {
        return this.handle.getBlockTicks();
    }

    @Override
    public long nextSubTickCount() {
        return this.handle.nextSubTickCount();
    }

    @Override
    public LevelTickAccess<Fluid> getFluidTicks() {
        return this.handle.getFluidTicks();
    }

    @Override
    public void playSound(Player except, BlockPos pos, SoundEvent sound, SoundSource category) {
        this.handle.playSound(except, pos, sound, category);
    }

    @Override
    public void playSound(Player source, BlockPos pos, SoundEvent sound, SoundSource category, float volume, float pitch) {
        this.handle.playSound(source, pos, sound, category, volume, pitch);
    }

    @Override
    public void levelEvent(int eventId, BlockPos pos, int data) {
        this.handle.levelEvent(eventId, pos, data);
    }

    @Override
    public void levelEvent(Player player, int eventId, BlockPos pos, int data) {
        this.handle.levelEvent(player, eventId, pos, data);
    }

    @Override
    public void addParticle(ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        this.handle.addParticle(parameters, x, y, z, velocityX, velocityY, velocityZ);
    }

    @Override
    public void gameEvent(Holder<GameEvent> event, Vec3 emitterPos, GameEvent.Context emitter) {
        this.handle.gameEvent(event, emitterPos, emitter);
    }

    @Override
    public void gameEvent(Holder<GameEvent> event, BlockPos pos, GameEvent.Context emitter) {
        this.handle.gameEvent(event, pos, emitter);
    }

    @Override
    public void gameEvent(Entity entity, Holder<GameEvent> event, BlockPos pos) {
        this.handle.gameEvent(entity, event, pos);
    }

    @Override
    public void gameEvent(Entity entity, Holder<GameEvent> event, Vec3 pos) {
        this.handle.gameEvent(entity, event, pos);
    }

    @Override
    public List<VoxelShape> getEntityCollisions(Entity entity, AABB box) {
        return this.handle.getEntityCollisions(entity, box);
    }

    @Override
    public <T extends BlockEntity> Optional<T> getBlockEntity(BlockPos pos, BlockEntityType<T> type) {
        return this.handle.getBlockEntity(pos, type);
    }

    @Override
    public BlockPos getHeightmapPos(Heightmap.Types heightmap, BlockPos pos) {
        return this.handle.getHeightmapPos(heightmap, pos);
    }

    @Override
    public boolean isUnobstructed(Entity except, VoxelShape shape) {
        return this.handle.isUnobstructed(except, shape);
    }

    @Override
    public boolean hasNearbyAlivePlayer(double x, double y, double z, double range) {
        return this.handle.hasNearbyAlivePlayer(x, y, z, range);
    }

    @Override
    public List<? extends Player> players() {
        return this.handle.players();
    }

    @Override
    public List<Entity> getEntities(Entity except, AABB box, Predicate<? super Entity> predicate) {
        return this.handle.getEntities(except, box, predicate);
    }

    @Override
    public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> filter, AABB box, Predicate<? super T> predicate) {
        return this.handle.getEntities(filter, box, predicate);
    }

    @Override
    public List<Entity> getEntities(Entity except, AABB box) {
        return this.handle.getEntities(except, box);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesOfClass(Class<T> entityClass, AABB box) {
        return this.handle.getEntitiesOfClass(entityClass, box);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesOfClass(Class<T> entityClass, AABB box, Predicate<? super T> predicate) {
        return this.handle.getEntitiesOfClass(entityClass, box, predicate);
    }

    @Override
    public Player getNearestPlayer(TargetingConditions targetPredicate, LivingEntity entity, double x, double y, double z) {
        return this.handle.getNearestPlayer(targetPredicate, entity, x, y, z);
    }

    @Override
    public Player getNearestPlayer(TargetingConditions targetPredicate, double x, double y, double z) {
        return this.handle.getNearestPlayer(targetPredicate, x, y, z);
    }

    @Override
    public Player getNearestPlayer(Entity entity, double maxDistance) {
        return this.handle.getNearestPlayer(entity, maxDistance);
    }

    @Override
    public Player getNearestPlayer(double x, double y, double z, double maxDistance, Predicate<Entity> targetPredicate) {
        return this.handle.getNearestPlayer(x, y, z, maxDistance, targetPredicate);
    }

    @Override
    public Player getNearestPlayer(double x, double y, double z, double maxDistance, boolean ignoreCreative) {
        return this.handle.getNearestPlayer(x, y, z, maxDistance, ignoreCreative);
    }

    @Override
    public Player getNearestPlayer(TargetingConditions targetPredicate, LivingEntity entity) {
        return this.handle.getNearestPlayer(targetPredicate, entity);
    }

    @Override
    public <T extends LivingEntity> T getNearestEntity(Class<? extends T> entityClass, TargetingConditions targetPredicate, LivingEntity entity, double x, double y, double z, AABB box) {
        return this.handle.getNearestEntity(entityClass, targetPredicate, entity, x, y, z, box);
    }

    @Override
    public <T extends LivingEntity> T getNearestEntity(List<? extends T> entityList, TargetingConditions targetPredicate, LivingEntity entity, double x, double y, double z) {
        return this.handle.getNearestEntity(entityList, targetPredicate, entity, x, y, z);
    }

    @Override
    public Player getPlayerByUUID(UUID uuid) {
        return this.handle.getPlayerByUUID(uuid);
    }

    @Override
    public List<Player> getNearbyPlayers(TargetingConditions targetPredicate, LivingEntity entity, AABB box) {
        return this.handle.getNearbyPlayers(targetPredicate, entity, box);
    }

    @Override
    public <T extends LivingEntity> List<T> getNearbyEntities(Class<T> entityClass, TargetingConditions targetPredicate, LivingEntity targetingEntity, AABB box) {
        return this.handle.getNearbyEntities(entityClass, targetPredicate, targetingEntity, box);
    }

    @Override
    @Deprecated
    public float getLightLevelDependentMagicValue(BlockPos pos) {
        return this.handle.getLightLevelDependentMagicValue(pos);
    }

    @Override
    public BlockGetter getChunkForCollisions(int chunkX, int chunkZ) {
        return this.handle.getChunkForCollisions(chunkX, chunkZ);
    }

    @Override
    public int getMaxLocalRawBrightness(BlockPos pos) {
        return this.handle.getMaxLocalRawBrightness(pos);
    }

    @Override
    public int getMaxLocalRawBrightness(BlockPos pos, int ambientDarkness) {
        return this.handle.getMaxLocalRawBrightness(pos, ambientDarkness);
    }

    @Override
    public boolean canSeeSkyFromBelowWater(BlockPos pos) {
        return this.handle.canSeeSkyFromBelowWater(pos);
    }

    @Override
    public float getPathfindingCostFromLightLevels(BlockPos pos) {
        return this.handle.getPathfindingCostFromLightLevels(pos);
    }

    @Override
    public Stream<BlockState> getBlockStatesIfLoaded(AABB box) {
        return this.handle.getBlockStatesIfLoaded(box);
    }

    @Override
    public Holder<Biome> getUncachedNoiseBiome(int biomeX, int biomeY, int biomeZ) {
        return this.handle.getUncachedNoiseBiome(biomeX, biomeY, biomeZ);
    }

    @Override
    @Deprecated
    public int getSeaLevel() {
        return this.handle.getSeaLevel();
    }

    @Override
    public boolean containsAnyLiquid(AABB box) {
        return this.handle.containsAnyLiquid(box);
    }

    @Override
    public int getMinBuildHeight() {
        return this.handle.getMinBuildHeight();
    }

    @Override
    public boolean isWaterAt(BlockPos pos) {
        return this.handle.isWaterAt(pos);
    }

    @Override
    public boolean isEmptyBlock(BlockPos pos) {
        return this.handle.isEmptyBlock(pos);
    }

    @Override
    public boolean isClientSide() {
        return this.handle.isClientSide();
    }

    @Override
    public DimensionType dimensionType() {
        return this.handle.dimensionType();
    }

    @Override
    public FeatureFlagSet enabledFeatures() {
        return this.handle.enabledFeatures();
    }

    @Override
    @Deprecated
    public boolean hasChunkAt(int x, int z) {
        return this.handle.hasChunkAt(x, z);
    }

    @Override
    @Deprecated
    public boolean hasChunkAt(BlockPos pos) {
        return this.handle.hasChunkAt(pos);
    }

    @Override
    public <T> HolderLookup<T> holderLookup(ResourceKey<? extends Registry<? extends T>> registryRef) {
        return this.handle.holderLookup(registryRef);
    }

    @Override
    public RegistryAccess registryAccess() {
        return this.handle.registryAccess();
    }

    @Override
    public Holder<Biome> getNoiseBiome(int biomeX, int biomeY, int biomeZ) {
        return this.handle.getNoiseBiome(biomeX, biomeY, biomeZ);
    }

    @Override
    public int getBlockTint(BlockPos pos, ColorResolver colorResolver) {
        return this.handle.getBlockTint(pos, colorResolver);
    }

    @Override
    @Deprecated
    public boolean hasChunksAt(BlockPos min, BlockPos max) {
        return this.handle.hasChunksAt(min, max);
    }

    @Override
    @Deprecated
    public boolean hasChunksAt(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return this.handle.hasChunksAt(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    @Deprecated
    public boolean hasChunksAt(int minX, int minZ, int maxX, int maxZ) {
        return this.handle.hasChunksAt(minX, minZ, maxX, maxZ);
    }

    @Override
    public ChunkAccess getChunk(int chunkX, int chunkZ, ChunkStatus leastStatus, boolean create) {
        return this.handle.getChunk(chunkX, chunkZ, leastStatus, create);
    }

    @Override
    public ChunkAccess getChunk(int chunkX, int chunkZ, ChunkStatus status) {
        return this.handle.getChunk(chunkX, chunkZ, status);
    }

    @Override
    public ChunkAccess getChunk(BlockPos pos) {
        return this.handle.getChunk(pos);
    }

    @Override
    public ChunkAccess getChunk(int chunkX, int chunkZ) {
        return this.handle.getChunk(chunkX, chunkZ);
    }

    @Override
    public int getHeight(Heightmap.Types heightmap, int x, int z) {
        return this.handle.getHeight(heightmap, x, z);
    }

    @Override
    public int getHeight() {
        return this.handle.getHeight();
    }

    @Override
    public Holder<Biome> getBiome(BlockPos pos) {
        return this.handle.getBiome(pos);
    }

    @Override
    public int getSkyDarken() {
        return this.handle.getSkyDarken();
    }

    @Override
    public BiomeManager getBiomeManager() {
        return this.handle.getBiomeManager();
    }

    @Override
    public boolean canSeeSky(BlockPos pos) {
        return this.handle.canSeeSky(pos);
    }

    @Override
    public int getRawBrightness(BlockPos pos, int ambientDarkness) {
        return this.handle.getRawBrightness(pos, ambientDarkness);
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return this.handle.getLightEngine();
    }

    @Override
    public int getBrightness(LightLayer type, BlockPos pos) {
        return this.handle.getBrightness(type, pos);
    }

    @Override
    public float getShade(Direction direction, boolean shaded) {
        return this.handle.getShade(direction, shaded);
    }

    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        return this.handle.getBlockEntity(pos);
    }

    @Override
    public double getBlockFloorHeight(VoxelShape blockCollisionShape, Supplier<VoxelShape> belowBlockCollisionShapeGetter) {
        return this.handle.getBlockFloorHeight(blockCollisionShape, belowBlockCollisionShapeGetter);
    }

    @Override
    public double getBlockFloorHeight(BlockPos pos) {
        return this.handle.getBlockFloorHeight(pos);
    }

    @Override
    public BlockHitResult clipWithInteractionOverride(Vec3 start, Vec3 end, BlockPos pos, VoxelShape shape, BlockState state) {
        return this.handle.clipWithInteractionOverride(start, end, pos, shape, state);
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return this.handle.getBlockState(pos);
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return this.handle.getFluidState(pos);
    }

    @Override
    public int getLightEmission(BlockPos pos) {
        return this.handle.getLightEmission(pos);
    }

    @Override
    public BlockHitResult clip(ClipContext context) {
        return this.handle.clip(context);
    }

    @Override
    public BlockHitResult clip(ClipContext arg0, BlockPos arg1) {
        return this.handle.clip(arg0, arg1);
    }

    @Override
    public int getMaxLightLevel() {
        return this.handle.getMaxLightLevel();
    }

    @Override
    public BlockHitResult isBlockInLine(ClipBlockStateContext context) {
        return this.handle.isBlockInLine(context);
    }

    @Override
    public Stream<BlockState> getBlockStates(AABB box) {
        return this.handle.getBlockStates(box);
    }

    @Override
    public boolean isOutsideBuildHeight(int y) {
        return this.handle.isOutsideBuildHeight(y);
    }

    @Override
    public boolean isOutsideBuildHeight(BlockPos pos) {
        return this.handle.isOutsideBuildHeight(pos);
    }

    @Override
    public int getSectionIndexFromSectionY(int coord) {
        return this.handle.getSectionIndexFromSectionY(coord);
    }

    @Override
    public int getSectionYFromSectionIndex(int index) {
        return this.handle.getSectionYFromSectionIndex(index);
    }

    @Override
    public int getMaxSection() {
        return this.handle.getMaxSection();
    }

    @Override
    public int getMinSection() {
        return this.handle.getMinSection();
    }

    @Override
    public int getSectionIndex(int y) {
        return this.handle.getSectionIndex(y);
    }

    @Override
    public int getSectionsCount() {
        return this.handle.getSectionsCount();
    }

    @Override
    public int getMaxBuildHeight() {
        return this.handle.getMaxBuildHeight();
    }

    @Override
    public boolean isUnobstructed(BlockState state, BlockPos pos, CollisionContext context) {
        return this.handle.isUnobstructed(state, pos, context);
    }

    @Override
    public boolean isUnobstructed(Entity entity) {
        return this.handle.isUnobstructed(entity);
    }

    @Override
    public WorldBorder getWorldBorder() {
        return this.handle.getWorldBorder();
    }

    @Override
    public Optional<Vec3> findFreePosition(Entity entity, VoxelShape shape, Vec3 target, double x, double y, double z) {
        return this.handle.findFreePosition(entity, shape, target, x, y, z);
    }

    @Override
    public Iterable<VoxelShape> getCollisions(Entity entity, AABB box) {
        return this.handle.getCollisions(entity, box);
    }

    @Override
    public Iterable<VoxelShape> getBlockCollisions(Entity entity, AABB box) {
        return this.handle.getBlockCollisions(entity, box);
    }

    @Override
    public boolean noCollision(AABB box) {
        return this.handle.noCollision(box);
    }

    @Override
    public boolean noCollision(Entity entity) {
        return this.handle.noCollision(entity);
    }

    @Override
    public boolean noCollision(Entity entity, AABB box) {
        return this.handle.noCollision(entity, box);
    }

    @Override
    public boolean collidesWithSuffocatingBlock(Entity entity, AABB box) {
        return this.handle.collidesWithSuffocatingBlock(entity, box);
    }

    @Override
    public Optional<BlockPos> findSupportingBlock(Entity entity, AABB box) {
        return this.handle.findSupportingBlock(entity, box);
    }

    @Override
    public int getBestNeighborSignal(BlockPos pos) {
        return this.handle.getBestNeighborSignal(pos);
    }

    @Override
    public int getControlInputSignal(BlockPos pos, Direction direction, boolean onlyFromGate) {
        return this.handle.getControlInputSignal(pos, direction, onlyFromGate);
    }

    @Override
    public int getDirectSignal(BlockPos pos, Direction direction) {
        return this.handle.getDirectSignal(pos, direction);
    }

    @Override
    public int getDirectSignalTo(BlockPos pos) {
        return this.handle.getDirectSignalTo(pos);
    }

    @Override
    public boolean hasNeighborSignal(BlockPos pos) {
        return this.handle.hasNeighborSignal(pos);
    }

    @Override
    public boolean hasSignal(BlockPos pos, Direction direction) {
        return this.handle.hasSignal(pos, direction);
    }

    @Override
    public int getSignal(BlockPos pos, Direction direction) {
        return this.handle.getSignal(pos, direction);
    }

    @Override
    public boolean isStateAtPosition(BlockPos pos, Predicate<BlockState> state) {
        return this.handle.isStateAtPosition(pos, state);
    }

    @Override
    public boolean isFluidAtPosition(BlockPos pos, Predicate<FluidState> state) {
        return this.handle.isFluidAtPosition(pos, state);
    }

    @Override
    public boolean addFreshEntity(Entity arg0, CreatureSpawnEvent.SpawnReason arg1) {
        return this.handle.addFreshEntity(arg0, arg1);
    }

    @Override
    public boolean addFreshEntity(Entity entity) {
        return this.handle.addFreshEntity(entity);
    }

    @Override
    public boolean removeBlock(BlockPos pos, boolean move) {
        return this.handle.removeBlock(pos, move);
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean drop, Entity breakingEntity, int maxUpdateDepth) {
        return this.handle.destroyBlock(pos, drop, breakingEntity, maxUpdateDepth);
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean drop, Entity breakingEntity) {
        return this.handle.destroyBlock(pos, drop, breakingEntity);
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean drop) {
        return this.handle.destroyBlock(pos, drop);
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState state, int flags) {
        return this.handle.setBlock(pos, state, flags);
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState state, int flags, int maxUpdateDepth) {
        return this.handle.setBlock(pos, state, flags, maxUpdateDepth);
    }

    @Override
    public float getTimeOfDay(float tickDelta) {
        return this.handle.getTimeOfDay(tickDelta);
    }

    @Override
    public float getMoonBrightness() {
        return this.handle.getMoonBrightness();
    }

    @Override
    public int getMoonPhase() {
        return this.handle.getMoonPhase();
    }
}
