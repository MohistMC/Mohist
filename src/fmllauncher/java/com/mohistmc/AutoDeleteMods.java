package com.mohistmc;

import com.mohistmc.util.PluginsModsDelete;

import java.util.ArrayList;
import java.util.List;

import static com.mohistmc.util.PluginsModsDelete.chekMods;

public class AutoDeleteMods {

    public static List<PluginsModsDelete.FixMods> mods_fix = new ArrayList<>();  //modId mod no fix/URL mod fix
    public static List<String> mods_not_compatible = new ArrayList<>();  //main class mod no compatible

    public static void init() {

    }

    public static void jar() throws Exception {
        for (PluginsModsDelete.FixMods fix : mods_fix) {
            chekMods(fix);
        }
        for (String mainClass : mods_not_compatible) {
            chekMods(mainClass);
        }
    }

}
