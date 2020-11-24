package com.mohistmc;

import com.mohistmc.configuration.MohistConfigUtil;
import com.mohistmc.network.download.DownloadJava;
import com.mohistmc.network.download.DownloadLibraries;
import com.mohistmc.util.EulaUtil;
import com.mohistmc.util.i18n.Message;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Mgazul
 * @date 2019/11/16 2:53
 */
public class MohistMC {

    public static String getVersion() {
        return MohistMC.class.getPackage().getImplementationVersion() != null ? MohistMC.class.getPackage().getImplementationVersion() : "unknown";
    }

    public static void main(String[] args) throws Exception {
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
            System.out.println("                                      " + Message.getString("forge.serverlanunchwrapper.1"));
        }

        if (MohistConfigUtil.bMohist("check_libraries")) DownloadLibraries.run();

        if (!EulaUtil.hasAcceptedEULA()) {
            System.out.println(Message.getString("eula"));
            while (!"true".equals(new Scanner(System.in).next())) ;
            EulaUtil.writeInfos();
        }

        System.out.println(Message.getString("mohist.start"));
        System.out.println(Message.getString("load.libraries"));
        String[] allArgs = Arrays.asList("--tweakClass", "cpw.mods.fml.common.launcher.FMLServerTweaker").toArray(new String[args.length + 2]);
        System.arraycopy(args, 0, allArgs, 2, args.length);

        try {
            Class.forName("net.minecraft.launchwrapper.Launch",true, MohistMC.class.getClassLoader()).getMethod("main", String[].class).invoke(null, (Object) allArgs);
            Class.forName("org.objectweb.asm.Type",true, MohistMC.class.getClassLoader());
        } catch (Exception e) {
            System.out.println(Message.getString("mohist.start.error"));
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

}
