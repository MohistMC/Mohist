package com.mohistmc.utils;

import com.mohistmc.MohistMCStart;
import com.mohistmc.utils.i18n.i18n;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;

public class InstallUtils {
    public static String forgeVer = MohistMCStart.getForgeVersion();
    public static String mcpVer = MohistMCStart.getMCPVersion();
    public static String libPath = JarTool.getJarDir() + "/libraries/";
    public static File universalJar = new File(libPath + "net/minecraftforge/forge/1.16.4-" + forgeVer + "/forge-1.16.4-" + forgeVer + "-universal.jar");
    public static File lzma = new File(libPath + "com/mohistmc/installation/data/server.lzma");
    public static File extra = new File(libPath + "net/minecraft/server/1.16.4-" + mcpVer + "/server-1.16.4-" + mcpVer + "-extra.jar");

    public static void startInstallation() throws Exception {
        System.out.println(i18n.get("installation.start"));
        copyFileFromJar(lzma, "data/server.lzma");
        copyFileFromJar(universalJar, "data/forge-1.16.4-" + forgeVer + "-universal.jar");

        ProcessBuilder processBuilder = new ProcessBuilder(new ArrayList<>(Arrays.asList("java", "-jar", "MohistInstallChecker.jar", "\"" + libPath + "\"", forgeVer, mcpVer)));
        processBuilder.directory(new File(libPath + "com/mohistmc/installation/"));
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null)
            if (line.startsWith("="))
                System.out.println(i18n.get(line.replaceFirst("=", "")));
            else System.out.println(line);

        process.waitFor();
        reader.close();
        process.destroy();
        new JarLoader().loadJar(extra);
    }

    private static void copyFileFromJar(File file, String pathInJar) throws Exception {
        InputStream is = MohistMCStart.class.getClassLoader().getResourceAsStream(pathInJar);
        if (!file.exists() || !MD5Util.getMd5(file).equals(MD5Util.getMd5(is))) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

}