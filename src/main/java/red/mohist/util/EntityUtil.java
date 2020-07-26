package red.mohist.util;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import red.mohist.common.cache.EntityCache;

public class EntityUtil {

    public static Map<Class<? extends net.minecraft.entity.Entity>, EntityCache> entityCache = new HashMap<Class<? extends net.minecraft.entity.Entity>, EntityCache>();

    public static String sanitizeClassName(Class clazz)
    {
        String name = clazz.getName().replace(".", "-");

        return name.replaceAll("[^A-Za-z0-9\\-]", "");
    }

    public static String sanitizeClassName(net.minecraft.entity.Entity entity)
    {
        return sanitizeClassName(entity.getClass());
    }

    public static boolean canEntityTick(net.minecraft.entity.Entity entity, World world)
    {
        if (entity == null) return false;
        if (world.entityConfig == null) return true;

        if (MinecraftServer.entityConfig.skipEntityTicks.getValue())
        {
            EntityCache eCache = entityCache.get(entity.getClass());
            if (eCache == null)
            {
                String eConfigPath = sanitizeClassName(entity);
                eCache = new EntityCache(entity.getClass(), world.getWorldInfo().getWorldName().toLowerCase(), eConfigPath, world.entityConfig.getInt(eConfigPath + ".tick-interval", 1));
                entityCache.put(entity.getClass(), eCache);
            }

            // do not tick if no players in range
            // @TODO not implemented yet

            // Skip tick interval
            if (eCache.tickInterval > 0 && (world.getWorldInfo().getGameTime() % eCache.tickInterval == 0L)) return true;
            return false;
        }

        return true;
    }
}