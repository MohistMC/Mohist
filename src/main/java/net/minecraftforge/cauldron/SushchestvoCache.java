package net.minecraftforge.cauldron;

import net.minecraft.entity.Entity;

public class SushchestvoCache {

    public Class<? extends Entity> entityClass;
    public boolean tickNoPlayers = false;
    public boolean neverEverTick = false;
    public int tickInterval = 1;
    public String configPath;
    public String worldName;

    public SushchestvoCache(Class<? extends Entity> entityClass, String worldName, String configPath, boolean tickNoPlayers, boolean neverEverTick, int tickInterval)
    {
        this.entityClass = entityClass;
        this.worldName = worldName.intern();
        this.tickNoPlayers = tickNoPlayers;
        this.neverEverTick = neverEverTick;
        this.tickInterval = tickInterval;
        this.configPath = configPath;
    }
}
