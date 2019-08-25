package red.mohist.i18n;

import red.mohist.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public enum Message {
    Mohist_Test("mohist.test"),
    Mohist_Start("mohist.start"),
    Mohist_Server_Start("mohist.server.start"),
    Mohist_Start_Error("mohist.start.error"),
    Not_Have_Library("mohist.start.error.nothavelibrary"),

    Dw_File("file.download"),
    Dw_Start("file.download.start"),
    Dw_Now("file.download.now"),
    Dw_Ok("file.download.ok"),

    UnZip_Start("file.unzip.start"),
    UnZip_Now("file.unzip.now"),
    UnZip_Ok("file.unzip.ok"),

    Mohist_Load_Map("mohist.load.map"),
    Mohist_Load_Map_Spawn("mohist.load.map.spawn"),
    Mohist_Stop("mohist.stop"),
    Mohist_Start_Fail("mohist.start.fail"),
    Mohist_Save_Players("mohist.save.players"),
    Mohist_Save_Worlds("mohist.save.worlds"),
    Mohist_Save_Other("mohist.save.other"),
    Load_libraries("load.libraries"),
    Server_Ip("server.ip"),
    EULA("eula"),
    EULA_LOAD_FAIL("eula.load.fail"),
    EULA_SAVE_FAIL("eula.save.fail"),
    EULA_TEXT("eula.text"),
    ERROR_START_DIRECTORY("error.start.directory"),
    error_load_icon("error.load.icon"),
    error_load_icon_wide("error.load.icon.wide"),
    error_load_icon_high("error.load.icon.high"),
    Load_dimension("load.dimension"),
    UnLoad_dimension("unload.dimension"),
    World_settings("world.settings"),
    whitelistMessage("message.whitelist"),
    serverFullMessage("message.serverfull"),
    bungeecord("message.bungeecord"),
    disconnect_flying("disconnect.flying"),
    disconnect_duplicate_login("disconnect.duplicate_login"),
    client_join_mods("client.join.mods"),
    save_chunks_level("save.chunks.level"),
    crash_report("crash.report"),
    crash_report1("crash.report1"),
    crash_report2("crash.report2"),
    crash_report3("crash.report3"),
    crash_report4("crash.report4"),
    crash_mc_version("crash.mc.version"),
    crash_system("crash.system"),
    crash_version("crash.version"),
    crash_java_version("crash.java.version"),
    crash_jvm_version("crash.jvm.version"),
    crash_Memory("crash.memory"),
    mohist_bytes("mohist.bytes"),
    crash_jvm_flags("crash.jvm.flags"),
    crash_craftbukkit_info("crash.craftbukkit.info"),
    crash_thread("crash.thread"),
    crash_Stacktrace("crash.stacktrace"),
    crash_mc_report("crash.mc.report"),
    crash_msg("crash.msg"),
    crash_save_error("crash.save.error"),
    crash_Negative_index("crash.negative.index"),

    other_upto("other.upto"),
    other_total("other.total"),
    other_IntCache("other.intcache"),
    other_head("other.head"),
    other_time("other.time"),
    other_Description("other.description"),

    command_nopermission("command.nopermission"),
    pluginscommand_load("pluginscommand.load"),
    pluginscommand_unload("pluginscommand.unload"),
    pluginscommand_reload("pluginscommand.reload"),
    pluginscommand_loaded("pluginscommand.loaded"),
    pluginscommand_unloaded("pluginscommand.unloaded"),
    pluginscommand_reloaded("pluginscommand.reloaded"),
    pluginscommand_noplugin("pluginscommand.noplugin"),
    pluginscommand_nofile("pluginscommand.nofile"),
    pluginscommand_noyml("pluginscommand.noyml"),
    pluginscommand_alreadyloaded("pluginscommand.alreadyloaded"),
    pluginscommand_notload("pluginscommand.notload"),
    pluginscommand_notunload("pluginscommand.notunload"),
    pluginscommand_nojar("pluginscommand.nojar"),
    pluginscommand_unloaderror("pluginscommand.unloaderror"),
    pluginscommand_reloaderror("pluginscommand.reloaderror"),

    bukkit_plugin_noyml("bukkit.plugin.noyml"),
    bukkit_plugin_enabling("bukkit.plugin.enabling"),
    bukkit_plugin_enablingunreg("bukkit.plugin.enablingunreg"),
    bukkit_plugin_enablingerror("bukkit.plugin.enablingerror"),
    bukkit_plugin_disabling("bukkit.plugin.disabling"),
    bukkit_plugin_disablingerror("bukkit.plugin.disablingerror"),

    craftserver_addworld("craftserver.addworld"),

    growth_modifier_defaulting("growth.modifier.defaulting"),
    growth_modifier("growth.modifier"),
    merge_radius_item("merge.radius.item"),
    merge_radius_exp("merge.radius.exp"),
    view_distance("view.distance"),
    mob_spawn_range("mob.spawn.range"),
    item_despawn_rate("item.despawn.rate"),
    entity_activation_range("entity.activation.range"),
    entity_tracking_range("entity.tracking.range"),
    ticks_per_hopper_transfer("ticks.per.hopper.transfer"),
    random_light_updates("random.light.updates"),
    save_structure_info("save.structure.info"),
    save_structure_info_error("save.structure.info.error"),
    save_structure_info_error1("save.structure.info.error1"),
    arrow_despawn_rate("arrow.despawn.rate"),
    zombie_aggressive_towards_villager("zombie.aggressive.towards.villager"),
    nerf_spawner_mobs("nerf.spawner.mobs"),
    enable_zombie_pigmen_portal_spawns("enable.zombie.pigmen.portal.spawns"),
    custom_map_seeds("custom.map.seeds"),
    max_tnt_per_tick("max-tnt.per.tick"),
    max_tick_time_tile("max.tick.time.tile"),

    Exception_Could_not_load_plugin("exception.could.not.load.plugin"),
    Exception_plugin_not_hav_depend("exception.plugin.not.hav.depend"),
    Exception_Invalid_Plugin("exception.invalid.plugin"),
    Exception_Invalid_Description("exception.invalid.description"),

    Use_Unkonw_Comamnd("use.unknow.command"),
    outdated_Client("outdate.client"),
    outdated_Server("outdate.server"),

    Server_Start_Done("server.start.done"),

    Mohist_update_program("mohist.update.program"),
    Mohist_update_program_check_hasupdate("mohist.update.program.check.hasupdate"),
    Mohist_update_program_check_noupdate("mohist.update.program.check.noupdate"),
    Mohist_update_program_tips_stopautoget("mohist.update.program.tips.stopautoget"),
    Mohist_update_message("mohist.update.message"),
    Mohist_update_date("mohist.update.date"),
    Mohist_update_program_tips_done("mohist.update.program.tips.done"),
    Mohist_update_program_tips_false("mohist.update.program.tips.false"),

    Watchdog_1("watchdog.1"),
    Watchdog_2("watchdog.2"),
    Watchdog_3("watchdog.3"),
    Watchdog_4("watchdog.4"),
    Watchdog_5("watchdog.5"),
    Watchdog_6("watchdog.6"),
    Watchdog_7("watchdog.7"),
    Watchdog_8("watchdog.8"),
    Watchdog_9("watchdog.9"),
    Watchdog_10("watchdog.10"),
    Watchdog_11("watchdog.11"),
    Watchdog_12("watchdog.12"),
    Watchdog_13("watchdog.13"),
    Watchdog_14("watchdog.14"),
    Watchdog_15("watchdog.15"),
    Watchdog_16("watchdog.16"),

    forge_loader_1("forge.loader.1"),
    forge_loader_2("forge.loader.2"),
    forge_loader_3("forge.loader.3"),
    forge_loader_4("forge.loader.4"),
    forge_loader_5("forge.loader.5"),
    forge_loader_6("forge.loader.6"),
    forge_loader_7("forge.loader.7"),
    forge_loader_8("forge.loader.8"),
    forge_loader_9("forge.loader.9"),
    forge_loader_10("forge.loader.10"),
    forge_loader_11("forge.loader.11"),
    forge_loader_12("forge.loader.12"),
    forge_loader_13("forge.loader.13"),
    forge_loader_14("forge.loader.14"),
    forge_loader_15("forge.loader.15"),
    forge_loader_16("forge.loader.16"),
    forge_loader_17("forge.loader.17"),
    forge_loader_18("forge.loader.18"),
    forge_loader_19("forge.loader.19"),
    forge_loader_20("forge.loader.20"),
    forge_loader_21("forge.loader.21"),
    forge_loader_22("forge.loader.22"),
    forge_loader_23("forge.loader.23"),
    forge_loader_24("forge.loader.24"),
    forge_loader_25("forge.loader.25"),
    forge_loader_26("forge.loader.26"),
    forge_loader_27("forge.loader.27"),
    forge_loader_28("forge.loader.28"),
    forge_loader_29("forge.loader.29"),
    forge_loader_30("forge.loader.30"),
    forge_loader_31("forge.loader.31"),
    forge_loader_32("forge.loader.32"),
    forge_loader_33("forge.loader.33"),
    forge_loader_34("forge.loader.34"),
    forge_loader_35("forge.loader.35"),
    forge_loader_36("forge.loader.36"),
    forge_loader_37("forge.loader.37"),
    forge_loader_38("forge.loader.38"),
    forge_loader_39("forge.loader.39"),
    forge_loader_40("forge.loader.40"),
    forge_loader_41("forge.loader.41"),
    forge_loader_42("forge.loader.42"),
    forge_loader_43("forge.loader.43"),
    forge_loader_44("forge.loader.44"),
    forge_loader_45("forge.loader.45"),
    forge_loader_46("forge.loader.46"),
    forge_loader_47("forge.loader.47"),
    forge_loader_48("forge.loader.48"),
    forge_loader_49("forge.loader.49"),
    forge_loader_50("forge.loader.50"),
    forge_loader_51("forge.loader.51"),
    forge_loader_52("forge.loader.52"),
    forge_loader_53("forge.loader.53"),
    forge_loader_54("forge.loader.54"),
    forge_loader_55("forge.loader.55"),
    forge_loader_56("forge.loader.56"),
    forge_loader_57("forge.loader.57"),

    Command_Register("command.register"),

    CraftBukkit_CraftServer_1("craftbukkit.craftserver.1"),

    forge_FMLServerHandler_1("forge.fmlserverhandler.1"),
    forge_FMLServerHandler_2("forge.fmlserverhandler.2"),
    forge_FMLServerHandler_3("forge.fmlserverhandler.3"),
    forge_FMLServerHandler_4("forge.fmlserverhandler.4"),

    forge_ServerLanunchWrapper_1("forge.serverlanunchwrapper.1"),

    Mohist_Dump_1("mohist.dump.1");

    public static ResourceBundle rb = ResourceBundle.getBundle("assets.mohist.lang.message", new Locale(getlanguage(1), getlanguage(2)), new UTF8Control());
    private final String value;


    Message(String value) {
        this.value = value;
    }

    @Deprecated
    public static String getString(Message key) {
        return rb.getString(key.toString());
    }

    public static String getString(String key) {
        return rb.getString(key);
    }

    @Deprecated
    public static String getFormatString(Message key, Object[] f) {
        return new MessageFormat(getString(key)).format(f);
    }

    public static String getFormatString(String key, Object[] f) {
        return new MessageFormat(getString(key)).format(f);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static String getlanguage(int key) {
        try {
            File f = new File("mohist.yml");
            String s = FileUtil.readContent(f, "UTF-8");
            if(s.contains("lang: ")){
                // Using regular expressions
                String string = s.substring(s.indexOf("lang"));
                String s1 = string.substring(string.indexOf(":") + 1);
                String s2 = s1.substring(1, 6);
                String s3 = s2.substring(0, 2);
                String s4 = s2.substring(3, 5);
                if (key == 1){
                    return s3;
                }
                if (key == 2){
                    return s4;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Returns a value that does not exist to return the system default
        return "xx";
    }
}
