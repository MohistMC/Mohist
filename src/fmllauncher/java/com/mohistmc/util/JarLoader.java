package com.mohistmc.util;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

import com.mohistmc.util.i18n.i18n;

public class JarLoader {

    private static Instrumentation inst = null;

    public JarLoader() {
    }

    // The JRE will call method before launching your main()
    public static void agentmain(final String a, final Instrumentation inst) {
        JarLoader.inst = inst;
    }

    // Don't forget to specify -javaagent:<mohist jar> on Java 9+,
    // if you load main Mohist jar from -cp rather than direct -jar
    public static void premain(String agentArgs, Instrumentation inst) {
        JarLoader.inst = inst;
    }

    public void loadJar(File path) throws Exception {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        if (!path.getName().equals("minecraft_server.1.16.5.jar")) {
            if (!(cl instanceof URLClassLoader)) {
                // If Java 9 or higher use Instrumentation
                //System.out.println(path);
                if (inst == null) {
                    System.out.println(i18n.get("jarloader.classpath1"));
                    System.out.println(i18n.get("jarloader.classpath2"));
                    System.exit(1);
                }
                inst.appendToSystemClassLoaderSearch(new JarFile(path));
            } else {
                // If Java 8 or below fallback to old method
                Method m = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                m.setAccessible(true);
                m.invoke((URLClassLoader)cl, path.toURI().toURL());
                //System.out.println(path);
            }
        }
    }
}