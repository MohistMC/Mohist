package net.minecraftforge.cauldron.configuration;

public class CauldronWorldConfig extends WorldConfig
{
    public boolean entityDespawnImmediate = true;

    public CauldronWorldConfig(String worldName, ConfigBase configFile)
    {
        super(worldName, configFile);
        init();
    }

    public void init()
    {
        entityDespawnImmediate = getBoolean( "entity-despawn-immediate", false);
        this.save();
    }
}
