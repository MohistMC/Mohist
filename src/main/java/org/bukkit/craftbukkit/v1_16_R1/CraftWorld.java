package org.bukkit.craftbukkit.v1_16_R1;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.BlockChangeDelegate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameRule;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Raid;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.StructureType;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.DragonBattle;
import org.bukkit.craftbukkit.v1_16_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_16_R1.metadata.BlockMetadataStore;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.world.SpawnChangeEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.level.ServerWorldProperties;
import red.mohist.extra.entity.ExtraEntity;
import red.mohist.extra.player.ExtraServerEntityPlayer;
import red.mohist.utils.UtilsFabric;

public class CraftWorld implements World {

    public static final int CUSTOM_DIMENSION_OFFSET = 10;
    private final BlockMetadataStore blockMetadata = new BlockMetadataStore(this);

    private ServerWorld nms;
    public CraftWorld(ServerWorld world) {
        this.nms = world;
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        Set<String> result = new HashSet<String>();

        for (Player player : getPlayers())
            result.addAll(player.getListeningPluginChannels());

        return result;
    }

    @Override
    public void sendPluginMessage(Plugin plugin, String channel, byte[] message) {
        for (Player player : getPlayers())
            player.sendPluginMessage(plugin, channel, message);
    }

    @Override
    public List<MetadataValue> getMetadata(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasMetadata(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removeMetadata(String arg0, Plugin arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setMetadata(String arg0, MetadataValue arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean addPluginChunkTicket(int arg0, int arg1, Plugin arg2) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canGenerateStructures() {
        // FIXME BROKEN!!!
        return false;//nms.getLevelProperties().hasStructures();
    }

    @Override
    public boolean createExplosion(Location arg0, float arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean createExplosion(Location arg0, float arg1, boolean arg2) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean createExplosion(double arg0, double arg1, double arg2, float arg3) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean createExplosion(Location arg0, float arg1, boolean arg2, boolean arg3) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean createExplosion(double arg0, double arg1, double arg2, float arg3, boolean arg4) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean createExplosion(Location arg0, float arg1, boolean arg2, boolean arg3, Entity arg4) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean createExplosion(double arg0, double arg1, double arg2, float arg3, boolean arg4, boolean arg5) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean createExplosion(double arg0, double arg1, double arg2, float arg3, boolean arg4, boolean arg5, Entity arg6) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Item dropItem(Location arg0, ItemStack arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Item dropItemNaturally(Location arg0, ItemStack arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean generateTree(Location arg0, TreeType arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean generateTree(Location arg0, TreeType arg1, BlockChangeDelegate arg2) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean getAllowAnimals() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean getAllowMonsters() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getAmbientSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getAnimalSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Biome getBiome(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Biome getBiome(int arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Block getBlockAt(Location loc) {
        return getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        return CraftBlock.at(nms, new BlockPos(x, y, z));
    }

    @Override
    public Chunk getChunkAt(Location arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Chunk getChunkAt(Block arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Chunk getChunkAt(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Difficulty getDifficulty() {
        return UtilsFabric.fromFabric(nms.getDifficulty());
    }

    @Override
    public ChunkSnapshot getEmptyChunkSnapshot(int arg0, int arg1, boolean arg2, boolean arg3) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DragonBattle getEnderDragonBattle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Entity> getEntities() {
        List<Entity> list = new ArrayList<Entity>();

        for (Object object : nms.entitiesById.values()) {
            if (object instanceof net.minecraft.entity.Entity) {
                net.minecraft.entity.Entity mc = (net.minecraft.entity.Entity) object;
                Entity bukkit = ((ExtraEntity)mc).getBukkitEntity();

                // Assuming that bukkitEntity isn't null
                if (bukkit != null && bukkit.isValid())
                    list.add(bukkit);
            }
        }

        return list;
    }

    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Entity> getEntitiesByClasses(Class<?>... arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Environment getEnvironment() {
        // TODO Auto-generated method stub
        return Environment.NORMAL;
    }

    @Override
    public Collection<Chunk> getForceLoadedChunks() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getFullTime() {
        return nms.getTimeOfDay();
    }

    @Override
    public <T> T getGameRuleDefault(GameRule<T> arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getGameRuleValue(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T getGameRuleValue(GameRule<T> arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getGameRules() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ChunkGenerator getGenerator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Block getHighestBlockAt(Location arg0) {
        // TODO Auto-generated method stub
        return getHighestBlockAt(arg0.getBlockX(), arg0.getBlockY());
    }

    @Override
    public Block getHighestBlockAt(int x, int z) {
        return getBlockAt(x, getHighestBlockYAt(x, z), z);
    }

    @Override
    public Block getHighestBlockAt(Location arg0, HeightMap arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Block getHighestBlockAt(int arg0, int arg1, HeightMap arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getHighestBlockYAt(Location arg0) {
        return getHighestBlockYAt(arg0.getBlockX(), arg0.getBlockZ());
    }

    @Override
    public int getHighestBlockYAt(int arg0, int arg1) {
        return getHighestBlockYAt(arg0, arg1, HeightMap.MOTION_BLOCKING);
    }

    @Override
    public int getHighestBlockYAt(Location arg0, HeightMap arg1) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getHighestBlockYAt(int arg0, int arg1, HeightMap arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getHumidity(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getHumidity(int arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<LivingEntity> getLivingEntities() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Chunk[] getLoadedChunks() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getMaxHeight() {
        return nms.getHeight();
    }

    @Override
    public int getMonsterSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getName() {
        return ((ServerWorldProperties) nms.getLevelProperties()).getLevelName();
    }

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox arg0, Predicate<Entity> arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location arg0, double arg1, double arg2, double arg3) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location arg0, double arg1, double arg2, double arg3, Predicate<Entity> arg4) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getPVP() {
        return nms.getServer().isPvpEnabled();
    }

    @Override
    public List<Player> getPlayers() {
        List<Player> list = new ArrayList<Player>(nms.getPlayers().size());

        for (PlayerEntity human : nms.getPlayers())
            if (human instanceof ServerPlayerEntity)
                list.add((Player) ((ExtraServerEntityPlayer)human).getBukkitEntity());

        return list;
    }

    @Override
    public Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Plugin> getPluginChunkTickets(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<BlockPopulator> getPopulators() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Raid> getRaids() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getSeaLevel() {
        return nms.getSeaLevel();
    }

    @Override
    public long getSeed() {
        return nms.getSeed();
    }

    @Override
    public Location getSpawnLocation() {
        BlockPos pos = nms.getSpawnPos();
        return new Location(this, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public double getTemperature(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getTemperature(int arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getThunderDuration() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getTicksPerAmbientSpawns() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getTicksPerAnimalSpawns() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getTicksPerMonsterSpawns() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getTicksPerWaterSpawns() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getTime() {
        return nms.getTime();
    }

    @Override
    public UUID getUID() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getWeatherDuration() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public WorldBorder getWorldBorder() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public File getWorldFolder() {
        // FIXME BROKEN
        return new File(getName());//((ServerWorld)nms).getSaveHandler().getWorldDir();
    }

    @Override
    public WorldType getWorldType() {
        return nms.isFlat() ? WorldType.FLAT : WorldType.NORMAL;
    }

    @Override
    public boolean hasStorm() {
        return nms.getLevelProperties().isRaining();
    }

    @Override
    public boolean isAutoSave() {
        return !nms.isSavingDisabled();
    }

    @Override
    public boolean isChunkForceLoaded(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChunkGenerated(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChunkInUse(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChunkLoaded(Chunk arg0) {
        return isChunkLoaded(arg0.getX(), arg0.getZ());
    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        return (null != nms.getChunkManager().getWorldChunk(x, z, false));
    }

    @Override
    public boolean isGameRule(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isHardcore() {
        return nms.getLevelProperties().isHardcore();
    }

    @Override
    public boolean isThundering() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void loadChunk(Chunk arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void loadChunk(int arg0, int arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean loadChunk(int arg0, int arg1, boolean arg2) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Raid locateNearestRaid(Location arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Location locateNearestStructure(Location arg0, StructureType arg1, int arg2, boolean arg3) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void playEffect(Location arg0, Effect arg1, int arg2) {
        // TODO Auto-generated method stub
    }

    @Override
    public <T> void playEffect(Location arg0, Effect arg1, T arg2) {
        // TODO Auto-generated method stub
    }

    @Override
    public void playEffect(Location arg0, Effect arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }

    @Override
    public <T> void playEffect(Location arg0, Effect arg1, T arg2, int arg3) {
        // TODO Auto-generated method stub
    }

    @Override
    public void playSound(Location arg0, Sound arg1, float arg2, float arg3) {
        // TODO Auto-generated method stub
    }

    @Override
    public void playSound(Location arg0, String arg1, float arg2, float arg3) {
        // TODO Auto-generated method stub
    }

    @Override
    public void playSound(Location arg0, Sound arg1, SoundCategory arg2, float arg3, float arg4) {
        // TODO Auto-generated method stub
    }

    @Override
    public void playSound(Location arg0, String arg1, SoundCategory arg2, float arg3, float arg4) {
        // TODO Auto-generated method stub
    }

    @Override
    public RayTraceResult rayTrace(Location arg0, Vector arg1, double arg2, FluidCollisionMode arg3, boolean arg4, double arg5, Predicate<Entity> arg6) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location arg0, Vector arg1, double arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location arg0, Vector arg1, double arg2, FluidCollisionMode arg3) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location arg0, Vector arg1, double arg2, FluidCollisionMode arg3, boolean arg4) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceEntities(Location arg0, Vector arg1, double arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceEntities(Location arg0, Vector arg1, double arg2, double arg3) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceEntities(Location arg0, Vector arg1, double arg2, Predicate<Entity> arg3) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RayTraceResult rayTraceEntities(Location arg0, Vector arg1, double arg2, double arg3, Predicate<Entity> arg4) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean refreshChunk(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean regenerateChunk(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removePluginChunkTicket(int arg0, int arg1, Plugin arg2) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removePluginChunkTickets(Plugin arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void save() {
        // TODO Auto-generated method stub
    }

    @Override
    public void setAmbientSpawnLimit(int arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setAnimalSpawnLimit(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setAutoSave(boolean arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setBiome(int arg0, int arg1, Biome arg2) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setBiome(int arg0, int arg1, int arg2, Biome arg3) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setChunkForceLoaded(int arg0, int arg1, boolean arg2) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setDifficulty(Difficulty diff) {
        // FIXME BROKEN
        //nms.getLevelProperties().setDifficulty(net.minecraft.world.Difficulty.byOrdinal(diff.ordinal()));
    }

    @Override
    public void setFullTime(long arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public <T> boolean setGameRule(GameRule<T> arg0, T arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean setGameRuleValue(String arg0, String arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setHardcore(boolean arg0) {
        // FIXME BROKEN!!
        //nms.getLevelProperties().setHardcore(arg0);
    }

    @Override
    public void setKeepSpawnInMemory(boolean arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setMonsterSpawnLimit(int arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setPVP(boolean arg0) {
        nms.getServer().setPvpEnabled(arg0);
    }

    @Override
    public void setSpawnFlags(boolean arg0, boolean arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean setSpawnLocation(Location location) {
        return equals(location.getWorld()) ? setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ()) : false;
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z) {
        try {
            Location previousLocation = getSpawnLocation();
            nms.setSpawnPos(new BlockPos(x, y, z));

            // Notify anyone who's listening.
            SpawnChangeEvent event = new SpawnChangeEvent(this, previousLocation);
            Bukkit.getPluginManager().callEvent(event);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void setStorm(boolean arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setThunderDuration(int arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setThundering(boolean arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setTicksPerAmbientSpawns(int arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setTicksPerAnimalSpawns(int arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setTicksPerMonsterSpawns(int arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setTicksPerWaterSpawns(int arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setTime(long arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setWaterAnimalSpawnLimit(int arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setWeatherDuration(int arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz) throws IllegalArgumentException {
        return spawn(location, clazz, null, SpawnReason.CUSTOM);
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<T> function) throws IllegalArgumentException {
        return spawn(location, clazz, function, SpawnReason.CUSTOM);
    }

    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<T> function, SpawnReason reason) throws IllegalArgumentException {
        net.minecraft.entity.Entity entity = createEntity(location, clazz);

        return addEntity(entity, reason, function);
    }

    public net.minecraft.entity.Entity createEntity(Location location, Class<? extends Entity> clazz) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> T addEntity(net.minecraft.entity.Entity entity, SpawnReason reason) throws IllegalArgumentException {
        return addEntity(entity, reason, null);
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> T addEntity(net.minecraft.entity.Entity entity, SpawnReason reason, Consumer<T> function) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Arrow spawnArrow(Location arg0, Vector arg1, float arg2, float arg3) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends AbstractArrow> T spawnArrow(Location arg0, Vector arg1, float arg2, float arg3, Class<T> arg4) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Entity spawnEntity(Location loc, EntityType entityType) {
        return spawn(loc, entityType.getEntityClass());
    }

    @Override
    public FallingBlock spawnFallingBlock(Location arg0, MaterialData arg1) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FallingBlock spawnFallingBlock(Location arg0, BlockData arg1) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FallingBlock spawnFallingBlock(Location arg0, Material arg1, byte arg2) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void spawnParticle(Particle arg0, Location arg1, int arg2) {
        // TODO Auto-generated method stub
    }

    @Override
    public <T> void spawnParticle(Particle arg0, Location arg1, int arg2, T arg3) {
        // TODO Auto-generated method stub
    }

    @Override
    public void spawnParticle(Particle arg0, double arg1, double arg2, double arg3, int arg4) {
        // TODO Auto-generated method stub
    }

    @Override
    public <T> void spawnParticle(Particle arg0, double arg1, double arg2, double arg3, int arg4, T arg5) {
        // TODO Auto-generated method stub
    }

    @Override
    public void spawnParticle(Particle arg0, Location arg1, int arg2, double arg3, double arg4, double arg5) {
        // TODO Auto-generated method stub
    }

    @Override
    public <T> void spawnParticle(Particle arg0, Location arg1, int arg2, double arg3, double arg4, double arg5, T arg6) {
        // TODO Auto-generated method stub
    }

    @Override
    public void spawnParticle(Particle arg0, Location arg1, int arg2, double arg3, double arg4, double arg5, double arg6) {
        // TODO Auto-generated method stub
    }

    @Override
    public void spawnParticle(Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6, double arg7) {
        // TODO Auto-generated method stub
    }

    @Override
    public <T> void spawnParticle(Particle arg0, Location arg1, int arg2, double arg3, double arg4, double arg5, double arg6, T arg7) {
        // TODO Auto-generated method stub
    }

    @Override
    public <T> void spawnParticle(Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6, double arg7, T arg8) {
        // TODO Auto-generated method stub

    }

    @Override
    public void spawnParticle(Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6, double arg7, double arg8) {
        // TODO Auto-generated method stub
    }

    @Override
    public <T> void spawnParticle(Particle arg0, Location arg1, int arg2, double arg3, double arg4, double arg5, double arg6, T arg7, boolean arg8) {
        // TODO Auto-generated method stub
    }

    @Override
    public <T> void spawnParticle(Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6, double arg7, double arg8, T arg9) {
        // TODO Auto-generated method stub
    }

    @Override
    public <T> void spawnParticle(Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6, double arg7, double arg8, T arg9, boolean arg10) {
        // TODO Auto-generated method stub
    }

    @Override
    public LightningStrike strikeLightning(Location arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LightningStrike strikeLightningEffect(Location arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean unloadChunk(Chunk arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean unloadChunk(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean unloadChunk(int arg0, int arg1, boolean arg2) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean unloadChunkRequest(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    public net.minecraft.world.World getHandle() {
        return nms;
    }

    public void setWaterAmbientSpawnLimit(int i) {
        // TODO Auto-generated method stub
    }

    public BlockMetadataStore getBlockMetadata() {
        return blockMetadata;
    }

    @Override
    public long getTicksPerWaterAmbientSpawns() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getWaterAmbientSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setTicksPerWaterAmbientSpawns(int arg0) {
        // TODO Auto-generated method stub
    }

}
