package com.mohistmc;

import static com.mohistmc.util.PluginsModsDelete.chekPlugins;

import java.util.ArrayList;
import java.util.List;

import com.mohistmc.util.PluginsModsDelete;

/**
 * @author Mgazul
 * @date 2020/3/22 23:15
 */
public class AutoDeletePlugins {

    public static List<PluginsModsDelete.Fix> plugins_fix = new ArrayList<>();
    public static List<String> plugins_not_compatible = new ArrayList<>();

    public static void init() {
        plugins_fix.add(new PluginsModsDelete.Fix("com.earth2me.essentials.Essentials",
                "https://github.com/KR33PY/Essentials/releases/download/essxmohist/EssentialsX-2.19.0-dev+99-fd961d5.jar",
                "2.19.0-dev+99-fd961d5"));
    }

    public static void jar() throws Exception {
        for (PluginsModsDelete.Fix fix : plugins_fix) {
            chekPlugins(fix);
        }
        for (String mainClass : plugins_not_compatible) {
            chekPlugins(mainClass);
        }
    }
}
