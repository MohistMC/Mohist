package red.mohist.common.cache;

import net.minecraft.entity.Entity;

public class EntityCache {
    public Class<? extends Entity> entityClass;
    public int tickInterval = 1;
    public String configPath;
    public String worldName;

    public EntityCache(Class<? extends Entity> entityClass, String worldName, String configPath, int tickInterval)
    {
        this.entityClass = entityClass;
        this.worldName = worldName;
        this.tickInterval = tickInterval;
        this.configPath = configPath;
    }
}