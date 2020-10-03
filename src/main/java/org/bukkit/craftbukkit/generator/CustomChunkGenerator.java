package org.bukkit.craftbukkit.generator;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.BiomeContainer;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.GenerationStage.Carving;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public class CustomChunkGenerator extends InternalChunkGenerator {

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
            for (int y = 0; y < world.getCBWorld().getMaxHeight(); y += 4) {
                setBiome(x, y, z, bio);
            }
        }

        @Override
        public Biome getBiome(int x, int y, int z) {
            return CraftBlock.biomeBaseToBiome((Registry<net.minecraft.world.biome.Biome>) biome.field_242704_g, biome.getNoiseBiome(x >> 2, y >> 2, z >> 2));
        }

        @Override
        public void setBiome(int x, int y, int z, Biome bio) {
            biome.setBiome(x >> 2, y >> 2, z >> 2, CraftBlock.biomeToBiomeBase((Registry<net.minecraft.world.biome.Biome>) biome.field_242704_g, bio));
        }
    }

    public CustomChunkGenerator(ServerWorld world, net.minecraft.world.gen.ChunkGenerator delegate, ChunkGenerator generator) {
        super(delegate.getBiomeProvider(), delegate.func_235957_b_());

        this.world = world;
        this.delegate = delegate;
        this.generator = generator;
    }

    @Override
    public void func_242706_a(Registry<net.minecraft.world.biome.Biome> p_242706_1_, IChunk p_242706_2_) {
        // Don't allow the server to override any custom biomes that have been set
    }

    @Override
    public BiomeProvider getBiomeProvider() {
        return delegate.getBiomeProvider();
    }

    @Override
    public void func_235953_a_(ISeedReader p_235953_1_, StructureManager p_235953_2_, IChunk p_235953_3_) {
        delegate.func_235953_a_(p_235953_1_, p_235953_2_, p_235953_3_);
    }

    @Override
    public int func_230356_f_() {
        return delegate.func_230356_f_();
    }

    @Override
    public void generateSurface(WorldGenRegion p_225551_1_, IChunk p_225551_2_) {
        // Call the bukkit ChunkGenerator before structure generation so correct biome information is available.
        int x = p_225551_2_.getPos().x;
        int z = p_225551_2_.getPos().z;
        random.setSeed((long) x * 341873128712L + (long) z * 132897987541L);

        // Get default biome data for chunk
        CustomBiomeGrid biomegrid = new CustomBiomeGrid(new BiomeContainer(world.func_241828_r().func_243612_b(Registry.field_239720_u_), p_225551_2_.getPos(), this.getBiomeProvider()));

        ChunkData data;
        if (generator.isParallelCapable()) {
            data = generator.generateChunkData(this.world.getCBWorld(), random, x, z, biomegrid);
        } else {
            synchronized (this) {
                data = generator.generateChunkData(this.world.getCBWorld(), random, x, z, biomegrid);
            }
        }

        Preconditions.checkArgument(data instanceof CraftChunkData, "Plugins must use createChunkData(World) rather than implementing ChunkData: %s", data);
        CraftChunkData craftData = (CraftChunkData) data;
        ChunkSection[] sections = craftData.getRawChunkData();

        ChunkSection[] csect = p_225551_2_.getSections();
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
        ((ChunkPrimer) p_225551_2_).func_225548_a_(biomegrid.biome);

        if (craftData.getTiles() != null) {
            for (BlockPos pos : craftData.getTiles()) {
                int tx = pos.getX();
                int ty = pos.getY();
                int tz = pos.getZ();
                Block block = craftData.getTypeId(tx, ty, tz).getBlock();

                if (block.func_235695_q_()) {
                    TileEntity tile = ((ITileEntityProvider) block).createNewTileEntity(world);
                    p_225551_2_.addTileEntity(new BlockPos((x << 4) + tx, ty, (z << 4) + tz), tile);
                }
            }
        }
    }

    @Override
    public void func_242707_a(DynamicRegistries p_242707_1_, StructureManager p_242707_2_, IChunk p_242707_3_, TemplateManager p_242707_4_, long p_242707_5_) {
        if (generator.shouldGenerateStructures()) {
            // Still need a way of getting the biome of this chunk to pass to createStructures
            // Using default biomes for now.
            delegate.func_242707_a(p_242707_1_, p_242707_2_, p_242707_3_, p_242707_4_, p_242707_5_);
        }

    }

    @Override
    public void func_230350_a_(long p_230350_1_, BiomeManager p_230350_3_, IChunk p_230350_4_, Carving p_230350_5_) {
        if (generator.shouldGenerateCaves()) {
            delegate.func_230350_a_(p_230350_1_, p_230350_3_, p_230350_4_, p_230350_5_);
        }
    }

    @Override
    public void func_230352_b_(IWorld p_230352_1_, StructureManager p_230352_2_, IChunk p_230352_3_) {
		// Disable vanilla generation
    }

    @Override
    public int func_222529_a(int p_222529_1_, int p_222529_2_, Type heightmapType) {
        return delegate.func_222529_a(p_222529_1_, p_222529_2_, heightmapType);
    }

    @Override
    public void func_230351_a_(WorldGenRegion p_230351_1_, StructureManager p_230351_2_) {
        if (generator.shouldGenerateDecorations()) {
            delegate.func_230351_a_(p_230351_1_, p_230351_2_);
        }
    }

    @Override
    public void func_230354_a_(WorldGenRegion p_230354_1_) {
        if (generator.shouldGenerateMobs()) {
            delegate.func_230354_a_(p_230354_1_);
        }
    }

    @Override
    public int getGroundHeight() {
        return delegate.getGroundHeight();
    }

    @Override
    public int func_230355_e_() {
        return delegate.func_230355_e_();
    }

    @Override
    public IBlockReader func_230348_a_(int p_230348_1_, int p_230348_2_) {
        return delegate.func_230348_a_(p_230348_1_, p_230348_2_);
    }

    @Override
    protected Codec<? extends net.minecraft.world.gen.ChunkGenerator> func_230347_a_() {
        throw new UnsupportedOperationException("Cannot serialize CustomChunkGenerator");
    }

    @Override
    public net.minecraft.world.gen.ChunkGenerator func_230349_a_(long p_230349_1_) {
        return null;
    }
}
