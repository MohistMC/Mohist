package red.mohist;

import java.util.Scanner;
import org.apache.logging.log4j.Logger;
import red.mohist.bukkit.AutoDeletePlugins;
import red.mohist.bukkit.nms.MappingFix;
import red.mohist.configuration.MohistConfigUtil;
import red.mohist.forge.AutoDeleteMods;
import red.mohist.network.download.DownloadJava;
import red.mohist.network.download.DownloadLibraries;
import red.mohist.network.download.UpdateUtils;
import red.mohist.util.EulaUtil;
import red.mohist.util.i18n.Message;

public class Mohist {
    public static final String NAME = "Mohist";
    public static Logger LOGGER;

    public static String getVersion() {
        return (Mohist.class.getPackage().getImplementationVersion() != null) ? Metrics.class.getPackage().getImplementationVersion() : "unknown";
    }

    public static void main(String[] args) throws Throwable {
        if (Float.parseFloat(System.getProperty("java.class.version")) != 52.0 || MohistConfigUtil.bMohist("use_custom_java8", "false"))
            DownloadJava.run(args);

        MohistConfigUtil.copyMohistConfig();
        System.out.println("\n" + "\n" +
                " __    __   ______   __  __   __   ______   ______  \n" +
                "/\\ \"-./  \\ /\\  __ \\ /\\ \\_\\ \\ /\\ \\ /\\  ___\\ /\\__  _\\ \n" +
                "\\ \\ \\-./\\ \\\\ \\ \\/\\ \\\\ \\  __ \\\\ \\ \\\\ \\___  \\\\/_/\\ \\/ \n" +
                " \\ \\_\\ \\ \\_\\\\ \\_____\\\\ \\_\\ \\_\\\\ \\_\\\\/\\_____\\  \\ \\_\\ \n" +
                "  \\/_/  \\/_/ \\/_____/ \\/_/\\/_/ \\/_/ \\/_____/   \\/_/ \n" +
                "                                                    \n" + "\n");
        System.out.println("                                      " +
                Message.getString("forge.serverlanunchwrapper.1"));

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
        if (!MohistConfigUtil.bMohist("disable_plugins_blacklist")) AutoDeletePlugins.jar();
        if (!MohistConfigUtil.bMohist("disable_mods_blacklist")) AutoDeleteMods.jar();

        Class.forName("net.minecraftforge.fml.relauncher.ServerLaunchWrapper").getDeclaredMethod("main", String[].class).invoke(null, new Object[]{args});
    }
}