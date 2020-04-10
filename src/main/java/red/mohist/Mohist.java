package red.mohist;

import org.apache.logging.log4j.Logger;
import red.mohist.bukkit.AutoDeletePlugins;
import red.mohist.configuration.MohistConfigUtil;
import red.mohist.down.DownloadLibraries;
import red.mohist.down.UpdateUtils;
import red.mohist.forge.AutoDeleteMods;
import red.mohist.util.EulaUtil;
import red.mohist.util.i18n.Message;

import java.io.*;
import java.util.Scanner;

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

        try {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, "Cp850"));
        } catch (UnsupportedEncodingException e) {
            throw new InternalError("VM does not support mandatory encoding UTF-8");
        }

        if(System.getProperty("log4j.configurationFile") == null)
            System.setProperty("log4j.configurationFile", "log4j2_mohist.xml");
        MohistConfigUtil.copyMohistConfig();

        if(!EulaUtil.hasAcceptedEULA()) {
            System.out.println(Message.getString("eula"));
            while (!"true".equals(new Scanner(System.in).next())) ;
            EulaUtil.writeInfos();
        }
        if(UpdateUtils.isCheckLibs()) DownloadLibraries.run();
        if(UpdateUtils.isCheckUpdate()) UpdateUtils.versionCheck();
        if(!MohistConfigUtil.getBoolean(new File("mohist-config", "mohist.yml"), "disable_plugins_blacklist:"))
            AutoDeletePlugins.jar();
        if(!MohistConfigUtil.getBoolean(new File("mohist-config", "mohist.yml"), "disable_mods_blacklist:"))
            AutoDeleteMods.jar();
        AutoDeleteMods.jarDisabled();

        Class.forName("net.minecraftforge.fml.relauncher.ServerLaunchWrapper").getDeclaredMethod("main", String[].class).invoke(null, new Object[]{args});
    }
}
