package com.mohistmc;

import com.mohistmc.config.MohistConfigUtil;
import com.mohistmc.network.download.DownloadLibraries;
import com.mohistmc.utils.i18n;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static com.mohistmc.utils.EulaUtil.hasAcceptedEULA;
import static com.mohistmc.utils.EulaUtil.writeInfos;
import static com.mohistmc.utils.InstallUtils.startInstallation;
import static net.minecraftforge.server.ServerMain.startMohistServer;

public class MohistMCStart {

    public static ArrayList<String> allArgs = new ArrayList<>();

    public static String getVersion() {
        return (MohistMCStart.class.getPackage().getImplementationVersion() != null) ? MohistMCStart.class.getPackage().getImplementationVersion() : "unknown";
    }

    public static String getForgeVersion() {
        return (MohistMCStart.class.getPackage().getSpecificationVersion() != null) ? MohistMCStart.class.getPackage().getSpecificationVersion() : "unknown";
    }

    public static String getMCPVersion() {
        return (MohistMCStart.class.getPackage().getSpecificationTitle() != null) ? MohistMCStart.class.getPackage().getSpecificationTitle() : "unknown";
    }

    public static void main(String[] args) throws Exception {
        allArgs.addAll(Arrays.asList(args));
        MohistConfigUtil.copyMohistConfig();
        System.out.println("\n" + "\n" +
                " __    __   ______   __  __   __   ______   ______  \n" +
                "/\\ \"-./  \\ /\\  __ \\ /\\ \\_\\ \\ /\\ \\ /\\  ___\\ /\\__  _\\ \n" +
                "\\ \\ \\-./\\ \\\\ \\ \\/\\ \\\\ \\  __ \\\\ \\ \\\\ \\___  \\\\/_/\\ \\/ \n" +
                " \\ \\_\\ \\ \\_\\\\ \\_____\\\\ \\_\\ \\_\\\\ \\_\\\\/\\_____\\  \\ \\_\\ \n" +
                "  \\/_/  \\/_/ \\/_____/ \\/_/\\/_/ \\/_/ \\/_____/   \\/_/ \n" +
                "                                                    \n" + "\n");
        System.out.println("                                      " + i18n.getString("mohist.launch.welcomemessage"));
        if (MohistConfigUtil.bMohist("check_libraries", "true")) DownloadLibraries.run();
        startInstallation();
        if(!hasAcceptedEULA()) {
            System.out.println(i18n.getString("eula"));
            while (!"true".equals(new Scanner(System.in).next())) ;
            writeInfos();
        }
        startMohistServer(args);
    }
}
