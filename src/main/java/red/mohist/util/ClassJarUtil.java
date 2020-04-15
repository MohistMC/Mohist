package red.mohist.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import red.mohist.configuration.MohistConfigUtil;
import red.mohist.down.UpdateUtils;
import red.mohist.util.i18n.Message;

public class ClassJarUtil {

    public static void checkFiles(String libDir, String classname, boolean implementation) throws IOException, ZipException {
        File[] files = new File(libDir).listFiles((dir, name) -> name.endsWith(".jar"));
        for (File file : files) {
            JarFile f = new JarFile(file);
            if(f.getJarEntry(classname.replaceAll("\\.", "/") + ".class") != null) {
                f.close();
                file.delete();
                if(!implementation)
                    System.out.println(Message.getFormatString("update.deleting", new Object[]{file.getName(), libDir}));
                else System.out.println(Message.getFormatString("update.implementation", new Object[]{file.getName()}));
                break;
            }
        }
    }

    public static void copyPluginYMLandCheck(String classname, String ver, String link, String what) throws IOException {
        File[] files = new File("plugins").listFiles((dir, name) -> name.endsWith(".jar"));
        for (File file : files) {
            JarFile jarfile = new JarFile(file);
            if(jarfile.getJarEntry(classname.replaceAll("\\.", "/") + ".class") != null) {
                if(jarfile.getJarEntry("plugin.yml") != null) {
                    Files.copy(jarfile.getInputStream(new ZipEntry("plugin.yml")), Paths.get("plugins/check.yml"), StandardCopyOption.REPLACE_EXISTING);
                    jarfile.close();
                    if(!MohistConfigUtil.getString(new File("plugins", "check.yml"), "version:", "xxxxx").equals(ver)) {
                        new File("plugins/check.yml").delete();
                        System.gc();
                        if(what.contains("mod")) {
                            System.out.println(Message.getFormatString("update.pluginversionforge", new Object[]{file.getName().replace(".jar", ""), link}));
                            System.out.println(Message.getFormatString("update.downloadpluginversion", new Object[]{file.getName().replace(".jar", "")}));
                            if(new Scanner(System.in).next().equals("yes")) {
                                file.delete();
                                System.out.println(Message.getFormatString("update.dl", new Object[]{UpdateUtils.getSize(UpdateUtils.getConnLength(link)), file.getName()}));
                                UpdateUtils.downloadFile(link, new File("mods/" + file.getName()));
                                System.out.println(Message.getString("update.finish"));
                            }
                        } else {
                            System.out.println(Message.getFormatString("update.pluginversion", new Object[]{file.getName().replace(".jar", ""), ver, link, ""}));
                            System.out.println(Message.getFormatString("update.downloadpluginversion", new Object[]{file.getName().replace(".jar", "")}));
                            if(new Scanner(System.in).next().equals("yes")) {
                                file.delete();
                                System.out.println(Message.getFormatString("update.dl", new Object[]{UpdateUtils.getSize(UpdateUtils.getConnLength(link)), file.getName()}));
                                UpdateUtils.downloadFile(link, new File("plugins/" + file.getName()));
                                System.out.println(Message.getString("update.finish"));
                            }
                        }
                    }
                }
            }
        }
    }
}
