package com.mohistmc.util;

import com.mohistmc.util.i18n.i18n;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Scanner;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import static com.mohistmc.network.download.UpdateUtils.downloadFile;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class PluginsModsDelete {

    public static void chekPlugins(Fix fix) throws Exception {
        File plugins = new File("plugins");
        String mainClassSplit = fix.mainClass.replaceAll("\\.", "/") + ".class";
        if (!plugins.exists())
            plugins.mkdir();
        for (File file : Objects.requireNonNull(plugins.listFiles((dir, name) -> name.endsWith(".jar")))) {
            if (!fileExists(file, "plugin.yml") && !fileExists(file, mainClassSplit)) return;

            JarFile jarFile = new JarFile(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(new ZipEntry("plugin.yml"))));

            boolean versionFound = false;
            boolean mainFound = false;

            String line;
            String version = "";
            String link = "";
            try {
                version = fix.version_fix;
                link = fix.urlFix;
            } catch (Exception ignored) {
            }

            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("version:") && !line.equals("version: " + version)) {
                    versionFound = true;
                }

                if (line.startsWith("main:") && line.equals("main: " + mainClassSplit.replaceAll("/", "\\.").replace(".class", "")))
                    mainFound = true;
            }
            bufferedReader.close();
            jarFile.close();

            if (!version.equals("")) {
                if (versionFound && mainFound) {
                    System.out.println(i18n.get("update.pluginversion", new Object[]{file.getName().replace(".jar", ""), version, link, ""}));
                    System.out.println(i18n.get("update.downloadpluginversion", new Object[]{file.getName().replace(".jar", "")}));
                    if (new Scanner(System.in).next().equals("yes"))
                        downloadFile(link, new File(plugins + "/" + file.getName()));
                }
            }
        }
    }

    public static void chekPlugins(String mainClass) throws Exception {
        String mainClassSplit = mainClass.replaceAll("\\.", "/") + ".class";

        File plugins = new File("plugins");
        if (!plugins.exists()) plugins.mkdir();
        for (File f : plugins.listFiles((dir, name) -> name.endsWith(".jar"))) {
            if (fileExists(f, "plugin.yml") && fileExists(f, mainClassSplit)) {
                System.out.println(i18n.get("update.deleting", new Object[]{f.getName(), "plugins"}));
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
    }

    public static void chekMods(Fix fix) {

    }

    public static void chekMods(String mainClass) throws Exception {
        String mainClassSplit = mainClass.replaceAll("\\.", "/") + ".class";

        File mods = new File("mods");
        if (!mods.exists()) mods.mkdir();
        for (File f : mods.listFiles((dir, name) -> name.endsWith(".jar"))) {
            if (fileExists(f, mainClassSplit)) {
                System.out.println(i18n.get("update.deleting", new Object[]{f.getName(), "mods"}));
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

    private static boolean fileExists(File file, String fileSearch) {
        if (!file.exists()) return false;
        try {
            JarFile jarFile = new JarFile(file);
            if (jarFile.getJarEntry(fileSearch) != null) {
                jarFile.close();
                return true;
            }
        } catch (Exception e) {
            System.out.println("[Mohist | ALERT] - The jar file " + file.getName() + " (at " + file.getAbsolutePath() + ") is maybe corrupted or empty.");
        }
        return false;
    }

    public static class Fix {

        public String mainClass;
        public String urlFix;
        public String version_fix;

        public Fix(String mainClass, String url_fix, String version_fix) {
            this.mainClass = mainClass;
            this.urlFix = url_fix;
            this.version_fix = version_fix;
        }
    }
}
