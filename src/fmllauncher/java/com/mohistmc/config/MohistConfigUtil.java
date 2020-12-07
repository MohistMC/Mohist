package com.mohistmc.config;

import com.mohistmc.MohistMCStart;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class MohistConfigUtil {
    public static File mohistyml = new File("mohist-config/mohist.yml");

    public static String getString(File f, String key, String defaultReturn) {
        try {
            for (String line : Files.readAllLines(f.toPath()))
                if(line.contains(key+": "))
                    return line.split(key+": ")[1].replace("'", "").replace("\"", "");
        } catch (Throwable ignored) {}
        return defaultReturn;
    }

    public static String sMohist(String key, String defaultReturn) {
        return getString(mohistyml, key, defaultReturn);
    }

    public static Boolean bMohist(String key, String defaultReturn) {
        return Boolean.parseBoolean(sMohist(key, defaultReturn));
    }

    public static void copyMohistConfig() {
        try {
            if (!mohistyml.exists()) {
                mohistyml.mkdirs();
                Files.copy(MohistMCStart.class.getClassLoader().getResourceAsStream("configs/mohist.yml"), mohistyml.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            System.out.println("File copy exception!");
        }
    }
}
