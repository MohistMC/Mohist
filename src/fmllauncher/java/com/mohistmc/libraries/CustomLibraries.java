package com.mohistmc.libraries;

import com.mohistmc.util.JarLoader;
import com.mohistmc.util.JarTool;
import java.io.File;

public class CustomLibraries {

    public static File file = new File(JarTool.getJarDir() + "/libraries/customize_libraries");

    public static void loadCustomLibs() throws Exception {
        if(!file.exists()) file.mkdirs();

        for (File lib : file.listFiles((dir, name) -> name.endsWith(".jar"))) {
            if (!DefaultLibraries.getDefaultLibs().keySet().toString().contains(lib.getName()))
                new JarLoader().loadJar(lib);
                System.out.println(lib.getName() +" custom library loaded successfully.");
        }
    }
}
