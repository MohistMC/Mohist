package red.mohist.configuration;

import net.minecraft.server.MinecraftServer;
import org.bukkit.configuration.file.YamlConfiguration;
import red.mohist.common.cache.TileEntityCache;
import red.mohist.util.TileEntity;

public class TileEntityConfig extends ConfigBase {
    public static TileEntityConfig instance;
    public final BoolSetting skipTileEntityTicks = new BoolSetting(this, "settings.skip-tileentity-ticks", true, "If enabled, turns on tileentity tick skip feature.");
    private final String HEADER = "This is the main configuration file for TileEntities.\n"
            + "Use carefully, it may break some mechanics";

    public TileEntityConfig() {
        super("tileentity.yml");
        init();
        instance = this;
    }

    public void init() {
        settings.put(skipTileEntityTicks.path, skipTileEntityTicks);
        load();
    }

    public void load() {
        try {
            config = YamlConfiguration.loadConfiguration(configFile);
            String header = HEADER + "\n";
            for (Setting toggle : settings.values()) {
                if (!toggle.description.equals(""))
                    header += "Setting: " + toggle.path + " Default: " + toggle.def + "   # " + toggle.description + "\n";

                config.addDefault(toggle.path, toggle.def);
                settings.get(toggle.path).setValue(config.getString(toggle.path));
            }
            config.options().header(header);
            config.options().copyDefaults(true);

            version = getInt("config-version", 1);
            set("config-version", 1);

            for (TileEntityCache teCache : TileEntity.tileEntityCache.values()) {
                teCache.tickNoPlayers = config.getBoolean("world-settings." + teCache.worldName + "." + teCache.configPath + ".tick-no-players", config.getBoolean("world-settings.default." + teCache.configPath + ".tick-no-players"));
                teCache.tickInterval = config.getInt("world-settings." + teCache.worldName + "." + teCache.configPath + ".tick-interval", config.getInt("world-settings.default." + teCache.configPath + ".tick-interval"));
            }
            this.save();
        } catch (Exception ex) {
            MinecraftServer.LOGGER.warn("Could not load " + this.configFile);
            ex.printStackTrace();
        }
    }
}