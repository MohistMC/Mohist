package org.bukkit.craftbukkit.generator;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Random;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.BiomeContainer;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.world.spawner.CatSpawner;
import net.minecraft.world.spawner.PatrolSpawner;
import net.minecraft.world.spawner.PhantomSpawner;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.village.VillageSiege;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.server.ServerWorld;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public class CustomChunkGenerator extends InternalChunkGenerator<GenerationSettings> {
    private final ChunkGenerator generator;
    private final ServerWorld world;
    private final long seed;
    private final Random random;
    private final Structure strongholdGen = Feature.STRONGHOLD;
    private final PhantomSpawner mobSpawnerPhantom = new PhantomSpawner();
    private final PatrolSpawner mobSpawnerPatrol = new PatrolSpawner();
    private final CatSpawner mobSpawnerCat = new CatSpawner();
    private final VillageSiege villageSiege = new VillageSiege();

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
            return CraftBlock.biomeBaseToBiome(biome.getBiome(x>> 2, y >> 2, z >> 2));
        }

        @Override
        public void setBiome(int x, int y, int z, Biome bio) {
            biome.setBiome(x >> 2, y >> 2, z >> 2, CraftBlock.biomeToBiomeBase(bio));
        }
    }

    public CustomChunkGenerator(World world, long seed, ChunkGenerator generator) {
        super(world, world.dimension.createChunkGenerator().getBiomeProvider(), new GenerationSettings());
        this.world = (ServerWorld) world;
        this.generator = generator;
        this.seed = seed;

        this.random = new Random(seed);
    }

    @Override
    public void func_225551_a_(WorldGenRegion regionlimitedworldaccess, IChunk ichunkaccess) {
        int x = ichunkaccess.getPos().x;
        int z = ichunkaccess.getPos().z;
        random.setSeed((long) x * 341873128712L + (long) z * 132897987541L);

        // Get default biome data for chunk
        CustomBiomeGrid biomegrid = new CustomBiomeGrid(new BiomeContainer(ichunkaccess.getPos(), this.getBiomeProvider()));

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
        ((ChunkPrimer) ichunkaccess).a(biomegrid.biome);

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
    public void func_225550_a_(BiomeManager biomemanager, IChunk ichunkaccess, GenerationStage.Carving worldgenstage_features) {
    }

    @Override
    public void makeBase(IWorld generatoraccess, IChunk ichunkaccess) {
    }

    @Override
    public int func_222529_a(int i, int j, Heightmap.Type heightmap_type) {
        return 0;
    }

    @Override
    public List<net.minecraft.world.biome.Biome.SpawnListEntry> getPossibleCreatures(EntityClassification type, BlockPos position) {
        net.minecraft.world.biome.Biome biomebase = world.func_226691_t_(position);

        return biomebase == null ? null : biomebase.getSpawns(type);
    }

    @Override
    public void decorate(WorldGenRegion regionlimitedworldaccess) {
    }

    @Override
    public void spawnMobs(WorldGenRegion regionlimitedworldaccess) {
    }

    @Override
    public BlockPos findNearestStructure(World world, String type, BlockPos position, int i, boolean flag) {
        return "Stronghold".equals(type) && this.strongholdGen != null ? this.strongholdGen.findNearest(world, this, position, i, flag) : null;
    }

    @Override
    public GenerationSettings getSettings() {
        return settings;
    }

    @Override
    public void spawnMobs(ServerWorld worldserver, boolean flag, boolean flag1) {
        if (worldserver.getWorldProvider().isOverworld()) {
            this.mobSpawnerPhantom.a(worldserver, flag, flag1);
            this.mobSpawnerPatrol.a(worldserver, flag, flag1);
            this.mobSpawnerCat.a(worldserver, flag, flag1);
            this.villageSiege.a(worldserver, flag, flag1);
        }
    }

    @Override
    public boolean hasStructure(net.minecraft.world.biome.Biome biomebase, Structure<? extends IFeatureConfig> structuregenerator) {
        return biomebase.a(structuregenerator);
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public int getGroundHeight() {
        return world.getSeaLevel() + 1;
    }

    @Override
    public int getMaxHeight() {
        return world.getHeight();
    }
}
