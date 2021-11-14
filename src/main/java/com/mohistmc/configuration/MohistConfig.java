package com.mohistmc.configuration;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.mohistmc.config.MohistConfigUtil;
import java.util.List;

import com.mohistmc.network.download.DownloadSource;
import com.mohistmc.util.i18n.i18n;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class MohistConfig extends ConfigBase {

    private final String HEADER = "This is the main configuration file for Mohist.\n"
            + "\n"
            + "Home: https://mohistmc.com/\n";

    public static MohistConfig instance;

    /* ======================================================================== */
    public final StringSetting server_type = new StringSetting(this, "mohist.server_type", "FML");
    public final StringSetting lang = new StringSetting(this, "mohist.lang", "xx_XX");
    public final StringSetting console_name = new StringSetting(this, "mohist.console_name", "Server");
    public final StringSetting custom_flags = new StringSetting(this, "mohist.custom_flags", "aaaa bbbb");
    public final BoolSetting use_custom_java11 = new BoolSetting(this, "mohist.use_custom_java11", false);
    public final BoolSetting hideJoinModsList = new BoolSetting(this, "forge.hidejoinmodslist", false);

    // mods black list
    public final StringSetting modsblacklist = new StringSetting(this, "forge.modsblacklist.list", "aaaa,bbbb");
    public final StringSetting modsblacklistkickMessage = new StringSetting(this, "forge.modsblacklist.kickmessage", "Use of unauthorized mods");
    public final BoolSetting modsblacklistenable = new BoolSetting(this, "forge.modsblacklist.enable", false);

    // mods white list
    public final IntSetting modsnumber = new IntSetting(this, "forge.modswhitelist.mods_number", 0);
    public final StringSetting modswhitelist = new StringSetting(this, "forge.modswhitelist.list", "minecraft,forge");
    public final StringSetting modswhitelistkickMessage = new StringSetting(this, "forge.modswhitelist.kickmessage", "Use of unauthorized mods");
    public final BoolSetting modswhitelistenable = new BoolSetting(this, "forge.modswhitelist.enable", false);

    // plugin fix
    public final BoolSetting enableworldeditfix = new BoolSetting(this, "mohist.enable_worldedit_fix", true);

    // MohistProxySelector
    public final BoolSetting debug_msg = new BoolSetting(this, "mohist.networkmanager.debug", false);

    public final StringSetting libraries_downloadsource = new StringSetting(this, "mohist.libraries_downloadsource", i18n.isCN() ? DownloadSource.CHINA.name() : DownloadSource.MOHIST.name());

    /* ======================================================================== */

    public MohistConfig() {
        super("mohist.yml");
        init();
        instance = this;
    }

    public void init()
    {
        for(Field f : this.getClass().getFields())
        {
            if(Modifier.isFinal(f.getModifiers()) && Modifier.isPublic(f.getModifiers()) && !Modifier.isStatic(f.getModifiers()))
            {
                try
                {
                    Setting setting = (Setting) f.get(this);
                    if(setting == null) {
                        continue;
                    }
                    settings.put(setting.path, setting);
                }
                catch (ClassCastException ignored)
                {

                }
                catch(Throwable t)
                {
                    System.out.println("[Mohist] Failed to initialize a MohistConfig setting.");
                    t.printStackTrace();
                }
            }
        }
        load();
    }

    @Override
    public void load()
    {
        try
        {
            config = YamlConfiguration.loadConfiguration(configFile);
            StringBuilder header = new StringBuilder(HEADER + "\n");
            for (Setting toggle : settings.values())
            {
                config.addDefault(toggle.path, toggle.def);
                settings.get(toggle.path).setValue(config.getString(toggle.path));
            }

            version = getInt("config-version", 1);
            set("config-version", 1);
            config.options().header(header.toString());
            config.options().copyDefaults(true);
            this.save();
        }
        catch (Exception ex)
        {
            Bukkit.getLogger().info("Could not load " + this.configFile);
            ex.printStackTrace();
        }
    }

    public static void setValueMohist(String key, String value) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(MohistConfigUtil.mohistyml);
        yml.set(key, value);
        try {
            yml.save(MohistConfigUtil.mohistyml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setValueMohist(String key, boolean value) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(MohistConfigUtil.mohistyml);
        yml.set(key, value);
        try {
            yml.save(MohistConfigUtil.mohistyml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getStringList0(String path, List<String> defValue) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(MohistConfigUtil.mohistyml);
        yml.addDefault(path, defValue);
        return yml.getStringList(path);
    }

    public static boolean getBoolean0(String path, boolean defValue) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(MohistConfigUtil.mohistyml);
        return yml.getBoolean(path, defValue);
    }
}
