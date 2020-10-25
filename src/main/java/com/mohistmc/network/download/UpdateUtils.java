package com.mohistmc.network.download;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mohistmc.MohistMC;
import com.mohistmc.util.i18n.Message;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mohistmc.configuration.MohistConfigUtil.bMohist;
import static com.mohistmc.network.download.NetworkUtil.getConn;
import static com.mohistmc.network.download.NetworkUtil.getInput;

public class UpdateUtils {

  public static void versionCheck() {
    System.out.println(Message.getString("update.check"));
    System.out.println(Message.getString("update.stopcheck"));

    try {
      JsonElement root = new JsonParser().parse(new InputStreamReader(getInput("https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.12.2/lastSuccessfulBuild/api/json")));

      String jar_sha = MohistMC.getVersion();
      String build_number = "1.12.2-" + root.getAsJsonObject().get("number").toString();
      String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(root.getAsJsonObject().get("timestamp").toString())));

      if(jar_sha.equals(build_number))
        System.out.println(Message.getFormatString("update.latest", new Object[]{"1.9", jar_sha, build_number}));
      else {
        System.out.println(Message.getFormatString("update.detect", new Object[]{build_number, jar_sha, time}));
        if(bMohist("check_update_auto_download"))
          downloadFile("mhttps://ci.codemc.io/job/Mohist-Community/job/Mohist-1.12.2/lastSuccessfulBuild/artifact/projects/mohist/build/libs/mohist-" + build_number + "-server.jar", new File(getMohistJar().getName()));
      }
    } catch (Throwable e) {
      System.out.println(Message.getString("check.update.noci"));
    }
  }

  private static int percentage = 0;
  public static void downloadFile(String URL, File f) throws Exception {
    URLConnection conn = getConn(URL.replace("mhttps", "https"));
    System.out.println(Message.getFormatString("file.download.start", new Object[]{f.getName(), getSize(conn.getContentLength())}));
    ReadableByteChannel rbc = Channels.newChannel(conn.getInputStream());
    FileChannel fc = FileChannel.open(f.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    int fS = conn.getContentLength();
    Timer t = new Timer();
    t.schedule(new TimerTask() {
      @Override
      public void run() {
        if(rbc.isOpen()) {
          if(percentage != Math.round((float) f.length() / fS * 100) && percentage < 100)
            System.out.println(Message.getFormatString("file.download.percentage", new Object[]{f.getName(), percentage}));
          percentage = Math.round((float) f.length() / fS * 100);
        } else t.cancel();
      }
    }, 3000, 1000);
    fc.transferFrom(rbc, 0, Long.MAX_VALUE);
    fc.close();
    rbc.close();
    if(URL.startsWith("mhttps"))
      restartServer(new ArrayList<>(Arrays.asList("java", "-jar", getMohistJar().getName())));
    else System.out.println(Message.getFormatString("file.download.ok", new Object[]{f.getName()}));
  }

  public static File getMohistJar() {
    return new File(UpdateUtils.class.getProtectionDomain().getCodeSource().getLocation().getFile().replaceFirst("file:/", "").split("server.jar!/com/mohistmc/")[0]+"server.jar");
  }

  public static HashMap<String, String> getLibs() {
    HashMap<String, String> libsToDl = new HashMap<>();
    HashMap<String, String> mavenList = new HashMap<>();
    List<String> allLibsLines;
    try {
      allLibsLines = Files.readAllLines(new File("libraries", "mohist_libraries.json").toPath());
    } catch (IOException e) { return null; }
    String[] joined = String.join(" ", allLibsLines).split("}");
    for (int i = 0; i < joined.length-1; i++) {
      String thePath = "";
      String theLink;
      String theMd5 = "nomd5";
      String t = joined[i];
      if(!t.contains("\"mavenlist\"")) {

        String[] s = t.split(",");
        if(!t.contains("\"maven\"")) {
          theLink = getElement(s, "link");
          for (Object o : mavenList.keySet())
            if(theLink.contains(o.toString()))
              theLink = theLink.replace(o + " + ", mavenList.get(o.toString()));

          try {thePath = new URL(theLink).getPath();} catch (MalformedURLException ignored) {}
        } else {
          String[] name = getElement(s, "name").split(":");
          String nameTransform = name[0].replaceAll("\\.", "/") + "/" + name[1].replaceAll("\\.", "/") + "/"
            + name[2] + "/" + name[1] + "-" + name[2] + ".jar";

          theLink = getElement(s, "maven") + "/" + nameTransform;
          thePath = nameTransform;
        }
        if(t.contains("\"md5\"")) theMd5 = getElement(s, "md5");
        if(t.contains("\"path\"")) thePath = getElement(s, "path");
        if(thePath.startsWith("/")) thePath = thePath.substring(1);
        if(thePath.startsWith("libraries")) thePath = thePath.replaceFirst("libraries", "");
        if(!thePath.endsWith(".jar")) thePath = thePath + "/" + theLink.split("/")[theLink.split("/").length-1];
        if(!t.contains("\"link\"")) theLink = "no";

        libsToDl.put(theLink  + "*" + thePath, theMd5);
      } else {
        t = t.split("\"mavenlist\":\\{")[1].replaceAll(" ", "");
        for (int j = 0; j < t.split(",").length; j++) {
          String e1 = t.split(",")[j];
          String[] e2 = e1.split("\":\"");
          String mvName = e2[0].substring(e2[0].indexOf("\"") + 1);
          String mvLink = e2[1].split("\"")[0];

          if(mvLink.contains("http") && mvLink.contains("://"))
            mavenList.put(mvName, mvLink+"/");
        }
      }
    }
    return libsToDl;
  }

  private static String getElement(String[] s, String what) {
    for (String value : s)
      if(value.contains(what))
        return value.substring(value.indexOf(what)).split("\"")[2];
    return "nothing";
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
