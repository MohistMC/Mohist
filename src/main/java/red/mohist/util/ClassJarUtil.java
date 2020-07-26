package red.mohist.util;

import red.mohist.util.i18n.I18N;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class ClassJarUtil {

    public static void checkOther(String libDir, String classname, boolean implementation) throws IOException {
        for (File file : new File(libDir).listFiles((dir, name) -> name.endsWith(".jar"))) {
            JarFile f = new JarFile(file);
            if (f.getJarEntry(classname.replaceAll("\\.", "/") + ".class") != null) {
                f.close();
                System.gc();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                file.delete();
                if (!implementation) System.out.println(I18N.get("update.deleting", file.getName(), libDir));
                else System.out.println(I18N.get("update.implementation", file.getName()));
                break;
            }
        }
    }

    public static void checkPl(String classname, String ver, String link, String what) throws Exception {
        boolean verB = false;
        boolean classB = false;
        for (File file : new File("plugins").listFiles((dir, name) -> name.endsWith(".jar"))) {
            JarFile jarfile = new JarFile(file);
            if (jarfile.getJarEntry(classname.replaceAll("\\.", "/") + ".class") != null && jarfile.getJarEntry("plugin.yml") != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(jarfile.getInputStream(new ZipEntry("plugin.yml"))));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("version:") && !line.equals("version: " + ver)) verB = true;
                    if (line.startsWith("main:") && line.equals("main: " + classname)) classB = true;
                }
                jarfile.close();
                br.close();
                if (verB && classB) {
                    if (what.contains("mods"))
                        System.out.println(I18N.get("update.pluginversionforge", file.getName().replace(".jar", "")));
                    else
                        System.out.println(I18N.get("update.pluginversion", file.getName().replace(".jar", ""), ver, link, ""));

                    System.out.println(I18N.get("update.downloadpluginversion", file.getName().replace(".jar", "")));
                    if (new Scanner(System.in).next().equals("yes")) {
                        System.gc();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        file.delete();
                    }
                }
            }
        }
    }
}
