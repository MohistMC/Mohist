package org.bukkit.craftbukkit.util;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.TickList;
import net.minecraft.world.level.EmptyTickList;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class DummyGeneratorAccess implements LevelAccessor {

    public static final LevelAccessor INSTANCE = new DummyGeneratorAccess();

    protected DummyGeneratorAccess() {
    }

    @Override
    public TickList<Block> getBlockTicks() {
        return EmptyTickList.empty();
    }

    @Override
    public TickList<net.minecraft.world.level.material.Fluid> getLiquidTicks() {
        return EmptyTickList.empty();
    }

    @Override
    public LevelData getLevelData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DifficultyInstance getCurrentDifficultyAt(BlockPos p_46800_) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Nullable
    @Override
    public MinecraftServer getServer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ChunkSource getChunkSource() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Random getRandom() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void playSound(@Nullable Player p_46775_, BlockPos p_46776_, SoundEvent p_46777_, SoundSource p_46778_, float p_46779_, float p_46780_) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addParticle(ParticleOptions p_46783_, double p_46784_, double p_46785_, double p_46786_, double p_46787_, double p_46788_, double p_46789_) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void levelEvent(@Nullable Player p_46771_, int p_46772_, BlockPos p_46773_, int p_46774_) {
        // Used by PowderSnowBlock.removeFluid
    }

    @Override
    public void gameEvent(@Nullable Entity p_151549_, GameEvent p_151550_, BlockPos p_151551_) {

    }

    @Override
    public float getShade(Direction p_45522_, boolean p_45523_) {
        return 0;
    }

    @Override
    public LevelLightEngine getLightEngine() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos p_45570_) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BlockState getBlockState(BlockPos p_45571_) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FluidState getFluidState(BlockPos p_45569_) {
        return Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public WorldBorder getWorldBorder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RegistryAccess registryAccess() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Entity> getEntities(@Nullable Entity p_45936_, AABB p_45937_, Predicate<? super Entity> p_45938_) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> p_151464_, AABB p_151465_, Predicate<? super T> p_151466_) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<? extends Player> players() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Nullable
    @Override
    public ChunkAccess getChunk(int p_46823_, int p_46824_, ChunkStatus p_46825_, boolean p_46826_) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getHeight(Heightmap.Types p_46827_, int p_46828_, int p_46829_) {
        return 0;
    }

    @Override
    public int getSkyDarken() {
        return 0;
    }

    @Override
    public BiomeManager getBiomeManager() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Biome getUncachedNoiseBiome(int p_46809_, int p_46810_, int p_46811_) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isClientSide() {
        return false;
    }

    @Override
    public int getSeaLevel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DimensionType dimensionType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isStateAtPosition(BlockPos p_46938_, Predicate<BlockState> p_46939_) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isFluidAtPosition(BlockPos p_151584_, Predicate<FluidState> p_151585_) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setBlock(BlockPos p_46947_, BlockState p_46948_, int p_46949_, int p_46950_) {
        return false;
    }

    @Override
    public boolean removeBlock(BlockPos p_46951_, boolean p_46952_) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean destroyBlock(BlockPos p_46957_, boolean p_46958_, @Nullable Entity p_46959_, int p_46960_) {
        return false; // SPIGOT-6515
    }
}
