package com.mohistmc.forge;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static com.mohistmc.network.download.UpdateUtils.downloadFile;
import static com.mohistmc.util.PluginsModsDelete.fastbench;

public class FastWorkBenchConf {
  public static void changeConf() throws Exception {
    File f = new File("config/fastbench.cfg");
    String lines = "";
    boolean c = false;
    if(f.exists()) {
      for (String t : Files.readAllLines(f.toPath(), StandardCharsets.UTF_8)) {
      if(t.contains("Disable Recipe Book") && t.contains("true")) {
        lines = lines + t.replace("true", "false") + "\n";
        c = true;
        System.out.println("[Mohist] Fixed FastWorkBench mod config to make it work.");
      } else lines = lines + t + "\n";
    }
    } else if(fastbench) {
      downloadFile("https://raw.githubusercontent.com/Shawiizz/shawiizz.github.io/master/fastbench.cfg", new File("config/fastbench.cfg"));
      System.out.println("[Mohist] Fixed FastWorkBench mod config to make it work.");
    }
    if(c) FileUtils.writeStringToFile(f, lines, StandardCharsets.UTF_8);
  }
}
