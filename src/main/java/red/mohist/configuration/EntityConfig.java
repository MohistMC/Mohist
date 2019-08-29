package red.mohist.configuration;

import net.minecraftforge.cauldron.CauldronHooks;
import net.minecraftforge.cauldron.EntityCache;
import net.minecraftforge.cauldron.command.EntityCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import red.mohist.Mohist;

public class EntityConfig extends ConfigBase {
    private final String HEADER = "This is the main configuration file for Entities.\n"
            + "\n"
            + "Home: https://www.mohist.red/\n";

    /* ======================================================================== */
    public final BoolSetting skipEntityTicks = new BoolSetting(this, "settings.skip-entity-ticks", true, "If enabled, allows the default entity tick skip feature when no players are near.");
    public final BoolSetting enableECanUpdateWarning = new BoolSetting(this, "debug.enable-ent-can-update-warning", false, "Set true to detect which entities should not be ticking.");
    public final BoolSetting preventInvalidEntityUpdates = new BoolSetting(this, "settings.prevent-invalid-entity-updates", true, "Used to determine if an entity should tick and if not the TE is added to a ban list. Note: This should help improve performance.");
    /* ======================================================================== */

    public EntityConfig() {
        super("entities.yml", "entities");
        init();
    }

    @Override
    public void addCommands() {
        commands.put(this.commandName, new EntityCommand());
    }

    public void init() {
        settings.put(skipEntityTicks.path, skipEntityTicks);
        settings.put(enableECanUpdateWarning.path, enableECanUpdateWarning);
        settings.put(preventInvalidEntityUpdates.path, preventInvalidEntityUpdates);
        load();
    }

    @Override
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

            for (EntityCache seCache : CauldronHooks.entityCache.values()) {
                seCache.tickNoPlayers = config.getBoolean("world-settings." + seCache.worldName + "." + seCache.configPath + ".tick-no-players", config.getBoolean("world-settings.default." + seCache.configPath + ".tick-no-players"));
                seCache.neverEverTick = config.getBoolean("world-settings." + seCache.worldName + "." + seCache.configPath + ".never-ever-tick", config.getBoolean("world-settings.default." + seCache.configPath + ".never-ever-tick"));
                seCache.tickInterval = config.getInt("world-settings." + seCache.worldName + "." + seCache.configPath + ".tick-interval", config.getInt("world-settings.default." + seCache.configPath + ".tick-interval"));
            }
            this.saveWorldConfigs();
            this.save();
        } catch (Exception ex) {
            Mohist.LOGGER.error("Could not load " + this.configFile);
            ex.printStackTrace();
        }
    }
}
