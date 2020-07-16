package org.bukkit.craftbukkit.generator;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityCategory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeArray;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.CavesChunkGenerator;
import net.minecraft.world.gen.chunk.CavesChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.FloatingIslandsChunkGenerator;
import net.minecraft.world.gen.chunk.FloatingIslandsChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public class CustomChunkGenerator extends InternalChunkGenerator<ChunkGeneratorConfig> {
    private final net.minecraft.world.gen.chunk.ChunkGenerator delegate;
    private final ChunkGenerator generator;
    private final ServerWorld world;
    private final Random random = new Random();

    private class CustomBiomeGrid implements BiomeGrid {

        private final BiomeArray biome; // SPIGOT-5529: stored in 4x4 grid

        public CustomBiomeGrid(BiomeArray biome) {
            this.biome = biome;
        }

        @Override
        public Biome getBiome(int x, int z) {
            return getBiome(x, 0, z);
        }

        @Override
        public void setBiome(int x, int z, Biome bio) {
            for (int y = 0; y < world.getCraftWorld().getMaxHeight(); y += 4) {
                setBiome(x, y, z, bio);
            }
        }

        @Override
        public Biome getBiome(int x, int y, int z) {
            return CraftBlock.biomeBaseToBiome(biome.getBiomeForNoiseGen(x >> 2, y >> 2, z >> 2));
        }

        @Override
        public void setBiome(int x, int y, int z, Biome bio) {
            biome.setBiome(x >> 2, y >> 2, z >> 2, CraftBlock.biomeToBiomeBase(bio));
        }
    }

    public CustomChunkGenerator(World world, ChunkGenerator generator) {
        super(world, world.dimension.createChunkGenerator().getBiomeSource(), new ChunkGeneratorConfig());
        switch (world.getCraftWorld().getEnvironment()) {
            case NORMAL:
                this.delegate = new OverworldChunkGenerator(world, world.dimension.createChunkGenerator().getBiomeSource(), new OverworldChunkGeneratorConfig());
                break;
            case NETHER:
                this.delegate = new CavesChunkGenerator(world, world.dimension.createChunkGenerator().getBiomeSource(), new CavesChunkGeneratorConfig());
                break;
            case THE_END:
                this.delegate = new FloatingIslandsChunkGenerator(world, world.dimension.createChunkGenerator().getBiomeSource(), new FloatingIslandsChunkGeneratorConfig());
                break;
            default:
                throw new AssertionError("Unknown delegate for environment " + world.getCraftWorld().getEnvironment());
        }

        this.world = (ServerWorld) world;
        this.generator = generator;
    }

    @Override
    public void populateBiomes(Chunk ichunkaccess) {
        // Don't allow the server to override any custom biomes that have been set
    }

    @Override
    public <C extends FeatureConfig> C getStructureConfig(net.minecraft.world.biome.Biome biomebase, StructureFeature<C> structuregenerator) {
        return (C) delegate.getStructureConfig(biomebase, structuregenerator);
    }

    @Override
    public BiomeSource getBiomeSource() {
        return delegate.getBiomeSource();
    }

    @Override
    public void addStructureReferences(IWorld generatoraccess, Chunk ichunkaccess) {
        delegate.addStructureReferences(generatoraccess, ichunkaccess);
    }

    @Override
    public int getSeaLevel() {
        return delegate.getSeaLevel();
    }

    @Override
    public void buildSurface(ChunkRegion regionlimitedworldaccess, Chunk ichunkaccess) {
        // Call the bukkit ChunkGenerator before structure generation so correct biome information is available.
        int x = ichunkaccess.getPos().x;
        int z = ichunkaccess.getPos().z;
        random.setSeed((long) x * 341873128712L + (long) z * 132897987541L);

        // Get default biome data for chunk
        CustomBiomeGrid biomegrid = new CustomBiomeGrid(new BiomeArray(ichunkaccess.getPos(), this.getBiomeSource()));

        ChunkData data;
        if (generator.isParallelCapable()) {
            data = generator.generateChunkData(this.world.getCraftWorld(), random, x, z, biomegrid);
        } else {
            synchronized (this) {
                data = generator.generateChunkData(this.world.getCraftWorld(), random, x, z, biomegrid);
            }
        }

        Preconditions.checkArgument(data instanceof CraftChunkData, "Plugins must use createChunkData(World) rather than implementing ChunkData: %s", data);
        CraftChunkData craftData = (CraftChunkData) data;
        ChunkSection[] sections = craftData.getRawChunkData();

        ChunkSection[] csect = ichunkaccess.getSectionArray();
        int scnt = Math.min(csect.length, sections.length);

        // Loop through returned sections
        for (int sec = 0; sec < scnt; sec++) {
            if (sections[sec] == null) {
                continue;
            }
            ChunkSection section = sections[sec];

            csect[sec] = section;
        }

        // Set biome grid
        ((ProtoChunk) ichunkaccess).method_22405(biomegrid.biome);

        if (craftData.getTiles() != null) {
            for (BlockPos pos : craftData.getTiles()) {
                int tx = pos.getX();
                int ty = pos.getY();
                int tz = pos.getZ();
                Block block = craftData.getTypeId(tx, ty, tz).getBlock();

                if (block.hasBlockEntity()) {
                    BlockEntity tile = ((BlockEntityProvider) block).createBlockEntity(world);
                    ichunkaccess.setBlockEntity(new BlockPos((x << 4) + tx, ty, (z << 4) + tz), tile);
                }
            }
        }
    }

    @Override
    public void setStructureStarts(BiomeAccess biomemanager, Chunk ichunkaccess, net.minecraft.world.gen.chunk.ChunkGenerator<?> chunkgenerator, StructureManager definedstructuremanager) {
        if (generator.shouldGenerateStructures()) {
            // Still need a way of getting the biome of this chunk to pass to createStructures
            // Using default biomes for now.
            delegate.setStructureStarts(biomemanager, ichunkaccess, chunkgenerator, definedstructuremanager);
        }
    }

    @Override
    public void carve(BiomeAccess biomemanager, Chunk ichunkaccess, GenerationStep.Carver worldgenstage_features) {
        if (generator.shouldGenerateCaves()) {
            delegate.carve(biomemanager, ichunkaccess, worldgenstage_features);
        }
    }

    @Override
    public void populateNoise(IWorld generatoraccess, Chunk ichunkaccess) {
        // Disable vanilla generation
    }

    @Override
    public int getHeightOnGround(int i, int j, Heightmap.Type heightmap_type) {
        return delegate.getHeightOnGround(i, j, heightmap_type);
    }

    @Override
    public List<net.minecraft.world.biome.Biome.SpawnEntry> getEntitySpawnList(EntityCategory enumcreaturetype, BlockPos blockposition) {
        return delegate.getEntitySpawnList(enumcreaturetype, blockposition);
    }

    @Override
    public void generateFeatures(ChunkRegion regionlimitedworldaccess) {
        if (generator.shouldGenerateDecorations()) {
            delegate.generateFeatures(regionlimitedworldaccess);
        }
    }

    @Override
    public void populateEntities(ChunkRegion regionlimitedworldaccess) {
        if (generator.shouldGenerateMobs()) {
            delegate.populateEntities(regionlimitedworldaccess);
        }
    }

    @Override
    public BlockPos locateStructure(World world, String s, BlockPos blockposition, int i, boolean flag) {
        return delegate.locateStructure(world, s, blockposition, i, flag);
    }

    @Override
    public ChunkGeneratorConfig getConfig() {
        return delegate.getConfig();
    }

    @Override
    public void spawnEntities(ServerWorld worldserver, boolean flag, boolean flag1) {
        delegate.spawnEntities(worldserver, flag, flag1);
    }

    @Override
    public boolean hasStructure(net.minecraft.world.biome.Biome biomebase, StructureFeature<? extends FeatureConfig> structuregenerator) {
        return delegate.hasStructure(biomebase, structuregenerator);
    }

    @Override
    public long getSeed() {
        return delegate.getSeed();
    }

    @Override
    public int getSpawnHeight() {
        return delegate.getSpawnHeight();
    }

    @Override
    public int getMaxY() {
        return delegate.getMaxY();
    }
}
