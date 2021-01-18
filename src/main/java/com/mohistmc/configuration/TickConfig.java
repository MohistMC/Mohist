package com.mohistmc.configuration;

import java.io.File;
import java.util.HashMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class TickConfig {

    /**
     * Default tick params if nothing to load
     */
    private static final TickParams DEFAULT_TICK_PARAMS;

    /**
     * Config section for default tick params
     */
    private static final String DEFAULTS_SEC;

    /**
     * Config section for overridden params
     */
    private static final String OVERRIDES_SEC;

    /**
     * Config key for 'skipEvery' tick field
     */
    private static final String SKIP_EVERY_KEY;

    /**
     * Config value indicating field is inherited
     */
    private static final String DEFAULT_VAL;

    /**
     * Class instance for entities
     */
    public static final TickConfig ENTITIES;

    /**
     * Class instance for tiles
     */
    public static final TickConfig TILES;

    static {
        DEFAULT_TICK_PARAMS = new TickParams();
        DEFAULT_TICK_PARAMS.skipEvery = 0;
        DEFAULTS_SEC = "defaults";
        OVERRIDES_SEC = "overrides";
        SKIP_EVERY_KEY = "skip-every";
        DEFAULT_VAL = "default";
        ENTITIES = new TickConfig("entities.yml", "entity");
        TILES = new TickConfig("tiles.yml", "tile");
    }

    public boolean canTick(Class<?> clazz, long gametime) {
        TickParams params = paramsMap.get(clazz);
        if (params == null) {
            if (cfg == null) reloadConfig();
            params = new TickParams();
            params.skipEvery = defaultLoadedParams.skipEvery;
            paramsMap.put(clazz, params);
            cfg.set(OVERRIDES_SEC + "." + formatClazz(clazz) + "." + SKIP_EVERY_KEY, DEFAULT_VAL);
            saveConfig();
        }
        return params.skipEvery == 0 || gametime % params.skipEvery != 0;
    }

    public void reloadConfig() {
        paramsMap.clear();
        cfg = YamlConfiguration.loadConfiguration(cfgfile);
        if (cfgfile.exists()) {
            ConfigurationSection defaultsSec = cfg.getConfigurationSection(DEFAULTS_SEC);
            if (defaultsSec == null) {
                defaultLoadedParams = new TickParams();
                defaultLoadedParams.skipEvery = DEFAULT_TICK_PARAMS.skipEvery;
            }
            else defaultLoadedParams = parseTickParams(defaultsSec);
            ConfigurationSection overridesSec = cfg.getConfigurationSection(OVERRIDES_SEC);
            if (overridesSec != null) {
                for (String override : overridesSec.getKeys(false)) {
                    ConfigurationSection overrideSec = overridesSec.getConfigurationSection(override);
                    if (overrideSec != null) {
                        try {
                            Class<?> overrideCls = Class.forName(unformatClazz(override));
                            TickParams params = parseTickParams(overrideSec);
                            paramsMap.put(overrideCls, params);
                        } catch (Exception e) {}
                    }
                }
            }
        }
        else {
            cfg.options().header(
                "Welcome to Mohist " + typename + " tick configuration!\n"
                + "This opens up good TPS savings if used wisely.\n"
                + "'" + OVERRIDES_SEC + "' section populates itself over time.\n"
                + "-=-=-=-=-\n"
                + "Simple usage cases:\n"
                + "'" + SKIP_EVERY_KEY + ": 50' - skips every 50th tick\n"
                + "'" + SKIP_EVERY_KEY + ": 0' - tick limiter is not applied (vanilla behaviour)\n"
                + "'" + SKIP_EVERY_KEY + ": 1' - " + typename + " will not tick at all (skips every 1st tick)\n"
                + "'" + SKIP_EVERY_KEY + ": " + DEFAULT_VAL + "' - value is grabbed from '" + DEFAULTS_SEC + "' section\n"
                + "Values in range of 0 up to 1000 can be used, you won't need more than that.\n"
                + "-=-=-=-=-\n"
            );
            defaultLoadedParams = new TickParams();
            defaultLoadedParams.skipEvery = DEFAULT_TICK_PARAMS.skipEvery;
            ConfigurationSection defaultsSec = cfg.createSection("defaults");
            defaultsSec.set(SKIP_EVERY_KEY, defaultLoadedParams.skipEvery);
            cfg.createSection("overrides");
            saveConfig();
        }
    }

    private TickParams parseTickParams(ConfigurationSection sec) {
        TickParams params = new TickParams();
        if (sec.isInt(SKIP_EVERY_KEY)) {
            int skipEvery = sec.getInt(SKIP_EVERY_KEY);
            if (skipEvery > 1000) skipEvery = 1000;
            if (skipEvery < 0) skipEvery = 0;
            params.skipEvery = skipEvery;
        }
        else {
            if (sec.getParent().getName().equals(OVERRIDES_SEC))
                params.skipEvery = defaultLoadedParams.skipEvery;
            else
                params.skipEvery = DEFAULT_TICK_PARAMS.skipEvery;
        }
        return params;
    }

    private String formatClazz(Class<?> clazz) {
        return clazz.getName().replace('.', '-');
    }

    private String unformatClazz(String clazz) {
        return clazz.replace('-', '.');
    }

    private void saveConfig() {
        try {
        cfg.save(cfgfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final File cfgfile;
    private final String typename;
    private final HashMap<Class<?>, TickParams> paramsMap;
    private YamlConfiguration cfg;
    private TickParams defaultLoadedParams;

    private TickConfig(String filename, String typename) {
        this.cfgfile = new File("mohist-config", filename);
        this.typename = typename;
        this.paramsMap = new HashMap<>();
    }

}
