package com.mohistmc.utils;

import com.mohistmc.MohistMCStart;
import com.mohistmc.utils.i18n.i18n;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;

public class InstallUtils {
    static String forgeVer = MohistMCStart.getForgeVersion();
    static String mcpVer = MohistMCStart.getMCPVersion();
    static String libPath = new File("libraries").getAbsolutePath();
    static File universalJar = new File(libPath+"/net/minecraftforge/forge/1.16.4-"+forgeVer+"/forge-1.16.4-"+forgeVer+"-universal.jar");
    static File lzma;

    public static void startInstallation() throws Exception {
        System.out.println(i18n.get("installation.start"));

        String lzmaMd5 = DatatypeConverter.printHexBinary(new DigestInputStream(MohistMCStart.class.getClassLoader().getResourceAsStream("data/server.lzma"), MessageDigest.getInstance("MD5")).getMessageDigest().digest()).toLowerCase();
        lzma = new File(libPath+"/com/mohistmc/installation/data/"+lzmaMd5+".lzma");

        copyUniversalJar();
        copyLzma();

        ProcessBuilder processBuilder = new ProcessBuilder(new ArrayList<>(Arrays.asList("java", "-jar", "\""+libPath+"/com/mohistmc/installation/MohistInstallChecker.jar\"", "\""+libPath+"\"", forgeVer, mcpVer, lzmaMd5)));
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null)
            if(line.startsWith("="))
                System.out.println(i18n.get(line.replaceFirst("=", "")));
            else System.out.println(line);

        process.waitFor();
        reader.close();
        process.destroy();
    }

    private static void copyLzma() throws Exception {
        copyFileFromJar(lzma, "data/server.lzma");
    }

    private static void copyUniversalJar() throws Exception {
        copyFileFromJar(universalJar, "data/forge-1.16.4-"+forgeVer+"-universal.jar");
    }

    private static void copyFileFromJar(File file, String pathInJar) throws Exception {
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            Files.copy(MohistMCStart.class.getClassLoader().getResourceAsStream(pathInJar), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

}
