package red.mohist;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import red.mohist.configuration.MohistConfigUtil;
import red.mohist.down.DownloadLibraries;
import red.mohist.down.Update;
import red.mohist.util.ServerEula;
import red.mohist.util.i18n.Message;

/**
 * @author Mgazul
 * @date 2019/11/16 2:53
 */
public class Mohist {

    public static final String NAME = "Mohist";
    public static final String VERSION = "1.1";
    public static final String LIB_VERSION = "1";
    public static Logger LOGGER;

    public static String getVersion() {
        return Mohist.class.getPackage().getImplementationVersion() != null ? Metrics.class.getPackage().getImplementationVersion() : "unknown";
    }

    public static void main(String[] args) {
        MohistConfigUtil.copyMohistConfig();

        ServerEula eula = new ServerEula(new File("eula.txt"));
        if (!eula.hasAcceptedEULA()) {
            System.out.println(Message.getString("eula"));
            eula.createEULAFile();
            return;
        }
        if (Update.isCheckVersion()) {
            Update.hasLatestVersion();
        }
        if (Update.getLibrariesVersion()) {
            System.out.println(Message.getString("mohist.start.error.nothavelibrary"));
            DownloadLibraries.run();
            System.out.println(Message.getString("file.ok"));
            return;
        }
        Class<?> launchwrapper = null;
        try
        {
            launchwrapper = Class.forName("net.minecraft.launchwrapper.Launch",true, Mohist.class.getClassLoader());
            Class.forName("org.objectweb.asm.Type",true, Mohist.class.getClassLoader());
            System.out.println("");
            System.out.println("");
            System.out.println(" /'\\_/`\\          /\\ \\       __          /\\ \\__   ");
            System.out.println("/\\      \\     ___ \\ \\ \\___  /\\_\\     ____\\ \\ ,_\\  ");
            System.out.println("\\ \\ \\__\\ \\   / __`\\\\ \\  _ `\\\\/\\ \\   /',__\\\\ \\ \\/  ");
            System.out.println(" \\ \\ \\_/\\ \\ /\\ \\L\\ \\\\ \\ \\ \\ \\\\ \\ \\ /\\__, `\\\\ \\ \\_ ");
            System.out.println("  \\ \\_\\\\ \\_\\\\ \\____/ \\ \\_\\ \\_\\\\ \\_\\\\/\\____/ \\ \\__\\");
            System.out.println("   \\/_/ \\/_/ \\/___/   \\/_/\\/_/ \\/_/ \\/___/   \\/__/");
            System.out.println("");
            System.out.println("");
            System.out.println("                        " + Message.getString("forge.serverlanunchwrapper.1"));
            System.out.println(Message.getString("mohist.start"));
            System.out.println(Message.getString("load.libraries"));
            LOGGER = LogManager.getLogger("Mohist");
        }
        catch (Exception e)
        {
            System.out.println(Message.getString("mohist.start.error.nothavelibrary"));
            e.printStackTrace(System.err);
            System.exit(1);
        }

        try
        {
            Method main = launchwrapper != null ? launchwrapper.getMethod("main", String[].class) : null;
            String[] allArgs = new String[args.length + 2];
            allArgs[0] = "--tweakClass";
            allArgs[1] = "cpw.mods.fml.common.launcher.FMLServerTweaker";
            System.arraycopy(args, 0, allArgs, 2, args.length);
            Objects.requireNonNull(main).invoke(null,(Object)allArgs);
        }
        catch (Exception e)
        {
            System.out.println(Message.getString("mohist.start.error"));
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
