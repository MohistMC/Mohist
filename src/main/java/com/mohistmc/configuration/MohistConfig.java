package com.mohistmc.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.mohistmc.util.i18n.i18n;
import net.minecraftforge.cauldron.configuration.BoolSetting;
import net.minecraftforge.cauldron.configuration.ConfigBase;
import net.minecraftforge.cauldron.configuration.Setting;
import net.minecraftforge.cauldron.configuration.StringSetting;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import com.mohistmc.api.ServerAPI;
import com.mohistmc.command.DumpCommand;
import com.mohistmc.command.MohistCommand;
import org.spigotmc.SpigotConfig;

public class MohistConfig extends ConfigBase {

    private final String HEADER = "This is the main configuration file for Mohist.\n"
            + "\n"
            + "Home: https://www.mohist.red/\n";

    public static MohistConfig instance;

    /* ======================================================================== */

    public final StringSetting server_type = new StringSetting(this, "server-type", "FML", "Set the server type displayed in motd (FML/BUKKIT/VANILLA)");
    public final StringSetting lang = new StringSetting(this, "lang", "en_US", "Mohist internationalization language setting, will return the default system language when your settings are invalid");
    public final StringSetting console_name = new StringSetting(this, "console_name", "Server", "Front of the console, for example /say");
    public final BoolSetting support_nocmd = new BoolSetting(this, "support_nocmd", false, "Some server tools do not recognize I18N");
    // Bukkit Event Canceled
    public final BoolSetting use_custom_java8 = new BoolSetting(this, "mohist.use_custom_java8", false, i18n.get("mohistsettings.use_custom_java8"));
    public final BoolSetting needToUpdate = new BoolSetting(this, "mohist.check_update_auto_download", false, i18n.get("mohistsettings.needToUpdate"));
    public final BoolSetting check_libraries = new BoolSetting(this, "mohist.check_libraries", true, i18n.get("mohistsettings.check_libraries"));
    public final BoolSetting check_update = new BoolSetting(this, "mohist.check_update", true, "Check Update");

    public final StringSetting libraries_black_list = new StringSetting(this, "libraries_black_list", "aaaaa;bbbbbb", i18n.get("mohistsettings.libraries_black_list"));

    /* ======================================================================== */

    public MohistConfig()
    {
        super("mohist.yml", "mohist");
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
                catch (ClassCastException e)
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
    public void addCommands()
    {
        commands.put("mohist", new MohistCommand("mohist"));
        commands.put("Dump", new DumpCommand("version"));
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
            config.options().header(header.toString());
            config.options().copyDefaults(true);

            version = getInt("config-version", 1);
            set("config-version", 1);

            this.save();
        }
        catch (Exception ex)
        {
            ServerAPI.getNMSServer().logSevere("Could not load " + this.configFile);
            ex.printStackTrace();
        }
    }

    public static boolean bungeeOnlineMode = true;
    private static void bungeeOnlineMode() {
        bungeeOnlineMode = instance.getBoolean("mohist.bungee-online-mode", true);
    }

    public static boolean isProxyOnlineMode() {
        return Bukkit.getOnlineMode() || (SpigotConfig.bungee && bungeeOnlineMode);
    }
}
