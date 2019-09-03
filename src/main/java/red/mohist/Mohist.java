package red.mohist;

import java.io.File;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import red.mohist.down.DownloadLibraries;
import red.mohist.down.Update;
import red.mohist.util.i18n.Message;
import red.mohist.util.ServerEula;

public class Mohist {

    private static final String NAME = "Mohist";
    private static final String VERSION = "1.4";
    private static final String NATIVE_VERSON = "v1_12_R1";
    private static final String NMS_PREFIX = "net/minecraft/server/";
    public static Logger LOGGER;

    public static String getName() {
        return NAME;
    }

    public static String getVersion() {
        return VERSION + "-" + ((Mohist.class.getPackage().getImplementationVersion() != null) ? Metrics.class.getPackage().getImplementationVersion() : "unknown");
    }

    public static String getNativeVersion() {
        return NATIVE_VERSON;
    }

    public static String getNmsPrefix() {
        return NMS_PREFIX;
    }

    public static void main(String[] args) {
        if (System.getProperty("log4j.configurationFile") == null)
        {
            // Set this early so we don't need to reconfigure later
            System.setProperty("log4j.configurationFile", "log4j2_mohist.xml");
        }

        ServerEula eula = new ServerEula(new File("eula.txt"));
        if (!eula.hasAcceptedEULA())
        {
            System.out.println(Message.getString("eula"));
            eula.createEULAFile();
            return;
        }
        if (Update.isCheckVersion()) {
            Update.hasLatestVersion();
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
            System.out.println("");
            System.out.println(Message.getString("mohist.start"));
            System.out.println(Message.getString("load.libraries"));
            LOGGER = LogManager.getLogger("Mohist");
        }
        catch (Exception e)
        {
            System.out.println(Message.getString("mohist.start.error.nothavelibrary"));
            System.out.println("");
            ExecutorService dl = Executors.newCachedThreadPool();
            dl.execute(new DownloadLibraries());
            System.out.println(Message.getString("file.ok"));
        }

        try
        {
            Method main = null;
            try{
                main = launchwrapper.getMethod("main",String[].class);
            }
            catch(NullPointerException e)
            {
                return;
            }
            String[] allArgs = new String[args.length + 2];
            allArgs[0] = "--tweakClass";
            allArgs[1] = "net.minecraftforge.fml.common.launcher.FMLServerTweaker";
            System.arraycopy(args, 0, allArgs, 2, args.length);
            main.invoke(null,(Object)allArgs);
        }
        catch (Exception e)
        {
            System.out.println(Message.getString("mohist.start.error"));
            e.printStackTrace(System.err);
        }
    }
}
