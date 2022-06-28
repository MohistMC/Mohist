/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2022.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.configuration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class ConfigBase {
    protected final File configFile;

    /* ======================================================================== */

    public YamlConfiguration config;
    protected int version;
    protected Map<String, Setting> settings = new HashMap<>();

    /* ======================================================================== */

    public ConfigBase(String fileName) {
        this.configFile = new File("mohist-config", fileName);
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    private static String transform(String s) {
        return ChatColor.translateAlternateColorCodes('&', s).replaceAll("\\\\n", "\n");
    }

    public Map<String, Setting> getSettings() {
        return settings;
    }

    public void save() {
        try {
            config.save(configFile);
        } catch (IOException ex) {
            Bukkit.getLogger().info("Could not save " + configFile);
            ex.printStackTrace();
        }
    }

    protected abstract void load();

    public void set(String path, Object val) {
        config.set(path, val);
    }

    public boolean isSet(String path) {
        return config.isSet(path);
    }

    public boolean isInt(String path) {
        return config.isInt(path);
    }

    public boolean isBoolean(String path) {
        return config.isBoolean(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public boolean getBoolean(String path, boolean def) {
        return getBoolean(path, def, true);
    }

    public boolean getBoolean(String path, boolean def, boolean useDefault) {
        if (useDefault) {
            config.addDefault(path, def);
        }
        return config.getBoolean(path, def);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInt(path, config.getInt(path));
    }

    public <T> List getList(String path, T def) {
        config.addDefault(path, def);
        return config.getList(path, config.getList(path));
    }

    public String getString(String path, String def) {
        return getString(path, def, true);
    }

    public String getString(String path, String def, boolean useDefault) {
        if (useDefault) {
            config.addDefault(path, def);
        }
        return config.getString(path, def);
    }

    public List<String> getStringList(String path, List<String> def) {
        config.addDefault(path, def);
        return config.getStringList(path);
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public String getFakePlayer(String className, String defaultName) {
        return getString("fake-players." + className + ".username", defaultName);
    }
}
