package org.bukkit.craftbukkit.v1_15_R1.util;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.particles.IParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.ITickList;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.lighting.WorldLightManager;
import net.minecraft.world.storage.WorldInfo;

public class DummyGeneratorAccess implements IWorld {

    public static final IWorld INSTANCE = new DummyGeneratorAccess();

    protected DummyGeneratorAccess() {
    }

    @Override
    public long getSeed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ITickList<Block> getPendingBlockTicks() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ITickList<Fluid> getPendingFluidTicks() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public World getWorld() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WorldInfo getWorldInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DifficultyInstance getDifficultyForLocation(BlockPos bp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AbstractChunkProvider getChunkProvider() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Random getRandom() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void notifyNeighbors(BlockPos bp, Block block) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BlockPos getSpawnPoint() {
        return null;
    }

    @Override
    public void playSound(PlayerEntity eh, BlockPos bp, SoundEvent se, SoundCategory sc, float f, float f1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addParticle(IParticleData pp, double d, double d1, double d2, double d3, double d4, double d5) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void playEvent(PlayerEntity eh, int i, BlockPos bp, int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Entity> getEntitiesInAABBexcluding(Entity entity, AxisAlignedBB aabb, Predicate<? super Entity> prdct) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> clazz, AxisAlignedBB aabb, @Nullable Predicate<? super T> filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<? extends PlayerEntity> getPlayers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IChunk getChunk(int i, int i1, ChunkStatus cs, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getHeight(Heightmap.Type type, int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getSkylightSubtracted() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BiomeManager getBiomeManager() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Biome getNoiseBiomeRaw(int p_225604_1_, int p_225604_2_, int p_225604_3_) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRemote() {
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
    public WorldLightManager getLightManager() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TileEntity getTileEntity(BlockPos blockposition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BlockState getBlockState(BlockPos blockposition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IFluidState getFluidState(BlockPos blockposition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WorldBorder getWorldBorder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasBlockState(BlockPos bp, Predicate<BlockState> prdct) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setBlockState(BlockPos blockposition, BlockState iblockdata, int i) {
        return false;
    }

    @Override
    public boolean removeBlock(BlockPos pos, boolean isMoving) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean p_225521_2_, @Nullable Entity entityIn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
