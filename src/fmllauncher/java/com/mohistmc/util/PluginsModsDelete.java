package com.mohistmc.util;

import java.io.File;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.jar.JarFile;

public class PluginsModsDelete {

    private static boolean fileExists(File f, String fName) {
        if (!f.exists()) return false;
        try {
            JarFile jf = new JarFile(f);
            if (jf.getJarEntry(fName) != null) {
                jf.close();
                return true;
            }
        } catch (Exception e) {
            System.out.println("[Mohist | ALERT] - The jar file " + f.getName() + " (at " + f.getAbsolutePath() + ") is maybe corrupted or empty.");
            return false;
        }
        return false;
    }

    public static void check(String type, String content) throws Exception {
        String cl = content.split("\\|")[0].replaceAll("\\.", "/") + ".class";

        if (type.equals("plugins")) {
            File plugins = new File("plugins");
            if (!plugins.exists()) plugins.mkdir();
            for (File f : plugins.listFiles((dir, name) -> name.endsWith(".jar"))) {
                if (fileExists(f, "plugin.yml") && fileExists(f, cl)) {
                    System.gc();
                    Thread.sleep(100);
                    File newf = new File("delete/plugins");
                    File qnewf = new File("delete", f.getPath());
                    if (!newf.exists()) {
                        newf.mkdirs();
                    } else {
                        if (qnewf.exists()) qnewf.delete();
                    }
                    Files.copy(f.toPath(), qnewf.toPath(), REPLACE_EXISTING);
                    f.delete();
                }
            }
        } else if (type.equals("mods")) {
            File mods = new File("mods");
            if (!mods.exists()) mods.mkdir();
            for (File f : mods.listFiles((dir, name) -> name.endsWith(".jar"))) {
                if (fileExists(f, cl)) {
                    System.gc();
                    Thread.sleep(100);
                    File newf = new File("delete/mods");
                    File qnewf = new File("delete", f.getPath());
                    if (!newf.exists()) {
                        newf.mkdirs();
                    } else {
                        if (qnewf.exists()) qnewf.delete();
                    }
                    Files.copy(f.toPath(), qnewf.toPath(), REPLACE_EXISTING);
                    f.delete();
                }
            }
        }
    }
}
