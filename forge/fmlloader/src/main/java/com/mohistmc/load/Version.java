package com.mohistmc.load;

import java.io.File;
import java.nio.file.Files;

public class Version {

    public static String get(String key) {
        try {
            for (String line : Files.readAllLines(new File("mods.txt").toPath())) {
                if (line.contains(key + ":")) {
                    String[] spl = line.split(":");
                    return spl[1];
                }
            }
        } catch (Throwable ignored) {
        }
        return "1.20";
    }
}
