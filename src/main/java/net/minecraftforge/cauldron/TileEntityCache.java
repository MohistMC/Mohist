package net.minecraftforge.cauldron;

import net.minecraft.tileentity.TileEntity;

public class TileEntityCache {

    public Class<? extends TileEntity> tileEntityClass;
    public boolean tickNoPlayers = false;
    public int tickInterval = 1;
    public String configPath;
    public String worldName;

    public TileEntityCache(Class<? extends TileEntity> tileEntityClass, String worldName, String configPath, boolean tickNoPlayers, int tickInterval)
    {
        this.tileEntityClass = tileEntityClass;
        this.worldName = worldName;
        this.tickNoPlayers = tickNoPlayers;
        this.tickInterval = tickInterval;
        this.configPath = configPath;
    }
}
