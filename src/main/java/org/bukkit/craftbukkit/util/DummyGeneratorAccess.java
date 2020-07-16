package org.bukkit.craftbukkit.util;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.light.LightingProvider;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.level.LevelProperties;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class DummyGeneratorAccess implements IWorld {

    protected DummyGeneratorAccess() {
    }

    @Override
    public long getSeed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TickScheduler<Block> getBlockTickScheduler() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TickScheduler<Fluid> getFluidTickScheduler() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public World getWorld() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LevelProperties getLevelProperties() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LocalDifficulty getLocalDifficulty(BlockPos bp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ChunkManager getChunkManager() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Random getRandom() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateNeighbors(BlockPos bp, Block block) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BlockPos getSpawnPos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void playSound(PlayerEntity eh, BlockPos bp, SoundEvent se, SoundCategory sc, float f, float f1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addParticle(ParticleEffect pp, double d, double d1, double d2, double d3, double d4, double d5) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void playLevelEvent(PlayerEntity eh, int i, BlockPos bp, int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Entity> getEntities(Entity entity, Box aabb, Predicate<? super Entity> prdct) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends Entity> List<T> getEntities(Class<? extends T> type, Box aabb, Predicate<? super T> prdct) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<? extends PlayerEntity> getPlayers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Chunk getChunk(int i, int i1, ChunkStatus cs, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getTopY(Heightmap.Type type, int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getAmbientDarkness() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BiomeAccess getBiomeAccess() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Biome getGeneratorStoredBiome(int i, int i1, int i2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isClient() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getSeaLevel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Dimension getDimension() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LightingProvider getLightingProvider() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BlockEntity getBlockEntity(BlockPos blockposition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BlockState getBlockState(BlockPos blockposition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FluidState getFluidState(BlockPos blockposition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WorldBorder getWorldBorder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean testBlockState(BlockPos bp, Predicate<BlockState> prdct) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setBlockState(BlockPos blockposition, BlockState iblockdata, int i) {
        return false;
    }

    @Override
    public boolean removeBlock(BlockPos blockposition, boolean flag) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean breakBlock(BlockPos blockposition, boolean flag, Entity entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addEntity(Entity var0, CreatureSpawnEvent.SpawnReason var1) {
        return true;
    }

    @Override
    public BlockHitResult rayTraceBlock(RayTraceContext var0, BlockPos var1) {
        return null;
    }
}
