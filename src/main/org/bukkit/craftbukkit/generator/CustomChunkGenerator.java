package org.bukkit.craftbukkit.generator;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkBiomeContainer;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public class CustomChunkGenerator extends InternalChunkGenerator {

    private final net.minecraft.world.level.chunk.ChunkGenerator delegate;
    private final ChunkGenerator generator;
    private final ServerLevel world;
    private final Random random = new Random();

    private class CustomBiomeGrid implements BiomeGrid {

        private final ChunkBiomeContainer biome; // SPIGOT-5529: stored in 4x4 grid

        public CustomBiomeGrid(ChunkBiomeContainer biome) {
            this.biome = biome;
        }

        @Override
        public Biome getBiome(int x, int z) {
            return getBiome(x, 0, z);
        }

        @Override
        public void setBiome(int x, int z, Biome bio) {
            for (int y = 0; y < world.getWorld().getMaxHeight(); y += 4) {
                setBiome(x, y, z, bio);
            }
        }

        @Override
        public Biome getBiome(int x, int y, int z) {
            return CraftBlock.biomeBaseToBiome((net.minecraft.core.Registry<net.minecraft.world.level.biome.Biome>) biome.biomeRegistry, biome.getNoiseBiome(x >> 2, y >> 2, z >> 2));
        }

        @Override
        public void setBiome(int x, int y, int z, Biome bio) {
            Preconditions.checkArgument(bio != Biome.CUSTOM, "Cannot set the biome to %s", bio);
            biome.setBiome(x >> 2, y >> 2, z >> 2, CraftBlock.biomeToBiomeBase((net.minecraft.core.Registry<net.minecraft.world.level.biome.Biome>) biome.biomeRegistry, bio));
        }
    }

    public CustomChunkGenerator(ServerLevel world, net.minecraft.world.level.chunk.ChunkGenerator delegate, ChunkGenerator generator) {
        super(delegate.getBiomeSource(), delegate.getSettings());

        this.world = world;
        this.delegate = delegate;
        this.generator = generator;
    }

    @Override
    public net.minecraft.world.level.chunk.ChunkGenerator withSeed(long i) {
        return new CustomChunkGenerator(this.world, delegate.withSeed(i), this.generator);
    }

    @Override
    public void createBiomes(net.minecraft.core.Registry<net.minecraft.world.level.biome.Biome> iregistry, ChunkAccess ichunkaccess) {
        // Don't allow the server to override any custom biomes that have been set
    }

    @Override
    public BiomeSource getBiomeSource() {
        return delegate.getBiomeSource();
    }

    @Override
    public void createReferences(WorldGenLevel generatoraccessseed, StructureFeatureManager structuremanager, ChunkAccess ichunkaccess) {
        delegate.createReferences(generatoraccessseed, structuremanager, ichunkaccess);
    }

    @Override
    public int getSeaLevel() {
        return delegate.getSeaLevel();
    }

    @Override
    public void buildSurfaceAndBedrock(WorldGenRegion regionlimitedworldaccess, ChunkAccess ichunkaccess) {
        // Call the bukkit ChunkGenerator before structure generation so correct biome information is available.
        int x = ichunkaccess.getPos().x;
        int z = ichunkaccess.getPos().z;
        random.setSeed((long) x * 341873128712L + (long) z * 132897987541L);

        // Get default biome data for chunk
        CustomBiomeGrid biomegrid = new CustomBiomeGrid(new ChunkBiomeContainer(world.registryAccess().registryOrThrow(net.minecraft.core.Registry.BIOME_REGISTRY), regionlimitedworldaccess, ichunkaccess.getPos(), this.getBiomeSource()));

        ChunkData data;
        if (generator.isParallelCapable()) {
            data = generator.generateChunkData(this.world.getWorld(), random, x, z, biomegrid);
        } else {
            synchronized (this) {
                data = generator.generateChunkData(this.world.getWorld(), random, x, z, biomegrid);
            }
        }

        Preconditions.checkArgument(data instanceof CraftChunkData, "Plugins must use createChunkData(World) rather than implementing ChunkData: %s", data);
        CraftChunkData craftData = (CraftChunkData) data;
        LevelChunkSection[] sections = craftData.getRawChunkData();

        LevelChunkSection[] csect = ichunkaccess.getSections();
        int scnt = Math.min(csect.length, sections.length);

        // Loop through returned sections
        for (int sec = 0; sec < scnt; sec++) {
            if (sections[sec] == null) {
                continue;
            }
            LevelChunkSection section = sections[sec];

            csect[sec] = section;
        }

        // Set biome grid
        ((ProtoChunk) ichunkaccess).setBiomes(biomegrid.biome);

        if (craftData.getTiles() != null) {
            for (BlockPos pos : craftData.getTiles()) {
                int tx = pos.getX();
                int ty = pos.getY();
                int tz = pos.getZ();
                BlockState block = craftData.getTypeId(tx, ty, tz);

                if (block.hasBlockEntity()) {
                    BlockEntity tile = ((EntityBlock) block.getBlock()).newBlockEntity(new BlockPos((x << 4) + tx, ty, (z << 4) + tz), block);
                    ichunkaccess.setBlockEntity(tile);
                }
            }
        }

        // Apply captured light blocks
        for (BlockPos lightPosition : craftData.getLights()) {
            ((ProtoChunk) ichunkaccess).j(new BlockPos((x << 4) + lightPosition.getX(), lightPosition.getY(), (z << 4) + lightPosition.getZ())); // PAIL rename addLightBlock
        }
    }

    @Override
    public void createStructures(RegistryAccess iregistrycustom, StructureFeatureManager structuremanager, ChunkAccess ichunkaccess, StructureManager definedstructuremanager, long i) {
        if (generator.shouldGenerateStructures()) {
            // Still need a way of getting the biome of this chunk to pass to createStructures
            // Using default biomes for now.
            delegate.createStructures(iregistrycustom, structuremanager, ichunkaccess, definedstructuremanager, i);
        }

    }

    @Override
    public void applyCarvers(long i, BiomeManager biomemanager, ChunkAccess ichunkaccess, GenerationStep.Carving worldgenstage_features) {
        if (generator.shouldGenerateCaves()) {
            delegate.applyCarvers(i, biomemanager, ichunkaccess, worldgenstage_features);
        }
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, StructureFeatureManager structuremanager, ChunkAccess ichunkaccess) {
        // Disable vanilla generation
        return CompletableFuture.completedFuture(ichunkaccess);
    }

    @Override
    public int getBaseHeight(int i, int j, Heightmap.Types heightmap_type, LevelHeightAccessor levelheightaccessor) {
       return delegate.getBaseHeight(i, j, heightmap_type, levelheightaccessor);
    }

    @Override
    public void applyBiomeDecoration(WorldGenRegion regionlimitedworldaccess, StructureFeatureManager structuremanager) {
        if (generator.shouldGenerateDecorations()) {
            delegate.applyBiomeDecoration(regionlimitedworldaccess, structuremanager);
        }
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion regionlimitedworldaccess) {
        if (generator.shouldGenerateMobs()) {
            delegate.spawnOriginalMobs(regionlimitedworldaccess);
        }
    }

    @Override
    public int getSpawnHeight(LevelHeightAccessor levelheightaccessor) {
        return delegate.getSpawnHeight(levelheightaccessor);
    }

    @Override
    public int getGenDepth() {
        return delegate.getGenDepth();
    }

    @Override
    public NoiseColumn getBaseColumn(int i, int j, LevelHeightAccessor levelheightaccessor) {
        return delegate.getBaseColumn(i, j, levelheightaccessor);
    }

    @Override
    protected Codec<? extends net.minecraft.world.level.chunk.ChunkGenerator> codec() {
        throw new UnsupportedOperationException("Cannot serialize CustomChunkGenerator");
    }
}
