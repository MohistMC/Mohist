package com.mohistmc;

import com.mohistmc.configuration.MohistConfig;
import java.util.Scanner;
import org.apache.logging.log4j.Logger;
import com.mohistmc.bukkit.AutoDeletePlugins;
import com.mohistmc.bukkit.nms.MappingFix;
import com.mohistmc.configuration.MohistConfigUtil;
import com.mohistmc.forge.AutoDeleteMods;
import com.mohistmc.network.download.DownloadJava;
import com.mohistmc.network.download.DownloadLibraries;
import com.mohistmc.network.download.UpdateUtils;
import com.mohistmc.util.EulaUtil;
import com.mohistmc.util.i18n.Message;

import static com.mohistmc.forge.FastWorkBenchConf.changeConf;

public class MohistMC {
    public static final String NAME = "Mohist";
    public static Logger LOGGER;

    public static String getVersion() {
        return (MohistMC.class.getPackage().getImplementationVersion() != null) ? MohistMC.class.getPackage().getImplementationVersion() : "unknown";
    }

    public static void main(String[] args) throws Throwable {
        MohistConfigUtil.copyMohistConfig();
        if (Float.parseFloat(System.getProperty("java.class.version")) != 52.0 || MohistConfigUtil.bMohist("use_custom_java8", "false"))
            DownloadJava.run(args);
        if(MohistConfigUtil.bMohist("showlogo")) {
            System.out.println("\n" + "\n" +
                    " __    __   ______   __  __   __   ______   ______  \n" +
                    "/\\ \"-./  \\ /\\  __ \\ /\\ \\_\\ \\ /\\ \\ /\\  ___\\ /\\__  _\\ \n" +
                    "\\ \\ \\-./\\ \\\\ \\ \\/\\ \\\\ \\  __ \\\\ \\ \\\\ \\___  \\\\/_/\\ \\/ \n" +
                    " \\ \\_\\ \\ \\_\\\\ \\_____\\\\ \\_\\ \\_\\\\ \\_\\\\/\\_____\\  \\ \\_\\ \n" +
                    "  \\/_/  \\/_/ \\/_____/ \\/_/\\/_/ \\/_/ \\/_____/   \\/_/ \n" +
                    "                                                    \n" + "\n");
            System.out.println("                                      " +
                    Message.getString("forge.serverlanunchwrapper.1"));
        }
        if (System.getProperty("log4j.configurationFile") == null) {
            System.setProperty("log4j.configurationFile", "log4j2_mohist.xml");
        }

        if (MohistConfigUtil.bMohist("check_libraries")) DownloadLibraries.run();

        MappingFix.init();

        if (!EulaUtil.hasAcceptedEULA()) {
            System.out.println(Message.getString("eula"));
            while (!"true".equals(new Scanner(System.in).next())) ;
            EulaUtil.writeInfos();
        }

        if (MohistConfigUtil.bMohist("check_update")) UpdateUtils.versionCheck();
        if (!MohistConfigUtil.bMohist("disable_plugins_blacklist", "false")) AutoDeletePlugins.jar();
        if (!MohistConfigUtil.bMohist("disable_mods_blacklist", "false")) AutoDeleteMods.jar();
        changeConf();

        Class.forName("net.minecraftforge.fml.relauncher.ServerLaunchWrapper").getDeclaredMethod("main", String[].class).invoke(null, new Object[]{args});
    }
}
