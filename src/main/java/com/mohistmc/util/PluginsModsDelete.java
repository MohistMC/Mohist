package com.mohistmc.util;

import com.mohistmc.util.i18n.Message;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import static com.mohistmc.network.download.UpdateUtils.downloadFile;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class PluginsModsDelete {
    public static boolean fastbench = false;

    private static boolean fileExists(File f, String fName) throws Exception {
        if (!f.exists()) return false;
        JarFile jf = new JarFile(f);
        if (jf.getJarEntry(fName) != null) {
            jf.close();
            return true;
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
                    boolean b1 = false;
                    boolean b2 = false;

                    JarFile jf = new JarFile(f);
                    BufferedReader br = new BufferedReader(new InputStreamReader(jf.getInputStream(new ZipEntry("plugin.yml"))));
                    String l;

                    String ver = "";
                    String link = "";
                    try {
                        ver = content.split("\\|")[1].split("#")[0];
                        link = content.split("#")[1];
                    } catch (Exception ignored) {
                    }

                    while ((l = br.readLine()) != null) {
                        if (l.startsWith("version:") && !l.equals("version: " + ver)) b1 = true;
                        if (l.startsWith("main:") && l.equals("main: " + cl.replaceAll("/", "\\.").replace(".class", "")))
                            b2 = true;
                    }
                    br.close();
                    jf.close();

                    if (!ver.equals("")) {
                        if (b1 && b2) {
                            System.out.println(Message.getFormatString("update.pluginversion", new Object[]{f.getName().replace(".jar", ""), ver, link, ""}));
                            System.out.println(Message.getFormatString("update.downloadpluginversion", new Object[]{f.getName().replace(".jar", "")}));
                            if (new Scanner(System.in).next().equals("yes"))
                                downloadFile(link, new File(type + "/" + f.getName()));
                        }
                    } else {
                        if (b2) {
                            System.out.println(Message.getFormatString("update.deleting", new Object[]{f.getName(), type}));
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
            }
        } else if (type.equals("mods")) {
            File mods = new File("mods");
            if (!mods.exists()) mods.mkdir();
            for (File f : mods.listFiles((dir, name) -> name.endsWith(".jar"))) {
                if (fileExists(f, cl)) {
                    System.out.println(Message.getFormatString("update.deleting", new Object[]{f.getName(), type}));
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
                if (fileExists(f, "shadows/fastbench/net/HijackedDedicatedPlayerList.class")) fastbench = true;
            }
        }
    }
}
