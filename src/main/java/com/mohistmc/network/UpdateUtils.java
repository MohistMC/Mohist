package com.mohistmc.network;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mohistmc.MohistMC;
import com.mohistmc.util.i18n.i18n;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mohistmc.configuration.MohistConfigUtil.bMohist;

public class UpdateUtils {

  public static void versionCheck() {
    System.out.println(i18n.get("update.check"));
    System.out.println(i18n.get("update.stopcheck"));

    try {
      JsonElement root = new JsonParser().parse(new InputStreamReader(NetworkUtil.getInput("https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.7.10/lastSuccessfulBuild/api/json")));

      String jar_sha = MohistMC.getVersion();
      String build_number = "1.7.10-" + root.getAsJsonObject().get("number").toString();
      String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(root.getAsJsonObject().get("timestamp").toString())));

      if(jar_sha.equals(build_number))
        System.out.println(i18n.get("update.latest", "1.0", jar_sha, build_number));
      else {
        System.out.println(i18n.get("update.detect", build_number, jar_sha, time));
        if(bMohist("check_update_auto_download"))
          downloadFile("mhttps://ci.codemc.io/job/Mohist-Community/job/Mohist-1.7.10/lastSuccessfulBuild/artifact/build/distributions/Mohist-" + build_number + "-server.jar", new File(getMohistJar().getName()));
      }
    } catch (Throwable e) {
      System.out.println(i18n.get("check.update.noci"));
    }
  }

  private static int percentage = 0;
  public static void downloadFile(String URL, File f) throws Exception {
    URLConnection conn = NetworkUtil.getConn(URL.replace("mhttps", "https"));
    System.out.println(i18n.get("file.download.start", f.getName(), getSize(conn.getContentLength())));
    ReadableByteChannel rbc = Channels.newChannel(conn.getInputStream());
    FileChannel fc = FileChannel.open(f.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    int fS = conn.getContentLength();
    Timer t = new Timer();
    t.schedule(new TimerTask() {
      @Override
      public void run() {
        if(rbc.isOpen()) {
          if(percentage != Math.round((float) f.length() / fS * 100) && percentage < 100)
            System.out.println(i18n.get("file.download.percentage", f.getName(), percentage));
          percentage = Math.round((float) f.length() / fS * 100);
        } else t.cancel();
      }
    }, 3000, 1000);
    fc.transferFrom(rbc, 0, Long.MAX_VALUE);
    fc.close();
    rbc.close();
    if(URL.startsWith("mhttps"))
      restartServer(new ArrayList<>(Arrays.asList("java", "-jar", getMohistJar().getName())));
    else System.out.println(i18n.get("file.download.ok", f.getName()));
  }

  public static File getMohistJar() {
        try {
          String path = UpdateUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString();
          if(path.contains("!/")) path = path.split("!/")[0].split("jar:file:/")[1];
          return new File(path);
        } catch (Exception e) {
            System.out.println("Can't found the Mohist jar !");
        }
        return null;
   }

  public static void restartServer(ArrayList<String> cmd) throws Exception {
    ProcessBuilder processBuilder = new ProcessBuilder(cmd);
    processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);
    processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
    Process process = processBuilder.start();
    process.waitFor();
    Thread.sleep(2000);
    System.exit(0);
  }

  public static String getSize(long size) { return (size >= 1048576L) ? (float) size / 1048576.0F + "MB" : ((size >= 1024) ? (float) size / 1024.0F + " KB" : size + " B"); }
  public static long getSizeOfDirectory(File path) throws IOException { return Files.walk(path.toPath()).parallel().filter(p -> !p.toFile().isDirectory()).count(); }
}
