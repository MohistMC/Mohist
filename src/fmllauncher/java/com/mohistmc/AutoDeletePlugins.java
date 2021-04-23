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
                "https://cdn.discordapp.com/attachments/835087766320316437/835087973598494720/EssentialsX-2.19.0-dev99-fd961d5.jar",
                "2.19.0-dev+99-fd961d5")); // Essential

        plugins_fix.add(new PluginsModsDelete.FixPlugin("com.earth2me.essentials.antibuild.EssentialsAntiBuild",
                "https://cdn.discordapp.com/attachments/835087766320316437/835087827561086976/EssentialsXAntiBuild-2.19.0-dev99-fd961d5.jar",
                "2.19.0-dev+99-fd961d5")); // essential antiBuild

        plugins_fix.add(new PluginsModsDelete.FixPlugin("com.earth2me.essentials.chat.EssentialsChat",
                "https://cdn.discordapp.com/attachments/835087766320316437/835087830329327616/EssentialsXChat-2.19.0-dev99-fd961d5.jar",
                "2.19.0-dev+99-fd961d5")); // Essential chat

        plugins_fix.add(new PluginsModsDelete.FixPlugin("com.earth2me.essentials.geoip.EssentialsGeoIP",
                "https://cdn.discordapp.com/attachments/835087766320316437/835087962093256745/EssentialsXGeoIP-2.19.0-dev99-fd961d5.jar",
                "2.19.0-dev+99-fd961d5")); // Essential geoIp

        plugins_fix.add(new PluginsModsDelete.FixPlugin("com.earth2me.essentials.protect.EssentialsProtect",
                "https://cdn.discordapp.com/attachments/835087766320316437/835087816597831700/EssentialsXProtect-2.19.0-dev99-fd961d5.jar",
                "2.19.0-dev+99-fd961d5")); // Essential protect

        plugins_fix.add(new PluginsModsDelete.FixPlugin("com.earth2me.essentials.spawn.EssentialsSpawn",
                "https://cdn.discordapp.com/attachments/835087766320316437/835087819126603816/EssentialsXSpawn-2.19.0-dev99-fd961d5.jar",
                "2.19.0-dev+99-fd961d5")); // Essential spawn

        plugins_fix.add(new PluginsModsDelete.FixPlugin("com.earth2me.essentials.xmpp.EssentialsXMPP",
                "https://cdn.discordapp.com/attachments/835087766320316437/835087863837229116/EssentialsXXMPP-2.19.0-dev99-fd961d5.jar",
                "2.19.0-dev+99-fd961d5")); // Essential XMPP

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
