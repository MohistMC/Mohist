package com.mohistmc;

import com.mohistmc.bukkit.AutoDeletePlugins;
import com.mohistmc.configuration.MohistConfigUtil;
import com.mohistmc.forge.AutoDeleteMods;
import com.mohistmc.forge.FastWorkBenchConf;
import com.mohistmc.libraries.CustomLibraries;
import com.mohistmc.libraries.DefaultLibraries;
import com.mohistmc.network.download.DownloadJava;
import com.mohistmc.network.download.UpdateUtils;
import com.mohistmc.util.EulaUtil;
import com.mohistmc.util.Logo;
import com.mohistmc.util.i18n.Message;
import java.lang.management.ManagementFactory;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MohistMC {
    public static final String NAME = "Mohist";
    public static Logger LOGGER;
    public static ArrayList<String> mainArgs = null;

    public static String getVersion() {
        return (MohistMC.class.getPackage().getImplementationVersion() != null) ? MohistMC.class.getPackage().getImplementationVersion() : "unknown";
    }

    public static void main(String[] args) throws Throwable {
        mainArgs = new ArrayList<>(Arrays.asList(args));
        MohistConfigUtil.copyMohistConfig();
        if (Float.parseFloat(System.getProperty("java.class.version")) != 52.0 || MohistConfigUtil.bMohist("use_custom_java8", "false"))
            DownloadJava.run();
        if (MohistConfigUtil.bMohist("showlogo")) {
            System.out.printf("%n%s%n%s - %s  Java(%s) %s PID: %s%n",
                    Logo.asMohist(),
                    Message.getString("mohist.launch.welcomemessage"),
                    getVersion(),
                    System.getProperty("java.class.version"),
                    System.getProperty("java.version"),
                    ManagementFactory.getRuntimeMXBean().getName().split("@")[0]
            );
            if (Message.isCN()) {
                System.out.println("+------------------------------------------------------+");
                System.out.println("|                                                      |");
                System.out.println("| 官方交流QQ群: 570870451                              |");
                System.out.println("| 官网(中国): https://www.mohistmc.cn/                 |");
                System.out.println("| 爱发电: https://afdian.com/a/MohistMC                |");
                System.out.println("|                                                      |");
                System.out.println("+------------------------------------------------------+");
            }
        }
        if (System.getProperty("log4j.configurationFile") == null) {
            System.setProperty("log4j.configurationFile", "log4j2_mohist.xml");
        }

        if (MohistConfigUtil.bMohist("check_libraries")) DefaultLibraries.run();
        DefaultLibraries.loadDefaultLibs();
        CustomLibraries.loadCustomLibs();

        // if (MohistConfigUtil.bMohist("check_update")) UpdateUtils.versionCheck();

        if (mainArgs.contains("-noserver"))
            System.exit(0); //-noserver -> Do not run the Minecraft server, only let the installation running.

        Class.forName("com.google.gson.internal.bind.TypeAdapters$EnumTypeAdapter").getClassLoader();

        if (!EulaUtil.hasAcceptedEULA()) {
            System.out.println(Message.getString("eula"));
            while (!"true".equals(new Scanner(System.in).next())) ;
            EulaUtil.writeInfos();
        }

        if (!MohistConfigUtil.bMohist("disable_plugins_blacklist", "false")) AutoDeletePlugins.jar();
        if (!MohistConfigUtil.bMohist("disable_mods_blacklist", "false")) AutoDeleteMods.jar();
        FastWorkBenchConf.changeConf();
        Class.forName("net.minecraftforge.fml.relauncher.ServerLaunchWrapper").getDeclaredMethod("main", String[].class).invoke(null, new Object[]{args});
    }
}
