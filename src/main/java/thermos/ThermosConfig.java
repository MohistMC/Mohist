package thermos;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.cauldron.configuration.BoolSetting;
import net.minecraftforge.cauldron.configuration.ConfigBase;
import net.minecraftforge.cauldron.configuration.Setting;
import org.bukkit.configuration.file.YamlConfiguration;

public class ThermosConfig extends ConfigBase {
    public BoolSetting commandEnable = new BoolSetting(this, "command.enable", true, "Enable Thermos command");
    public BoolSetting opConsoleOnly = new BoolSetting(this, "op.consoleonly", false, "Set the OP command to only be allowed to run in console");
    public BoolSetting updatecheckerEnable = new BoolSetting(this, "updatechecker.enable", true, "Enable Thermos update checker");
    public BoolSetting updatecheckerQuiet = new BoolSetting(this, "updatechecker.quiet", false, "Print less info during update");

    public BoolSetting loggingMaterialInjection = new BoolSetting(this, "logging.materialInjection", false, "Log material injection event");
    public BoolSetting loggingClientModList = new BoolSetting(this, "logging.clientModList", true, "Print client's mod list during attempt to join");
        
    public BoolSetting commonAllowNetherPortal = new BoolSetting(this, "common.allowNetherPortalBesidesOverworld", false, "Allow nether portals in dimensions besides overworld");
    
    public ThermosConfig() {
        super("thermos.yml", "thermos");
        register(commandEnable);
        register(updatecheckerEnable);
        register(updatecheckerQuiet);
        register(loggingMaterialInjection);
        register(loggingClientModList);
        register(commonAllowNetherPortal);
        register(opConsoleOnly);
        load();
    }

    private void register(Setting<?> setting) {
        settings.put(setting.path, setting);
    }

    @Override
    public void registerCommands() {
        if (commandEnable.getValue()) {
            super.registerCommands();
        }
    }

    @Override
    protected void addCommands() {
        commands.put(commandName, new ThermosCommand());
    }

    @Override
    protected void load() {
        try {
            config = YamlConfiguration.loadConfiguration(configFile);
            String header = "";
            for (Setting<?> toggle : settings.values()) {
                if (!toggle.description.equals(""))
                    header += "Setting: " + toggle.path + " Default: "
                            + toggle.def + " # " + toggle.description + "\n";

                config.addDefault(toggle.path, toggle.def);
                settings.get(toggle.path).setValue(
                        config.getString(toggle.path));
            }
            config.options().header(header);
            config.options().copyDefaults(true);
            save();
        } catch (Exception ex) {
            MinecraftServer.getServer().logSevere(
                    "Could not load " + this.configFile);
            ex.printStackTrace();
        }
    }
}