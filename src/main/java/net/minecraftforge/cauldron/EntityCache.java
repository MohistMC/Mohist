package net.minecraftforge.cauldron;

import net.minecraft.entity.Entity;

public class EntityCache{

    public Class<? extends Entity> tileEntityClass;
    public int tickInterval=1;
    public String configPath;
    public String worldName;

    public EntityCache(Class<? extends Entity> entityClass,String worldName,String configPath,int tickInterval){
        this.tileEntityClass=entityClass;
        this.worldName=worldName;
        this.tickInterval=tickInterval;
        this.configPath=configPath;
    }
}
