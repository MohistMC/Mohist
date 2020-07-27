package red.mohist.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import red.mohist.util.i18n.Message;

import static red.mohist.network.download.UpdateUtils.downloadFile;

public class ClassJarUtil {

  public static void checkOther(String libDir, String classname, boolean implementation) throws IOException {
    for (File file : new File(libDir).listFiles((dir, name) -> name.endsWith(".jar"))) {
      JarFile f = new JarFile(file);
      if(f.getJarEntry(classname.replaceAll("\\.", "/") + ".class") != null) {
        f.close();
        System.gc();
        try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
        file.delete();
        if(!implementation) System.out.println(Message.getFormatString("update.deleting", new Object[]{file.getName(), libDir}));
        else System.out.println(Message.getFormatString("update.implementation", new Object[]{file.getName()}));
        break;
      }
    }
  }

  public static void checkPl(String classname, String ver, String link, String what) throws Exception {
    boolean verB = false;
    boolean classB = false;
    for (File file : new File("plugins").listFiles((dir, name) -> name.endsWith(".jar"))) {
      JarFile jarfile = new JarFile(file);
      if(jarfile.getJarEntry(classname.replaceAll("\\.", "/") + ".class") != null && jarfile.getJarEntry("plugin.yml") != null) {
        BufferedReader br = new BufferedReader(new InputStreamReader(jarfile.getInputStream(new ZipEntry("plugin.yml"))));
        String line;
        while ((line = br.readLine()) != null) {
          if(line.startsWith("version:") && !line.equals("version: " + ver)) verB = true;
          if(line.startsWith("main:") && line.equals("main: " + classname)) classB = true;
        }
        jarfile.close();
        br.close();
        if(verB && classB) {
          if(what.contains("mods"))
            System.out.println(Message.getFormatString("update.pluginversionforge", new Object[]{file.getName().replace(".jar", "")}));
          else
            System.out.println(Message.getFormatString("update.pluginversion", new Object[]{file.getName().replace(".jar", ""), ver, link, ""}));

          System.out.println(Message.getFormatString("update.downloadpluginversion", new Object[]{file.getName().replace(".jar", "")}));
          if(new Scanner(System.in).next().equals("yes")) {
            System.gc();
            try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
            file.delete();
            downloadFile(link, new File(what+"/" + file.getName()));
          }
        }
      }
    }
  }
}
