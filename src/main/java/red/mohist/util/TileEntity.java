package red.mohist.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import red.mohist.common.cache.TileEntityCache;

import java.util.HashMap;
import java.util.Map;

public class TileEntity {
    public static Map<Class<? extends net.minecraft.tileentity.TileEntity>, TileEntityCache> tileEntityCache = new HashMap<Class<? extends net.minecraft.tileentity.TileEntity>, TileEntityCache>();

    public static String sanitizeClassName(Class clazz) {
        String name = clazz.getName().replace(".", "-");

        return name.replaceAll("[^A-Za-z0-9\\-]", "");
    }

    public static String sanitizeClassName(net.minecraft.tileentity.TileEntity tileEntity) {
        return sanitizeClassName(tileEntity.getClass());
    }

    public static boolean canTileEntityTick(net.minecraft.tileentity.TileEntity tileEntity, World world) {
        if (tileEntity == null) {
            return false;
        }

        if (world.tileentityConfig == null) {
            return true;
        }

        if (MinecraftServer.tileEntityConfig.skipTileEntityTicks.getValue()) {
            TileEntityCache teCache = tileEntityCache.get(tileEntity.getClass());
            if (teCache == null) {
                String teConfigPath = sanitizeClassName(tileEntity);
                teCache = new TileEntityCache(tileEntity.getClass(), world.getWorldInfo().getWorldName().toLowerCase(), teConfigPath, world.tileentityConfig.getBoolean(teConfigPath + ".tick-no-players", false), world.tileentityConfig.getInt(teConfigPath + ".tick-interval", 1));
                tileEntityCache.put(tileEntity.getClass(), teCache);
            }


            // do not tick if no players in range
            // @TODO not implemented yet
//            if (!teCache.tickNoPlayers && !ActivationRange.checkIfActive(tileEntity))
//            {
//
//                return false;
//            }

            // Skip tick interval
            if (teCache.tickInterval > 0 && (world.getWorldInfo().getWorldTotalTime() % teCache.tickInterval == 0L)) {
                return true;
            }

            return false;
        }

        return true;
    }
}
