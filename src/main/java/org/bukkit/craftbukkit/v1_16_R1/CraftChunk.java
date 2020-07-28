package org.bukkit.craftbukkit.v1_16_R1;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LightType;
import net.minecraft.world.biome.source.BiomeArray;
import net.minecraft.world.chunk.ChunkNibbleArray;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.Palette;
import net.minecraft.world.chunk.PalettedContainer;
import net.minecraft.world.chunk.light.LightingProvider;
import net.minecraft.world.gen.ChunkRandom;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_16_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_16_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_16_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import red.mohist.extra.entity.ExtraEntity;
import red.mohist.extra.world.ExtraWorld;
import red.mohist.extra.world.ExtraWorldChunk;

public class CraftChunk implements Chunk {
    
    private WeakReference<net.minecraft.world.chunk.WorldChunk> weakChunk;
    private final ServerWorld worldServer;
    private final int x;
    private final int z;
    private static final PalettedContainer<net.minecraft.block.BlockState> emptyBlockIDs = new ChunkSection(0).getContainer();
    private static final byte[] emptyLight = new byte[2048];

    public CraftChunk(net.minecraft.world.chunk.WorldChunk chunk) {
        this.weakChunk = new WeakReference<>(chunk);

        worldServer = (ServerWorld) getHandle().getWorld();
        x = getHandle().getPos().x;
        z = getHandle().getPos().z;
    }

    @Override
    public World getWorld() {
        return (World) worldServer.getWorld();
    }

    public CraftWorld getCraftWorld() {
        return (CraftWorld) getWorld();
    }

    public net.minecraft.world.chunk.WorldChunk getHandle() {
        net.minecraft.world.chunk.WorldChunk c = weakChunk.get();

        if (c == null) {
            c = worldServer.getChunk(x, z);
            weakChunk = new WeakReference<>(c);
        }

        return c;
    }

    void breakLink() {
        weakChunk.clear();
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
    public String toString() {
        return "CraftChunk{" + "x=" + getX() + "z=" + getZ() + '}';
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return new CraftBlock(worldServer, new BlockPos((this.x << 4) | x, y, (this.z << 4) | z));
    }

    @Override
    public Entity[] getEntities() {
        if (!isLoaded())
            getWorld().getChunkAt(x, z);
        int count = 0, index = 0;
        net.minecraft.world.chunk.WorldChunk chunk = getHandle();

        for (int i = 0; i < 16; i++)
            count += ((ExtraWorldChunk)(Object)chunk).getEntitySections()[i].size();

        Entity[] entities = new Entity[count];

        for (int i = 0; i < 16; i++) {
            for (Object obj : ((ExtraWorldChunk)(Object)chunk).getEntitySections()[i].toArray()) {
                if (!(obj instanceof net.minecraft.entity.Entity))
                    continue;

                entities[index++] = ((ExtraEntity)(Object)((net.minecraft.entity.Entity) obj)).getBukkitEntity();
            }
        }

        return entities;
    }

    @Override
    public BlockState[] getTileEntities() {
        if (!isLoaded())
            getWorld().getChunkAt(x, z);

        int index = 0;
        net.minecraft.world.chunk.WorldChunk chunk = getHandle();

        BlockState[] entities = new BlockState[chunk.getBlockEntities().size()];

        for (Object obj : chunk.getBlockEntities().keySet().toArray()) {
            if (!(obj instanceof BlockPos))
                continue;

            BlockPos position = (BlockPos) obj;
            entities[index++] = ((ExtraWorld)(Object)worldServer).getCraftWorld().getBlockAt(position.getX(), position.getY(), position.getZ()).getState();
        }

        return entities;
    }

    @Override
    public boolean isLoaded() {
        return getWorld().isChunkLoaded(this);
    }

    @Override
    public boolean load() {
        return getWorld().loadChunk(getX(), getZ(), true);
    }

    @Override
    public boolean load(boolean generate) {
        return getWorld().loadChunk(getX(), getZ(), generate);
    }

    @Override
    public boolean unload() {
        return getWorld().unloadChunk(getX(), getZ());
    }

    @Override
    public boolean isSlimeChunk() {
        return ChunkRandom.getSlimeRandom(getX(), getZ(), getWorld().getSeed(), 987234911L).nextInt(10) == 0;
    }

    @Override
    public boolean unload(boolean save) {
        return getWorld().unloadChunk(getX(), getZ(), save);
    }

    @Override
    public boolean isForceLoaded() {
        return getWorld().isChunkForceLoaded(getX(), getZ());
    }

    @Override
    public void setForceLoaded(boolean forced) {
        getWorld().setChunkForceLoaded(getX(), getZ(), forced);
    }

    @Override
    public boolean addPluginChunkTicket(Plugin plugin) {
        return getWorld().addPluginChunkTicket(getX(), getZ(), plugin);
    }

    @Override
    public boolean removePluginChunkTicket(Plugin plugin) {
        return getWorld().removePluginChunkTicket(getX(), getZ(), plugin);
    }

    @Override
    public Collection<Plugin> getPluginChunkTickets() {
        return getWorld().getPluginChunkTickets(getX(), getZ());
    }

    @Override
    public long getInhabitedTime() {
        return getHandle().getInhabitedTime();
    }

    @Override
    public void setInhabitedTime(long ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks cannot be negative");
        getHandle().setInhabitedTime(ticks);
    }

    @Override
    public boolean contains(BlockData block) {
        Preconditions.checkArgument(block != null, "Block cannot be null");

        Predicate<net.minecraft.block.BlockState> nms = Predicates.equalTo(((CraftBlockData) block).getState());
        for (ChunkSection section : getHandle().getSectionArray())
            if (section != null && section.getContainer().method_19526(nms))
                return true;

        return false;
    }

    @Override
    public ChunkSnapshot getChunkSnapshot() {
        return getChunkSnapshot(true, false, false);
    }

    @Override
    public ChunkSnapshot getChunkSnapshot(boolean includeMaxBlockY, boolean includeBiome, boolean includeBiomeTempRain) {
        net.minecraft.world.chunk.WorldChunk chunk = getHandle();

        ChunkSection[] cs = chunk.getSectionArray();
        PalettedContainer[] sectionBlockIDs = new PalettedContainer[cs.length];
        byte[][] sectionSkyLights = new byte[cs.length][];
        byte[][] sectionEmitLights = new byte[cs.length][];
        boolean[] sectionEmpty = new boolean[cs.length];

        for (int i = 0; i < cs.length; i++) {
            if (cs[i] == null) { // Section is empty?
                sectionBlockIDs[i] = emptyBlockIDs;
                sectionSkyLights[i] = emptyLight;
                sectionEmitLights[i] = emptyLight;
                sectionEmpty[i] = true;
            } else { // Not empty
                CompoundTag data = new CompoundTag();
                cs[i].getContainer().write(data, "Palette", "BlockStates");

                PalettedContainer blockids = new PalettedContainer<>(ChunkSection.palette, net.minecraft.block.Block.STATE_IDS, NbtHelper::toBlockState, NbtHelper::fromBlockState, Blocks.AIR.getDefaultState()); // TODO: snapshot whole ChunkSection
                blockids.read(data.getList("Palette", CraftMagicNumbers.NBT.TAG_COMPOUND), data.getLongArray("BlockStates"));

                sectionBlockIDs[i] = blockids;

                LightingProvider lightengine = chunk.world.getChunkManager().getLightingProvider();
                ChunkNibbleArray skyLightArray = lightengine.get(LightType.SKY).getLightArray(ChunkSectionPos.from(x, i, z));
                if (skyLightArray == null)
                    sectionSkyLights[i] = emptyLight;
                else {
                    sectionSkyLights[i] = new byte[2048];
                    System.arraycopy(skyLightArray.asByteArray(), 0, sectionSkyLights[i], 0, 2048);
                }
                ChunkNibbleArray emitLightArray = lightengine.get(LightType.BLOCK).getLightArray(ChunkSectionPos.from(x, i, z));
                if (emitLightArray == null)
                    sectionEmitLights[i] = emptyLight;
                else {
                    sectionEmitLights[i] = new byte[2048];
                    System.arraycopy(emitLightArray.asByteArray(), 0, sectionEmitLights[i], 0, 2048);
                }
            }
        }

        Heightmap hmap = null;

        if (includeMaxBlockY) {
            hmap = new Heightmap(null, Heightmap.Type.MOTION_BLOCKING);
            hmap.setTo(chunk.heightmaps.get(Heightmap.Type.MOTION_BLOCKING).asLongArray());
        }

        BiomeArray biome = null;
        if (includeBiome || includeBiomeTempRain)
            biome = chunk.getBiomeArray().copy();

        World world = getWorld();
        return new CraftChunkSnapshot(getX(), getZ(), world.getName(), world.getFullTime(), sectionBlockIDs, sectionSkyLights, sectionEmitLights, sectionEmpty, hmap, biome);
    }

    @SuppressWarnings("unchecked")
    private static Palette<net.minecraft.block.BlockState> getPallette() {
        try {
            Field f = ChunkSection.class.getDeclaredField("palette");
            f.setAccessible(true);
            return (Palette<net.minecraft.block.BlockState>) f.get(null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ChunkSnapshot getEmptyChunkSnapshot(int x, int z, CraftWorld world, boolean includeBiome, boolean includeBiomeTempRain) {
        BiomeArray biome = null;

        if (includeBiome || includeBiomeTempRain)
            biome = new BiomeArray(new ChunkPos(x, z), ((ServerWorld)world.getHandle()).getChunkManager().getChunkGenerator().getBiomeSource());

        /* Fill with empty data */
        int hSection = world.getMaxHeight() >> 4;
        PalettedContainer[] blockIDs = new PalettedContainer[hSection];
        byte[][] skyLight = new byte[hSection][];
        byte[][] emitLight = new byte[hSection][];
        boolean[] empty = new boolean[hSection];

        for (int i = 0; i < hSection; i++) {
            blockIDs[i] = emptyBlockIDs;
            skyLight[i] = emptyLight;
            emitLight[i] = emptyLight;
            empty[i] = true;
        }

        return new CraftChunkSnapshot(x, z, world.getName(), world.getFullTime(), blockIDs, skyLight, emitLight, empty, new Heightmap(null, Heightmap.Type.MOTION_BLOCKING), biome);
    }

    static void validateChunkCoordinates(int x, int y, int z) {
        Preconditions.checkArgument(0 <= x && x <= 15, "x out of range (expected 0-15, got %s)", x);
        Preconditions.checkArgument(0 <= y && y <= 255, "y out of range (expected 0-255, got %s)", y);
        Preconditions.checkArgument(0 <= z && z <= 15, "z out of range (expected 0-15, got %s)", z);
    }

    static {
        Arrays.fill(emptyLight, (byte) 0xFF);
    }

}