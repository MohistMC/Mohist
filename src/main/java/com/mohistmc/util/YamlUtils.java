package com.mohistmc.util;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * @author Mgazul by MohistMC
 * @date 2023/8/2 18:28:15
 */
public class YamlUtils {

    public static void save(File file, FileConfiguration yaml) {
        try {
            yaml.save(file);
        } catch (IOException var4) {
            var4.fillInStackTrace();
        }
    }
}
