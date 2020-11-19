package com.mohistmc;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.mohistmc.configuration.MohistConfigUtil;
import com.mohistmc.down.DownloadLibraries;
import com.mohistmc.down.Update;
import com.mohistmc.util.ServerEula;
import com.mohistmc.util.i18n.Message;

/**
 * @author Mgazul
 * @date 2019/11/16 2:53
 */
public class MohistMC {

    public static final String NAME = "Mohist";
    public static final String VERSION = "1.1";
    public static final String LIB_VERSION = "1.1";

    public static String getVersion() {
        return MohistMC.class.getPackage().getImplementationVersion() != null ? MohistMC.class.getPackage().getImplementationVersion() : "unknown";
    }

    public static void main(String[] args) {
        MohistConfigUtil.copyMohistConfig();

        ServerEula eula = new ServerEula(new File("eula.txt"));
        if (!eula.hasAcceptedEULA()) {
            System.out.println(Message.getString("eula"));
            eula.createEULAFile();
            return;
        }
//        if (Update.isCheckVersion()) {
//            Update.hasLatestVersion();
//        }
        if (Update.getLibrariesVersion()) {
            System.out.println(Message.getString("mohist.start.error.nothavelibrary"));
            DownloadLibraries.run();
            System.out.println(Message.getString("file.ok"));
            return;
        }
        Class<?> launchwrapper = null;
        try
        {
            launchwrapper = Class.forName("net.minecraft.launchwrapper.Launch",true, MohistMC.class.getClassLoader());
            Class.forName("org.objectweb.asm.Type",true, MohistMC.class.getClassLoader());
            System.out.println("");
            System.out.println("                   __                     __      ");
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
        }
        catch (Exception e)
        {
            System.out.println(Message.getString("mohist.start.error.nothavelibrary"));
            e.printStackTrace(System.err);
            System.exit(1);
        }

        try
        {
            Method main = launchwrapper.getMethod("main", String[].class);
            String[] allArgs = new String[args.length + 2];
            allArgs[0] = "--tweakClass";
            allArgs[1] = "cpw.mods.fml.common.launcher.FMLServerTweaker";
            System.arraycopy(args, 0, allArgs, 2, args.length);
            main.invoke(null,(Object)allArgs);
        }
        catch (Exception e)
        {
            System.out.println(Message.getString("mohist.start.error"));
            if (e instanceof InvocationTargetException)
                System.out.println(((InvocationTargetException)e).getCause());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
