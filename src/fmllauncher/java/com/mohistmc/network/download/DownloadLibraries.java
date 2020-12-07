package com.mohistmc.network.download;

import com.mohistmc.MohistMCStart;
import com.mohistmc.config.MohistConfigUtil;
import com.mohistmc.utils.JarLoader;
import com.mohistmc.utils.i18n.i18n;

import java.io.File;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.jar.Manifest;

import static com.mohistmc.network.download.UpdateUtils.downloadFile;
import static com.mohistmc.network.download.UpdateUtils.getLibs;
import static com.mohistmc.utils.MD5Util.getMd5;

public class DownloadLibraries {
  static int retry = 0;
  static HashMap<String, String> fail = new HashMap<>();
  static File libF = new File("libraries/mohist_libraries.json");

  public static void run() throws Exception {
    System.out.println(i18n.getString("libraries.checking.start"));
    if(!libF.exists()) {
      libF.mkdirs();
      libF.createNewFile();
      Files.copy(MohistMCStart.class.getClassLoader().getResourceAsStream("mohist_libraries.json"), libF.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
    HashMap<String, String> libs = getLibs();
    ArrayList<File> indexLibs = new ArrayList<>();
    for (Object o : libs.keySet()) {
      File lib = new File("libraries/" + o.toString().split("\\*")[1]);
      boolean md5 = lib.exists() && getMd5(lib).equals(libs.get(o.toString()));
      if(libs.get(o.toString()).equals("nomd5")) md5 = true;
      String u = o.toString().split("\\*")[0];

      if((!lib.exists() || !md5) && !MohistConfigUtil.getString(MohistConfigUtil.mohistyml, "libraries_black_list:", "xxxxx").contains(lib.getName()) && !u.equals("no")) {
        lib.getParentFile().mkdirs();
        if(i18n.isLang("CN") && u.contains("https://www.mgazul.cn/"))
          u = u.replace("https://www.mgazul.cn/", "https://mohist-community.gitee.io/mohistdown/"); //Gitee Mirror
        System.out.println("Global percentage > " + String.valueOf((float) UpdateUtils.getSizeOfDirectory(new File("libraries")) / 58 * 100).substring(0, 2).replace(".", "") + "%"); //Global percentage

        try {
          downloadFile(u, lib);
        } catch (Exception e) {
          System.out.println(i18n.getFormatString("file.download.nook", new Object[]{u}));
          e.printStackTrace();
          lib.delete();
          fail.put(u, lib.getAbsolutePath());
        }
      }
      if(lib.length() > 1) indexLibs.add(lib);
    }

    /*FINISHED | RECHECK IF A FILE FAILED*/
    if(retry < 3 && !fail.isEmpty()) {
      retry++;
      System.out.println(i18n.getFormatString("libraries.check.retry", new Object[]{retry}));
      run();
    } else {
      System.out.println(i18n.getString("libraries.check.end"));
      if(!fail.isEmpty()) {
        System.out.println(i18n.getString("libraries.check.missing"));
        for (String lib : fail.keySet()) System.out.println("Link : " + lib + "\nPath : " + fail.get(lib)+"\n");
        System.exit(0);
      }
    }
  }

  public static void loadLibs() throws Exception {
    Manifest manifest = new Manifest(MohistMCStart.class.getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF"));
    for (String path : manifest.getMainAttributes().getValue("Class-Path").split(" "))
      new JarLoader((URLClassLoader) ClassLoader.getSystemClassLoader()).loadJar(Paths.get(path).toUri().toURL());
  }
}
