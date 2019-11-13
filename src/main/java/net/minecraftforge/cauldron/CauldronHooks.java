package net.minecraftforge.cauldron;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;

public class CauldronHooks {

    public static Map<Class<? extends TileEntity>, TileEntityCache> tileEntityCache = new HashMap<>();
    public static Map<Class<? extends Entity>, EntityCache> entityCache = new HashMap<>();

    /*
     * Thermos
     * 0 = false, dependent on others
     * 1 = true
     * -1 = false, absolute
     */
    public static byte canEntityTick(Entity entity, World world)
    {
        if (entity == null || world.entityWorldConfig == null) { return 0; }
        if (!MinecraftServer.entityConfig.skipEntityTicks.getValue()) { return 1; }
        int cX = MathHelper.floor(entity.posX) >> 4, cZ = MathHelper.floor( entity.posZ ) >> 4;
        EntityCache seCache = entityCache.get(entity.getClass());
        if (seCache == null)
        {
            String seConfigPath = entity.getClass().getName().replace(".", "-");
            seConfigPath = seConfigPath.replaceAll("[^A-Za-z0-9\\-]", ""); // Fix up odd class names to prevent YAML errors
            seCache = new EntityCache(entity.getClass(), world.getWorldInfo().getWorldName().toLowerCase(), seConfigPath, world.entityWorldConfig.getBoolean(seConfigPath + ".tick-no-players", false), world.entityWorldConfig.getBoolean(seConfigPath + ".never-ever-tick", false), world.entityWorldConfig.getInt(seConfigPath + ".tick-interval", 1));
            entityCache.put(entity.getClass(), seCache);
        }

        if(seCache.neverEverTick)
        {
            return -1;
        }

        // Skip tick interval
        if (seCache.tickInterval > 0 && (world.getWorldInfo().getWorldTotalTime() % seCache.tickInterval == 0L))
        {
            return 0;
        }

        // Tick with no players near?
        if (seCache.tickNoPlayers)
        {
            return 1;
        }

        if(world.getChunkProvider() instanceof ChunkProviderServer) // Thermos - allow the server to tick entities that are in chunks trying to unload
        {
            ChunkProviderServer cps = ((ChunkProviderServer)world.getChunkProvider());
            if(cps.droppedChunksSet.contains(ChunkPos.asLong(cX, cZ)))
            {
                Chunk c = cps.getChunkIfLoaded(cX, cZ);
                if(c != null)
                {
                    if(c.lastAccessedTick < 2L)
                    {
                        return 1;
                    }
                }
            }
        }

        return -1;
    }

    public static boolean canTileEntityTick(TileEntity tileEntity, World world)
    {
        /*
        if (tileEntity == null || world.tileEntityWorldConfig == null) {
            return false;
        }
        if (MinecraftServer.tileEntityConfig.skipTileEntityTicks.getValue())
        {
            TileEntityCache teCache = tileEntityCache.get(tileEntity.getClass());
            if (teCache == null)
            {
                String teConfigPath = tileEntity.getClass().getName().replace(".", "-");
                teConfigPath = teConfigPath.replaceAll("[^A-Za-z0-9\\-]", ""); // Fix up odd class names to prevent YAML errors
                teCache = new TileEntityCache(tileEntity.getClass(), world.getWorldInfo().getWorldName().toLowerCase(), teConfigPath, world.tileEntityWorldConfig.getBoolean(teConfigPath + ".tick-no-players", false), world.tileEntityWorldConfig.getInt(teConfigPath + ".tick-interval", 1));
                tileEntityCache.put(tileEntity.getClass(), teCache);
            }

            // Tick with no players near?
            if (!teCache.tickNoPlayers && !world.isActiveBlockCoord(tileEntity.getPos().getX(), tileEntity.getPos().getZ()))
            {
                return false;
            }

            // Skip tick interval
            if (teCache.tickInterval > 0 && (world.getWorldInfo().getWorldTotalTime() % teCache.tickInterval == 0L))
            {
                return true;
            }

            if(world.getChunkProvider() instanceof ChunkProviderServer) // Thermos - allow the server to tick tiles that are trying to unload
            {
                ChunkProviderServer cps = ((ChunkProviderServer)world.getChunkProvider());
                if(cps.droppedChunksSet.contains(Long.valueOf(ChunkPos.asLong(tileEntity.getPos().getX() >> 4, tileEntity.getPos().getZ() >> 4))))
                {
                    Chunk c = cps.getChunkIfLoaded(tileEntity.getPos().getX() >> 4, tileEntity.getPos().getY() >> 4);
                    if(c != null)
                    {
                        if(c.lastAccessedTick < 2L)
                        {
                            return true;
                        }
                    }
                }
            }

            return false;
        }
         */
        return true;
    }


}
