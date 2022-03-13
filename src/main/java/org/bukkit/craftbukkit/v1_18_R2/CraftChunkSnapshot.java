package org.bukkit.craftbukkit.v1_18_R2;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.levelgen.Heightmap;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftMagicNumbers;

/**
 * Represents a static, thread-safe snapshot of chunk of blocks
 * Purpose is to allow clean, efficient copy of a chunk data to be made, and then handed off for processing in another thread (e.g. map rendering)
 */
public class CraftChunkSnapshot implements ChunkSnapshot {
    private final int x, z;
    private final int minHeight, maxHeight;
    private final String worldname;
    private final PalettedContainer<BlockState>[] blockids;
    private final byte[][] skylight;
    private final byte[][] emitlight;
    private final boolean[] empty;
    private final Heightmap hmap; // Height map
    private final long captureFulltime;
    private final net.minecraft.core.Registry<net.minecraft.world.level.biome.Biome> biomeRegistry;
    private final PalettedContainer<Holder<net.minecraft.world.level.biome.Biome>>[] biome;

    CraftChunkSnapshot(int x, int z, int minHeight, int maxHeight, String wname, long wtime, PalettedContainer<BlockState>[] sectionBlockIDs, byte[][] sectionSkyLights, byte[][] sectionEmitLights, boolean[] sectionEmpty, Heightmap hmap, net.minecraft.core.Registry<net.minecraft.world.level.biome.Biome> biomeRegistry, PalettedContainer<Holder<net.minecraft.world.level.biome.Biome>>[] biome) {
        this.x = x;
        this.z = z;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.worldname = wname;
        this.captureFulltime = wtime;
        this.blockids = sectionBlockIDs;
        this.skylight = sectionSkyLights;
        this.emitlight = sectionEmitLights;
        this.empty = sectionEmpty;
        this.hmap = hmap;
        this.biomeRegistry = biomeRegistry;
        this.biome = biome;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public String getWorldName() {
        return worldname;
    }

    @Override
    public boolean contains(BlockData block) {
        Preconditions.checkArgument(block != null, "Block cannot be null");

        Predicate<BlockState> nms = Predicates.equalTo(((CraftBlockData) block).getState());
        for (PalettedContainer<BlockState> palette : blockids) {
            if (palette.maybeHas(nms)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Material getBlockType(int x, int y, int z) {
        validateChunkCoordinates(x, y, z);

        return CraftMagicNumbers.getMaterial(blockids[getSectionIndex(y)].get(x, y & 0xF, z).getBlock());
    }

    @Override
    public final BlockData getBlockData(int x, int y, int z) {
        validateChunkCoordinates(x, y, z);

        return CraftBlockData.fromData(blockids[getSectionIndex(y)].get(x, y & 0xF, z));
    }

    @Override
    public final int getData(int x, int y, int z) {
        validateChunkCoordinates(x, y, z);

        return CraftMagicNumbers.toLegacyData(blockids[getSectionIndex(y)].get(x, y & 0xF, z));
    }

    @Override
    public final int getBlockSkyLight(int x, int y, int z) {
        validateChunkCoordinates(x, y, z);

        int off = ((y & 0xF) << 7) | (z << 3) | (x >> 1);
        return (skylight[getSectionIndex(y)][off] >> ((x & 1) << 2)) & 0xF;
    }

    @Override
    public final int getBlockEmittedLight(int x, int y, int z) {
        validateChunkCoordinates(x, y, z);

        int off = ((y & 0xF) << 7) | (z << 3) | (x >> 1);
        return (emitlight[getSectionIndex(y)][off] >> ((x & 1) << 2)) & 0xF;
    }

    @Override
    public final int getHighestBlockYAt(int x, int z) {
        Preconditions.checkState(hmap != null, "ChunkSnapshot created without height map. Please call getSnapshot with includeMaxblocky=true");
        validateChunkCoordinates(x, 0, z);

        return hmap.getFirstAvailable(x, z);
    }

    @Override
    public final Biome getBiome(int x, int z) {
        return getBiome(x, 0, z);
    }

    @Override
    public final Biome getBiome(int x, int y, int z) {
        Preconditions.checkState(biome != null, "ChunkSnapshot created without biome. Please call getSnapshot with includeBiome=true");
        validateChunkCoordinates(x, y, z);

        PalettedContainer<Holder<net.minecraft.world.level.biome.Biome>> biome = this.biome[getSectionIndex(y >> 2)];
        return CraftBlock.biomeBaseToBiome(biomeRegistry, biome.get(x >> 2, (y & 0xF) >> 2, z >> 2));
    }

    @Override
    public final double getRawBiomeTemperature(int x, int z) {
        return getRawBiomeTemperature(x, 0, z);
    }

    @Override
    public final double getRawBiomeTemperature(int x, int y, int z) {
        Preconditions.checkState(biome != null, "ChunkSnapshot created without biome. Please call getSnapshot with includeBiome=true");
        validateChunkCoordinates(x, y, z);

        PalettedContainer<Holder<net.minecraft.world.level.biome.Biome>> biome = this.biome[getSectionIndex(y >> 2)];
        return biome.get(x >> 2, (y & 0xF) >> 2, z >> 2).value().getTemperature(new BlockPos((this.x << 4) | x, y, (this.z << 4) | z));
    }

    @Override
    public final long getCaptureFullTime() {
        return captureFulltime;
    }

    @Override
    public final boolean isSectionEmpty(int sy) {
        return empty[sy];
    }

    private int getSectionIndex(int y) {
        return (y - minHeight) >> 4;
    }

    private void validateChunkCoordinates(int x, int y, int z) {
        CraftChunk.validateChunkCoordinates(minHeight, maxHeight, x, y, z);
    }
}
