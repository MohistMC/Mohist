package com.mohistmc.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;

public class JarLoader {

    private URLClassLoader urlClassLoader;

    public JarLoader(URLClassLoader urlClassLoader) {
        this.urlClassLoader = urlClassLoader;
    }

    public void loadJar(URL url) throws Exception {
        Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        addURL.setAccessible(true);
        addURL.invoke(urlClassLoader, url);
    }

    public static void loadjar(JarLoader jarLoader, String thepath) throws Exception {
        File libdir = new File(thepath);
        if(libdir.isDirectory()) {

            Files.walk(libdir.toPath()).filter(Files::isRegularFile).filter(c -> c.getFileName().toString().endsWith(".jar")).forEach(path -> {
                try { jarLoader.loadJar(path.toFile().toURI().toURL()); } catch (Exception e) { }
            });

        } else {
            System.exit(0);
        }
    }
}