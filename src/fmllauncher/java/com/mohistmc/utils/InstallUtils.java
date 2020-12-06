package com.mohistmc.utils;

import com.mohistmc.MohistMCStart;
import com.mohistmc.network.download.UpdateUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;

import static com.mohistmc.MohistMCStart.allArgs;
import static com.mohistmc.network.download.UpdateUtils.getMohistJar;
import static com.mohistmc.network.download.UpdateUtils.restartServer;

public class InstallUtils {
    public static void startInstallation() throws Exception {
        System.out.println(i18n.getString("installation.start"));
        String libPath = new File("libraries").getAbsolutePath();
        String forgeVer = MohistMCStart.getForgeVersion();
        File universalJar = new File(libPath+"/net/minecraftforge/forge/1.16.4-"+forgeVer+"/forge-1.16.4-"+forgeVer+"-universal.jar");
        if(!universalJar.exists()) {
            universalJar.getParentFile().mkdirs();
            universalJar.createNewFile();
            Files.copy(MohistMCStart.class.getClassLoader().getResourceAsStream("data/forge-1.16.4-"+forgeVer+"-universal.jar"), universalJar.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        File lzma = new File(libPath+"/com/mohistmc/installation/data/server.lzma");
        if(!lzma.exists()) {
            lzma.getParentFile().mkdirs();
            lzma.createNewFile();
            Files.copy(MohistMCStart.class.getClassLoader().getResourceAsStream("data/server.lzma"), lzma.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        ProcessBuilder processBuilder = new ProcessBuilder(new ArrayList<>(Arrays.asList("java", "-jar", "\""+libPath+"/com/mohistmc/installation/MohistInstallChecker.jar\"", "\""+libPath+"\"", forgeVer, MohistMCStart.getMCPVersion())));
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null)
            if(line.startsWith("="))
                System.out.println(i18n.getString(line.replaceFirst("=", "")));
            else System.out.println(line);

        process.waitFor();
        reader.close();
        process.destroy();
        if(!allArgs.contains("installChecked"))
            restartServer(new ArrayList<>(Arrays.asList("java", "-jar", getMohistJar().getName(), "installChecked")), true);
    }
}
