package com.mohistmc.configuration;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import com.mohistmc.util.i18n.Message;

public class MohistConfig extends ConfigBase {

    private final String HEADER = "This is the main configuration file for Mohist.\n"
            + "\n"
            + "Home: https://mohist.red/\n";

    public static MohistConfig instance;

    /* ======================================================================== */
    public final StringSetting server_type = new StringSetting(this, "mohist.server_type", "FML", Message.get("mohistsettings.server_type"));
    public final StringSetting lang = new StringSetting(this, "mohist.lang", "xx_XX", Message.get("mohistsettings.lang"));
    public final StringSetting console_name = new StringSetting(this, "mohist.console_name", "Server", Message.get("mohistsettings.console_name"));

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
                if (!toggle.description.equals("")) {
                    header.append("Setting: ").append(toggle.path).append(" Default: ").append(toggle.def).append("   # ").append(toggle.description).append("\n");
                }

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

    public static String getHighlight(String key, String def) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(MohistConfigUtil.mohistyml);
        String color = yml.getString(key, def);
        return color;
    }

    public static void setValueMohist(String oldValue, String value) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(MohistConfigUtil.mohistyml);
        yml.set(oldValue, value);
        try {
            yml.save(MohistConfigUtil.mohistyml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setValueMohist(String oldValue, boolean value) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(MohistConfigUtil.mohistyml);
        yml.set(oldValue, value);
        try {
            yml.save(MohistConfigUtil.mohistyml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
