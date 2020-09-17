package com.mohistmc.configuration;

import com.mohistmc.common.cache.EntityCache;
import com.mohistmc.util.Entity;
import net.minecraft.server.MinecraftServer;
import org.bukkit.configuration.file.YamlConfiguration;

public class EntityConfig extends ConfigBase
{
    private final String HEADER = "This is the main configuration file for Entities.\n"
            + "Use carefully, it may break some mechanics";

    public static EntityConfig instance;
    public final BoolSetting skipEntityTicks = new BoolSetting(this, "settings.skip-entity-ticks", true, "If enabled, turns on entity tick skip feature.");
    public final BoolSetting skipActivationRange = new BoolSetting(this, "settings.skip-activation-range", false, "If enabled, skips entity activation range checks.");

    public EntityConfig()
    {
        super("entity.yml");
        init();
        instance = this;
    }

    public void init()
    {
        settings.put(skipEntityTicks.path, skipEntityTicks);
        settings.put(skipActivationRange.path, skipActivationRange);
        load();
    }

    public void load()
    {
        try
        {
            config = YamlConfiguration.loadConfiguration(configFile);
            String header = HEADER + "\n";
            for (Setting toggle : settings.values())
            {
                if (!toggle.description.equals(""))
                    header += "Setting: " + toggle.path + " Default: " + toggle.def + "   # " + toggle.description + "\n";

                config.addDefault(toggle.path, toggle.def);
                settings.get(toggle.path).setValue(config.getString(toggle.path));
            }
            config.options().header(header);
            config.options().copyDefaults(true);

            version = getInt("config-version", 1);
            set("config-version", 1);

            for (EntityCache cache : Entity.entityCache.values())
            {
                cache.tickInterval = config.getInt( "world-settings." + cache.worldName + "." + cache.configPath + ".tick-interval", config.getInt( "world-settings.default." + cache.configPath + ".tick-interval") );
            }
            this.save();
        }
        catch (Exception ex)
        {
            MinecraftServer.LOGGER.warn("Could not load " + this.configFile);
            ex.printStackTrace();
        }
    }
}