package com.mohistmc.configuration;

import com.mohistmc.api.ServerAPI;
import java.util.List;

public class WorldConfig {
    private final String worldName;
    public ConfigBase baseConfig;
    private boolean verbose;

    public WorldConfig(String worldName, ConfigBase configFile) {
        this.worldName = worldName.toLowerCase();
        this.baseConfig = configFile;
    }

    public void save() {
        baseConfig.save();
    }

    private void log(String s) {
        if (verbose) {
            ServerAPI.getNMSServer().logInfo(s);
        }
    }

    public void set(String path, Object val) {
        baseConfig.config.set(path, val);
    }

    public boolean isBoolean(String path) {
        return baseConfig.config.isBoolean(path);
    }

    public boolean getBoolean(String path, boolean def) {
        if (baseConfig.settings.get("world-settings.default." + path) == null) {
            baseConfig.settings.put("world-settings.default." + path, new BoolSetting(baseConfig, "world-settings.default." + path, def));
        }

        baseConfig.config.addDefault("world-settings.default." + path, def);
        return baseConfig.config.getBoolean("world-settings." + worldName + "." + path, baseConfig.config.getBoolean("world-settings.default." + path));
    }

    private double getDouble(String path, double def) {
        baseConfig.config.addDefault("world-settings.default." + path, def);
        return baseConfig.config.getDouble("world-settings." + worldName + "." + path, baseConfig.config.getDouble("world-settings.default." + path));
    }

    public int getInt(String path, int def) {
        if (baseConfig.settings.get("world-settings.default." + path) == null) {
            baseConfig.settings.put("world-settings.default." + path, new IntSetting(baseConfig, "world-settings.default." + path, def));
        }

        baseConfig.config.addDefault("world-settings.default." + path, def);
        return baseConfig.config.getInt("world-settings." + worldName + "." + path, baseConfig.config.getInt("world-settings.default." + path));
    }

    private <T> List getList(String path, T def) {
        baseConfig.config.addDefault("world-settings.default." + path, def);
        return baseConfig.config.getList("world-settings." + worldName + "." + path, baseConfig.config.getList("world-settings.default." + path));
    }

    private String getString(String path, String def) {
        baseConfig.config.addDefault("world-settings.default." + path, def);
        return baseConfig.config.getString("world-settings." + worldName + "." + path, baseConfig.config.getString("world-settings.default." + path));
    }
}
