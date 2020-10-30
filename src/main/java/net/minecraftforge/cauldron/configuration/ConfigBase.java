package net.minecraftforge.cauldron.configuration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.cauldron.CauldronHooks;
import net.minecraftforge.cauldron.TileEntityCache;
import net.minecraftforge.cauldron.command.CauldronCommand;
import net.minecraftforge.common.DimensionManager;

import org.apache.commons.lang.BooleanUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.base.Throwables;

public abstract class ConfigBase
{
    protected final File configFile;
    protected final String commandName;
    
    /* ======================================================================== */

    protected YamlConfiguration config;
    protected int version;
    protected Map<String, Command> commands;
    protected Map<String, Setting> settings = new HashMap<String, Setting>();

    /* ======================================================================== */

    public ConfigBase(String fileName, String commandName)
    {
        this.configFile = new File(fileName);
        this.config = YamlConfiguration.loadConfiguration(configFile);
        this.commandName = commandName;
        this.commands = new HashMap<String, Command>();
        this.addCommands();
    }

    protected abstract void addCommands();

    public Map<String, Setting> getSettings()
    {
        return settings;
    }

    public void registerCommands()
    {
        for (Map.Entry<String, Command> entry : commands.entrySet())
        {
            MinecraftServer.getServer().server.getCommandMap().register(entry.getKey(), this.commandName, entry.getValue());
        }
    }

    public void save()
    {
        try
        {
            config.save(configFile);
        }
        catch (IOException ex)
        {
            MinecraftServer.getServer().logSevere("Could not save " + configFile);
            ex.printStackTrace();
        }
    }

    public void saveWorldConfigs()
    {
        for (int i = 0; i < MinecraftServer.getServer().worlds.size(); ++i)
        {
            WorldServer worldserver = MinecraftServer.getServer().worlds.get(i);

            if (worldserver != null)
            {
                if (worldserver.cauldronConfig != null)
                {
                    worldserver.cauldronConfig.save();
                }
                if (worldserver.tileentityConfig != null)
                {
                    worldserver.tileentityConfig.save();
                }
                if (worldserver.sushchestvoConfig != null)
                {
                    worldserver.sushchestvoConfig.save();
                }                
            }
        }
    }

    protected abstract void load();
 
    public void set(String path, Object val)
    {
        config.set(path, val);
    }

    public boolean isSet(String path)
    {
        return config.isSet(path);
    }

    public boolean isInt(String path)
    {
        return config.isInt(path);
    }

    public boolean isBoolean(String path)
    {
        return config.isBoolean(path);
    }

    public boolean getBoolean(String path)
    {
        return config.getBoolean(path);
    }

    public boolean getBoolean(String path, boolean def)
    {
        return getBoolean(path, def, true);
    }

    public boolean getBoolean(String path, boolean def, boolean useDefault)
    {
        if (useDefault)
        {
        config.addDefault(path, def);
        }
        return config.getBoolean(path, def);
    }

    public int getInt(String path)
    {
        return config.getInt(path);
    }

    public int getInt(String path, int def)
    {
        config.addDefault(path, def);
        return config.getInt(path, config.getInt(path));
    }

    private <T> List getList(String path, T def)
    {
        config.addDefault(path, def);
        return config.getList(path, config.getList(path));
    }

    public String getString(String path, String def)
    {
        return getString(path, def, true);
    }

    public String getString(String path, String def, boolean useDefault)
    {
        if (useDefault)
        {
        config.addDefault(path, def);
        }
        return config.getString(path, def);
    }

    public String getFakePlayer(String className, String defaultName)
    {
        return getString("fake-players." + className + ".username", defaultName);
    }
}
