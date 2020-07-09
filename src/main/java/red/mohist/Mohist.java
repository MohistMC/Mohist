package red.mohist;

import org.apache.logging.log4j.Logger;
import red.mohist.bukkit.AutoDeletePlugins;
import red.mohist.bukkit.nms.MappingFix;
import red.mohist.configuration.MohistConfigUtil;
import red.mohist.network.download.DownloadLibraries;
import red.mohist.network.download.UpdateUtils;
import red.mohist.util.i18n.Message;

import java.util.Scanner;

import static red.mohist.configuration.MohistConfigUtil.bMohist;
import static red.mohist.forge.AutoDeleteMods.jar;
import static red.mohist.util.EulaUtil.hasAcceptedEULA;
import static red.mohist.util.EulaUtil.writeInfos;

public class Mohist {

  public static final String NAME = "Mohist";
  public static Logger LOGGER;

  public static String getVersion() {
    return Mohist.class.getPackage().getImplementationVersion() != null ? Metrics.class.getPackage().getImplementationVersion() : "unknown";
  }

  public static void main(String[] args) throws Throwable {
    if(Float.parseFloat(System.getProperty("java.class.version")) != 52.0) {
      System.out.println(Message.getString("unsupported.java.version"));
      System.exit(0);
    }
    if(System.getProperty("log4j.configurationFile") == null) {
      System.setProperty("log4j.configurationFile", "log4j2_mohist.xml");
    }
    System.out.println("\n" +
      "\n" +
      " __    __   ______   __  __   __   ______   ______  \n" +
      "/\\ \"-./  \\ /\\  __ \\ /\\ \\_\\ \\ /\\ \\ /\\  ___\\ /\\__  _\\ \n" +
      "\\ \\ \\-./\\ \\\\ \\ \\/\\ \\\\ \\  __ \\\\ \\ \\\\ \\___  \\\\/_/\\ \\/ \n" +
      " \\ \\_\\ \\ \\_\\\\ \\_____\\\\ \\_\\ \\_\\\\ \\_\\\\/\\_____\\  \\ \\_\\ \n" +
      "  \\/_/  \\/_/ \\/_____/ \\/_/\\/_/ \\/_/ \\/_____/   \\/_/ \n" +
      "                                                    \n" +
      "\n");
    System.out.println("                                      " + Message.getString("forge.serverlanunchwrapper.1"));
    if(System.getProperty("log4j.configurationFile") == null) {
      System.setProperty("log4j.configurationFile", "log4j2_mohist.xml");
    }
    if(bMohist("check_libraries")) {
      DownloadLibraries.run();
    }
    MappingFix.init();
    MohistConfigUtil.copyMohistConfig();
    if(!hasAcceptedEULA()) {
      System.out.println(Message.getString("eula"));
      while (!"true".equals(new Scanner(System.in).next())) ;
      writeInfos();
    }
    if(bMohist("check_update")) {
      UpdateUtils.versionCheck();
    }
    if(!bMohist("disable_plugins_blacklist")) {
      AutoDeletePlugins.jar();
    }
    if(!bMohist("disable_mods_blacklist")) {
      jar((byte) 1);
    }
    jar((byte) 2);

    Class.forName("net.minecraftforge.fml.relauncher.ServerLaunchWrapper").getDeclaredMethod("main", String[].class).invoke(null, new Object[]{args});
  }
}
