package cc.uraniummc;

import cc.uraniummc.command.UraniumCommand;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.cauldron.configuration.BoolSetting;
import net.minecraftforge.cauldron.configuration.ConfigBase;
import net.minecraftforge.cauldron.configuration.IntSetting;
import net.minecraftforge.cauldron.configuration.Setting;
import net.minecraftforge.cauldron.configuration.StringSetting;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.configuration.file.YamlConfiguration;

public class UraniumConfig extends ConfigBase {
    public BoolSetting commandEnable = new BoolSetting(this, "command.enable",
            true, "Enable Uranium command");
    public StringSetting language = new StringSetting(this,"language","en_US","Set server i18n language (You can show any different language such as Chinese(zh_CN), Japanese(ja_JP) like client.)");
    public BoolSetting updatecheckerEnable = new BoolSetting(this,
            "updatechecker.enable", false, "Enable Uranium update checker");
    public StringSetting updatecheckerSymlinks = new StringSetting(this,
            "updatechecker.symlinks", "Uranium.jar", "(Re)create symlinks after update");
    public BoolSetting updatecheckerAutoinstall = new BoolSetting(this,
            "updatechecker.autoinstall", false, "Install updates without confirming");
    public BoolSetting updatecheckerAutorestart = new BoolSetting(this,
            "updatechecker.autorestart", false, "Restart server after updating without confirming (set restart script in spigot.yml)");
    public BoolSetting updatecheckerQuiet = new BoolSetting(this,
            "updatechecker.quiet", false, "Print less info during update");

    public BoolSetting loggingMaterialInjection = new BoolSetting(this,
            "logging.materialInjection", false, "Log material injection event");
    public BoolSetting loggingClientModList = new BoolSetting(this,
            "logging.clientModList", true, "Print client's mod list during attempt to join");
        
    public BoolSetting commonAllowNetherPortal = new BoolSetting(this,
            "common.allowNetherPortalBesidesOverworld", false, "Allow nether portals in dimensions besides overworld");
    public BoolSetting commonFastLeavesDecayEnable = new BoolSetting(this,
            "common.fastLeavesDecay.enable", false, "Enable fast decaying of leaves, not affects drop chanches /etc");
    public IntSetting commonFastLeavesDecayMinTickTime = new IntSetting(this,
            "common.fastLeavesDecay.minTickTime", 5, "Minimal amount of tick between block updates");
    public IntSetting commonFastLeavesDecayMaxTickTime = new IntSetting(this,
            "common.fastLeavesDecay.maxTickTime", 10, "Minimal amount of tick between block updates");
    public IntSetting commonMaxChunkGenPerTick = new IntSetting(this,
            "common.maxChunkGenPerTick", 1, "How many chunks generate during tick");

    public BoolSetting experimentalTileEntityListRecreation = new BoolSetting(this,
            "experimental.tileEntityListRecreation", false, "EXPERIMENTAL! Recreate list of TE each tick.");
    
    public BoolSetting enable_oversizedchunk = new BoolSetting(this, "command.enable_oversizedchunk", true, "Enable Oversized Chunk to be saved");
    
    public IntSetting chunk_cache_size = new IntSetting(this,"command.chunk_cache_size", 256, "Size of cached chunk");

    /**
     * by xjboss<br>
     * force using offline uuid when using bungee
     */
    public BoolSetting forceuseOfflineUUID=new BoolSetting(this,"uuid.forceUseOfflineUUID",false,"Force use offline uuid when using bungeecord or fake online mode");
    /**
     * by xjboss<br>
     * offline uuid mode
     */
    public BoolSetting captureBlockOnItemRightClick=new BoolSetting(this,"capture.captureBlockOnItemRightClick",true,"Capture block to prevent brush things.");
    public BoolSetting captureBlockOnMetaChange=new BoolSetting(this,"capture.captureBlockOnMetaChange",true,
            "Capture block to prevent such wrench change direction of block.");
    public IntSetting uuidMode=new IntSetting(this,"uuid.mode",0,"Offline UUID Mode 0 is normal mode 1 is lowercase mode 2 is upcase mode");
    public BoolSetting usingCustomOnlineModeServer=new BoolSetting(this,"onlinemode.usingCustomServer",false,"Using custom online mode server like netease \"\u6211\u7684\u4e16\u754c\"");
    public StringSetting customOnlineModeServer =new StringSetting(this,"onlinemode.customServer","https://sessionserver.mojang.com/session/minecraft/join","Custom online mode server URL");
    public BoolSetting onlyConsoleOP=new BoolSetting(this,"op.onlyConsole",false,"Only allow console using op command");
    public BoolSetting allowShowCommandThrowableOnClient =new BoolSetting(this,"command.allowShowCommandThrowableOnClient",true,"Allow show throwable information on client.");
    public BoolSetting enableGuava17=new BoolSetting(this,"experimental.guava17",false,"EXPERIMENTAL! Using guava17 to replace guava 10 in com.google.common package. Lots of plugins will not working.");
    public BoolSetting enableGuava21=new BoolSetting(this,"experimental.guava21",false,"EXPERIMENTAL! Using guava21 to replace guava 10 in com.google.common package. Lots of plugins will not working.");
    public StringSetting uraniumName=new StringSetting(this,"experimental.UraniumName","","EXPERIMENTAL! Some plugins not support Uranium as server name, you can change it to KCauldron or Cauldron to improve compatibility");
    public BoolSetting remapReflection=new BoolSetting(this,"plugin-settings.default.remap-Reflection",true,"This options can remap Class.forName getMethod getField to support plugins which using the net.minecraft.server.Rxxx package, but it will make some plugin work slow, if server is slow, can disable this option or only enabled for some plugins.");
    //public BoolSetting fakeVanillaMode= new BoolSetting(this,"experimental.fakeVanillaMode",false,"EXPERIMENTAL! Make client think this is a Vanilla server.");
    @Deprecated
    public BoolSetting enableSQLite321=new BoolSetting(this,"experimental.remap-to-sqlite321",false,"EXPERIMENTAL! Using new sqlite to replace old sqlite. Lots of old plugins will not working.");
    public static boolean tileEntityListRecreation;
    
    private List<String> mBlockNoCaptureMetaStr=new ArrayList();
    private HashSet mBlockNoCaptureMeta=null;
    public BoolSetting enableForgeRespawnClone =new BoolSetting(this,"forge.clone-player-on-respawn",false,"Original forge will clone a new player when player respawn, some mods needs this feature.");
    public UraniumConfig() {
        super("uranium.yml", "um");
        for(Field sField : UraniumConfig.class.getDeclaredFields()){
            if(!Setting.class.isAssignableFrom(sField.getType())||sField.getAnnotation(Deprecated.class)!=null)
                continue;

            sField.setAccessible(true);
            try {
                Setting tValue = (Setting)FieldUtils.readField(sField, this);
                if(tValue!=null)
                    register(tValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
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
        commands.put(commandName, new UraniumCommand(commandName));
    }

    private void addd(String path,Object obj){
        config.addDefault(path,obj);
    }
    private void setExtraDefault(){
        addd("plugin-settings.PermissionsEx.remap-guava17-ver","*^(1.23|2)");
        addd("plugin-settings.WorldGuard.remap-guava17-ver","*^(\\d[6-9]|\\d\\d+)");
        addd("plugin-settings.LuckPerms.remap-guava17",true);
        addd("plugin-settings.ProtocolLib.remap-Reflection",false);
        addd("plugin-settings.MyPet.remap-Reflection",true);

        addd("capture.blockNotCaptureOnMetaChange",new String[]{"minecraft:stone_button","minecraft:wooden_button",
                "minecraft:wooden_door","minecraft:iron_door","minecraft:trapdoor",
                "minecraft:wooden_pressure_plate","minecraft:stone_pressure_plate",
                "minecraft:light_weighted_pressure_plate","minecraft:heavy_weighted_pressure_plate",
                "minecraft:lever","minecraft:fence_gate"});
    }
    @Override
    protected void load() {
        try {
            config = YamlConfiguration.loadConfiguration(configFile);
            setExtraDefault();

            String header = "";
            for (Setting<?> toggle : settings.values()) {
                if (!toggle.description.equals(""))
                    header += "Setting: " + toggle.path + " Default: "
                            + toggle.def + " # " + toggle.description + "\n";

                config.addDefault(toggle.path, toggle.def);
                settings.get(toggle.path).setValue(
                        config.getString(toggle.path));
            }

            this.mBlockNoCaptureMetaStr=config.getStringList("capture.blockNotCaptureOnMetaChange");
            this.mBlockNoCaptureMeta=null;

            config.options().header(header);
            config.options().copyDefaults(true);
            save();
        } catch (Exception ex) {
            MinecraftServer.getServer().logSevere(
                    "Could not load " + this.configFile);
            ex.printStackTrace();
        }
        tileEntityListRecreation = experimentalTileEntityListRecreation.getValue();
    }
    
    public boolean shouldBlockCaptureOnMetaChange(Block pBlock,int pMeta){
        if(this.mBlockNoCaptureMeta==null){
            this.mBlockNoCaptureMeta=new HashSet();
            for(String sStr: this.mBlockNoCaptureMetaStr){
                Object t = Block.blockRegistry.getObject(sStr);
                if(t!=null) this.mBlockNoCaptureMeta.add(t);
            }
        }

        return !this.mBlockNoCaptureMeta.contains(pBlock);
    }
}
