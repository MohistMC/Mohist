package com.mohistmc.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class JarLoader {

    public static void loadjar(String file_or_directory) throws Exception {
        File libdir = new File(file_or_directory);
        if (libdir.isDirectory()) {
            for (File file : libdir.listFiles(file -> file.exists() && file.isFile() && file.getName().endsWith(".jar"))) {
                loadJar(file.toURI().toURL());
            }
        } else {
            loadJar(libdir.toURI().toURL());
        }
    }

    public static void loadJar(URL url) throws Exception {
        Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        addURL.setAccessible(true);
        addURL.invoke((URLClassLoader) ClassLoader.getSystemClassLoader(), url);
    }
}
