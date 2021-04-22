package com.mohistmc;

import com.mohistmc.util.PluginsModsDelete;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.mohistmc.util.PluginsModsDelete.chekPlugins;

/**
 * @author Mgazul
 * @date 2020/3/22 23:15
 */
public class AutoDeletePlugins {

    public static List<PluginsModsDelete.FixPlugin> plugins_fix = new ArrayList<>();
    public static List<String> plugins_not_compatible = new ArrayList<>();

    public static void init() {
        plugins_fix.add(new PluginsModsDelete.FixPlugin("com.earth2me.essentials.Essentials",
                "https://cdn.discordapp.com/attachments/747808399634202706/822855068210167848/EssentialsX-2.19.0-dev97-bfeb0ef-all.jar",
                "2.19.0-dev+97-bfeb0ef"));

        plugins_not_compatible.add("com.comphenix.protocol.ProtocolLib"); // Until it is resolved, the plug-in is not allowed
    }

    public static void jar() throws Exception {
        for (PluginsModsDelete.FixPlugin fix : plugins_fix) {
            chekPlugins(fix);
        }
        for (String mainClass : plugins_not_compatible) {
            chekPlugins(mainClass);
        }
    }
}
