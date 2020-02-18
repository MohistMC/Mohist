package red.mohist.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

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

    public static void loadjar(JarLoader jarLoader, String path) throws Exception {
        File libdir = new File(path);
        if (libdir != null && libdir.isDirectory()) {

            File[] listFiles = libdir.listFiles(file -> file.exists() && file.isFile() && file.getName().endsWith(".jar"));

            for (File file : listFiles) {
                jarLoader.loadJar(file.toURI().toURL());
            }

        } else {
            System.exit(0);
        }
    }
}
