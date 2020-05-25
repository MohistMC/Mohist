/*
 * This file is licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014-2016 Daniel Ennis <http://aikar.co>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package co.aikar.timings;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.generator.InternalChunkGenerator;
import org.bukkit.generator.BlockPopulator;

public class TimedChunkGenerator extends InternalChunkGenerator {
    private final WorldServer world;
    private final InternalChunkGenerator timedGenerator;

    public TimedChunkGenerator(WorldServer worldServer, InternalChunkGenerator gen) {
        world = worldServer;
        timedGenerator = gen;
    }

    @Override
    @Deprecated
    public byte[] generate(org.bukkit.World world, Random random, int x, int z) {
        return timedGenerator.generate(world, random, x, z);
    }

    @Override
    @Deprecated
    public short[][] generateExtBlockSections(org.bukkit.World world, Random random, int x, int z,
                                              BiomeGrid biomes) {
        return timedGenerator.generateExtBlockSections(world, random, x, z, biomes);
    }

    @Override
    @Deprecated
    public byte[][] generateBlockSections(org.bukkit.World world, Random random, int x, int z,
                                          BiomeGrid biomes) {
        return timedGenerator.generateBlockSections(world, random, x, z, biomes);
    }

    @Override
    public ChunkData generateChunkData(org.bukkit.World world, Random random, int x, int z, BiomeGrid biome) {
        return timedGenerator.generateChunkData(world, random, x, z, biome);
    }

    @Override
    public boolean canSpawn(org.bukkit.World world, int x, int z) {
        return timedGenerator.canSpawn(world, x, z);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(org.bukkit.World world) {
        return timedGenerator.getDefaultPopulators(world);
    }

    @Override
    public Location getFixedSpawnLocation(org.bukkit.World world, Random random) {
        return timedGenerator.getFixedSpawnLocation(world, random);
    }

    @Override
    public Chunk generateChunk(int i, int j) {
        try (Timing ignored = world.timings.chunkGeneration.startTiming()) {
            return timedGenerator.generateChunk(i, j);
        }
    }

    @Override
    public void populate(int i, int j) {
        try (Timing ignored = world.timings.syncChunkLoadStructuresTimer.startTiming()) {
            timedGenerator.populate(i, j);
        }
    }

    @Override
    public boolean generateStructures(Chunk chunk, int i, int j) {
        return timedGenerator.generateStructures(chunk, i, j);
    }

    @Override
    public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType enumcreaturetype, BlockPos blockposition) {
        return timedGenerator.getPossibleCreatures(enumcreaturetype, blockposition);
    }

    @Override
    @Nullable
    public BlockPos getNearestStructurePos(World world, String s, BlockPos blockposition, boolean flag) {
        return timedGenerator.getNearestStructurePos(world, s, blockposition, flag);
    }

    @Override
    public void recreateStructures(Chunk chunk, int i, int j) {
        try (Timing ignored = world.timings.syncChunkLoadStructuresTimer.startTiming()) {
            timedGenerator.recreateStructures(chunk, i, j);
        }
    }

    @Override
    public boolean isInsideStructure(World world, String s, BlockPos blockPosition) {
        return timedGenerator.isInsideStructure(world, s, blockPosition);
    }
}
