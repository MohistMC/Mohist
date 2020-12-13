package com.mohistmc.libraries;

import com.mohistmc.util.JarLoader;
import com.mohistmc.util.JarTool;
import java.io.File;

public class CustomLibraries {

    public static File file = new File(JarTool.getJarDir() + "/libraries/customize_libraries");

    public static void run() throws Exception {
        if (!file.exists()) {
            file.mkdirs();
        }
        for (File lib : file.listFiles((dir, name) -> name.endsWith(".jar"))) {
            if (DefaultLibraries.getDefaultLibs().contains(lib.getName())) {
                JarLoader.loadjar(lib.getPath());
            }
        }
    }
}
