package com.mohistmc.network.download;

import com.mohistmc.MohistMC;
import com.mohistmc.configuration.MohistConfigUtil;
import com.mohistmc.util.JarLoader;
import com.mohistmc.util.i18n.Message;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;

import static com.mohistmc.network.download.UpdateUtils.downloadFile;
import static com.mohistmc.network.download.UpdateUtils.getLibs;

public class DownloadLibraries {
  static int retry = 0;
  static HashMap<String, String> fail = new HashMap<>();
  static File libF = new File("libraries", "mohist_libraries.json");

  public static void run() throws Exception {
    System.out.println(Message.getString("libraries.checking.start"));
    if(!libF.exists()) {
      libF.mkdirs();
      libF.createNewFile();
      Files.copy(MohistMC.class.getClassLoader().getResourceAsStream("mohist_libraries.json"), libF.toPath(), StandardCopyOption.REPLACE_EXISTING);
    } else {
      Files.copy(MohistMC.class.getClassLoader().getResourceAsStream("mohist_libraries.json"), libF.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
    HashMap<String, String> libs = getLibs();
    ArrayList<File> indexLibs = new ArrayList<>();
    for (Object o : libs.keySet()) {
      File lib = new File("libraries/" + o.toString().split("\\*")[1]);
      boolean md5 = lib.exists() && DatatypeConverter.printHexBinary(MessageDigest.getInstance("MD5").digest(Files.readAllBytes(lib.toPath()))).toLowerCase().equals(libs.get(o.toString()));
      if(libs.get(o.toString()).equals("nomd5")) md5 = true;
      String u = o.toString().split("\\*")[0];

      if((!lib.exists() || !md5) && !MohistConfigUtil.getString(MohistConfigUtil.mohistyml, "libraries_black_list:", "xxxxx").contains(lib.getName()) && !u.equals("no")) {
        lib.getParentFile().mkdirs();
        if(Message.isCN() && u.contains("https://www.mgazul.cn/"))
          u = u.replace("https://www.mgazul.cn/", "https://mohist-community.gitee.io/mohistdown/"); //Gitee Mirror
        System.out.println(Message.getString("libraries.global.percentage") + String.valueOf((float) UpdateUtils.getSizeOfDirectory(new File("libraries")) / 35 * 100).substring(0, 2).replace(".", "") + "%"); //Global percentage

        try {
          downloadFile(u, lib);
        } catch (Exception e) {
          System.out.println(Message.getFormatString("file.download.nook", new Object[]{u}));
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
      System.out.println(Message.getFormatString("update.retry", new Object[]{retry}));
      run();
    } else {
      for (File f : indexLibs)
        JarLoader.loadjar(new JarLoader((URLClassLoader) ClassLoader.getSystemClassLoader()), f.getParent());
      System.out.println(Message.getString("libraries.checking.end"));
      if(!fail.isEmpty()) {
        System.out.println(Message.getString("libraries.cant.start.server"));
        for (String lib : fail.keySet()) System.out.println("Link : " + lib + "\nPath : " + fail.get(lib)+"\n");
        System.exit(0);
      }
    }
  }
}
