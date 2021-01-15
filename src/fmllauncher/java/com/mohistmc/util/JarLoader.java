package com.mohistmc.util;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

public class JarLoader {

    private static Instrumentation inst = null;

    public JarLoader() {
    }

    // The JRE will call method before launching your main()
    public static void agentmain(final String a, final Instrumentation inst) {
        JarLoader.inst = inst;
    }

    public void loadJar(File path) throws Exception {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        if (!path.getName().equals("minecraft_server.1.16.4.jar") || !isASM611(path.getName())) {
            if (!(cl instanceof URLClassLoader)) {
                // If Java 9 or higher use Instrumentation
                //System.out.println(path);
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

    public boolean isASM611(String asm) {
        return asm.equals("asm-6.1.1.jar") || asm.equals("asm-commons-6.1.1.jar") || asm.equals("asm-tree-6.1.1.jar") || asm.equals("asm-analysis-6.1.1.jar");
    }
}