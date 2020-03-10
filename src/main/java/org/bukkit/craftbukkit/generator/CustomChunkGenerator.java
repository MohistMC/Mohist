package org.bukkit.craftbukkit.generator;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.EntityClassification;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeContainer;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.EndChunkGenerator;
import net.minecraft.world.gen.EndGenerationSettings;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.NetherChunkGenerator;
import net.minecraft.world.gen.NetherGenSettings;
import net.minecraft.world.gen.OverworldChunkGenerator;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public class CustomChunkGenerator extends InternalChunkGenerator<GenerationSettings> {
    private final net.minecraft.world.gen.ChunkGenerator delegate;
    private final ChunkGenerator generator;
    private final ServerWorld world;
    private final Random random = new Random();

    private class CustomBiomeGrid implements BiomeGrid {

        private final BiomeContainer biome; // SPIGOT-5529: stored in 4x4 grid

        public CustomBiomeGrid(BiomeContainer biome) {
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
            return CraftBlock.biomeBaseToBiome(biome.getNoiseBiome(x >> 2, y >> 2, z >> 2));
        }

        @Override
        public void setBiome(int x, int y, int z, Biome bio) {
            biome.setBiome(x >> 2, y >> 2, z >> 2, CraftBlock.biomeToBiomeBase(bio));
        }
    }

    public CustomChunkGenerator(World world, ChunkGenerator generator) {
        super(world, world.dimension.createChunkGenerator().getBiomeProvider(), new GenerationSettings());
        switch (world.getWorldCB().getEnvironment()) {
            case NORMAL:
                this.delegate = new OverworldChunkGenerator(world, world.dimension.createChunkGenerator().getBiomeProvider(), new OverworldGenSettings());
                break;
            case NETHER:
                this.delegate = new NetherChunkGenerator(world, world.dimension.createChunkGenerator().getBiomeProvider(), new NetherGenSettings());
                break;
            case THE_END:
                this.delegate = new EndChunkGenerator(world, world.dimension.createChunkGenerator().getBiomeProvider(), new EndGenerationSettings());
                break;
            default:
                throw new AssertionError("Unknown delegate for environment " + world.getWorldCB().getEnvironment());
        }

        this.world = (ServerWorld) world;
        this.generator = generator;
    }

    @Override
    public void generateBiomes(IChunk ichunkaccess) {
        // Don't allow the server to override any custom biomes that have been set
    }

    @Override
    public <C extends IFeatureConfig> C getStructureConfig(net.minecraft.world.biome.Biome biomebase, Structure<C> structuregenerator) {
        return (C) delegate.getStructureConfig(biomebase, structuregenerator);
    }

    @Override
    public BiomeProvider getBiomeProvider() {
        return delegate.getBiomeProvider();
    }

    @Override
    public void generateStructureStarts(IWorld generatoraccess, IChunk ichunkaccess) {
        delegate.generateStructureStarts(generatoraccess, ichunkaccess);
    }

    @Override
    public int getSeaLevel() {
        return delegate.getSeaLevel();
    }

    @Override
    public void func_225551_a_(WorldGenRegion regionlimitedworldaccess, IChunk ichunkaccess) {
        // Call the bukkit ChunkGenerator before structure generation so correct biome information is available.
        int x = ichunkaccess.getPos().x;
        int z = ichunkaccess.getPos().z;
        random.setSeed((long) x * 341873128712L + (long) z * 132897987541L);

        // Get default biome data for chunk
        CustomBiomeGrid biomegrid = new CustomBiomeGrid(new BiomeContainer(ichunkaccess.getPos(), this.getBiomeProvider()));

        ChunkData data;
        if (generator.isParallelCapable()) {
            data = generator.generateChunkData(this.world.getWorldCB(), random, x, z, biomegrid);
        } else {
            synchronized (this) {
                data = generator.generateChunkData(this.world.getWorldCB(), random, x, z, biomegrid);
            }
        }

        Preconditions.checkArgument(data instanceof CraftChunkData, "Plugins must use createChunkData(World) rather than implementing ChunkData: %s", data);
        CraftChunkData craftData = (CraftChunkData) data;
        ChunkSection[] sections = craftData.getRawChunkData();

        ChunkSection[] csect = ichunkaccess.getSections();
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
        ((ChunkPrimer) ichunkaccess).func_225548_a_(biomegrid.biome);

        if (craftData.getTiles() != null) {
            for (BlockPos pos : craftData.getTiles()) {
                int tx = pos.getX();
                int ty = pos.getY();
                int tz = pos.getZ();
                Block block = craftData.getTypeId(tx, ty, tz).getBlock();

                if (block.hasTileEntity()) {
                    TileEntity tile = ((ITileEntityProvider) block).createNewTileEntity(world);
                    ichunkaccess.addTileEntity(new BlockPos((x << 4) + tx, ty, (z << 4) + tz), tile);
                }
            }
        }
    }

    @Override
    public void generateStructures(BiomeManager biomemanager, IChunk ichunkaccess, net.minecraft.world.gen.ChunkGenerator<?> chunkgenerator, TemplateManager definedstructuremanager) {
        if (generator.shouldGenerateStructures()) {
            // Still need a way of getting the biome of this chunk to pass to createStructures
            // Using default biomes for now.
            delegate.generateStructures(biomemanager, ichunkaccess, chunkgenerator, definedstructuremanager);
        }
    }

    @Override
    public void func_225550_a_(BiomeManager biomemanager, IChunk ichunkaccess, GenerationStage.Carving worldgenstage_features) {
        if (generator.shouldGenerateCaves()) {
            delegate.func_225550_a_(biomemanager, ichunkaccess, worldgenstage_features);
        }
    }

    @Override
    public void makeBase(IWorld generatoraccess, IChunk ichunkaccess) {
        // Disable vanilla generation
    }

    @Override
    public int func_222529_a(int i, int j, Heightmap.Type heightmap_type) {
        return delegate.func_222529_a(i, j, heightmap_type);
    }

    @Override
    public List<net.minecraft.world.biome.Biome.SpawnListEntry> getPossibleCreatures(EntityClassification enumcreaturetype, BlockPos blockposition) {
        return delegate.getPossibleCreatures(enumcreaturetype, blockposition);
    }

    @Override
    public void decorate(WorldGenRegion regionlimitedworldaccess) {
        if (generator.shouldGenerateDecorations()) {
            delegate.decorate(regionlimitedworldaccess);
        }
    }

    @Override
    public void spawnMobs(WorldGenRegion regionlimitedworldaccess) {
        if (generator.shouldGenerateMobs()) {
            delegate.spawnMobs(regionlimitedworldaccess);
        }
    }

    @Override
    public BlockPos findNearestStructure(World world, String s, BlockPos blockposition, int i, boolean flag) {
        return delegate.findNearestStructure(world, s, blockposition, i, flag);
    }

    @Override
    public GenerationSettings getSettings() {
        return delegate.getSettings();
    }

    @Override
    public void spawnMobs(ServerWorld worldserver, boolean flag, boolean flag1) {
        delegate.spawnMobs(worldserver, flag, flag1);
    }

    @Override
    public boolean hasStructure(net.minecraft.world.biome.Biome biomebase, Structure<? extends IFeatureConfig> structuregenerator) {
        return delegate.hasStructure(biomebase, structuregenerator);
    }

    @Override
    public long getSeed() {
        return delegate.getSeed();
    }

    @Override
    public int getGroundHeight() {
        return delegate.getGroundHeight();
    }

    @Override
    public int getMaxHeight() {
        return delegate.getMaxHeight();
    }
}
