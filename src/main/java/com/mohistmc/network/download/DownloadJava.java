package com.mohistmc.network.download;

import com.mohistmc.MohistMC;
import static com.mohistmc.configuration.MohistConfigUtil.bMohist;
import com.mohistmc.util.i18n.Message;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DownloadJava {
  public static File java = new File("CustomJAVA/");
  public static File javabin = new File("CustomJAVA/bin/");
  private static File javadl = new File(java.getAbsolutePath()+"/java.zip");
  public static ArrayList<String> launchArgs = new ArrayList<>();

  public static void run(String[] args) throws Exception {
    launchArgs.addAll(Arrays.asList(args));

    if(!launchArgs.contains("launchedWithCustomJava8")) {
      if(!javabin.exists()) {
        if(!bMohist("use_custom_java8")) {
          System.out.println(Message.getString("unsupported.java.version"));
          Scanner scan = new Scanner(System.in);
          System.out.println(Message.getString("customjava.ask"));
          String input = scan.nextLine();
          if(input.equalsIgnoreCase("Yes")) searchJava();
          else {
            System.out.println(Message.getString("customjava.no"));
            System.exit(0);
          }
        } else searchJava();
      } else searchJava();
    }
  }

  public static void searchJava() throws Exception {
    if(System.getProperty("sun.arch.data.model").equals("64")) {
      if(os().equals("Windows"))
        prepareLaunch("https://github.com/Shawiizz/shawiizz.github.io/releases/download/jrezipfiles/javawin64.zip", "java.exe");
      else if(os().equals("Unix"))
        prepareLaunch("https://github.com/Shawiizz/shawiizz.github.io/releases/download/jrezipfiles/javalinux64.zip", "java");
      else if(os().equals("Mac"))
        prepareLaunch("https://github.com/Shawiizz/shawiizz.github.io/releases/download/jrezipfiles/javamac64.zip", "java");
    } else {
      if(os().equals("Windows"))
        prepareLaunch("https://github.com/Shawiizz/shawiizz.github.io/releases/download/jrezipfiles/janawin32.zip", "java.exe");
      else if(os().equals("Unix"))
        prepareLaunch("https://github.com/Shawiizz/shawiizz.github.io/releases/download/jrezipfiles/javalinux32.zip", "java");
    }
  }

  private static void prepareLaunch(String URL, String javaName) throws Exception {
    if(!javabin.exists()) {
      java.mkdirs();
      java.createNewFile();
      System.out.println(Message.getFormatString("customjava.dl", new Object[]{os()}));
      UpdateUtils.downloadFile(URL, javadl);
      unzip(new FileInputStream(javadl), java.toPath());
      javadl.delete();
      if(os().equals("Unix")) Runtime.getRuntime().exec("chmod 755 -R ./CustomJAVA");
    }

    ArrayList<String> command = new ArrayList<>(Arrays.asList(java.getAbsolutePath()+"/bin/"+javaName, "-jar"));
    launchArgs.addAll(ManagementFactory.getRuntimeMXBean().getInputArguments());
    launchArgs.add(new File(MohistMC.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1)).getName());
    launchArgs.add("launchedWithCustomJava8");
    command.addAll(launchArgs);
    System.out.println(Message.getFormatString("customjava.run", new Object[]{os(), command}));
    UpdateUtils.restartServer(command);
  }

  public static void unzip(InputStream is, Path targetDir) throws IOException {
    try (ZipInputStream zipIn = new ZipInputStream(is)) {
      for (ZipEntry ze; (ze = zipIn.getNextEntry()) != null; ) {
        Path resolvedPath = targetDir.resolve(ze.getName());
        if(ze.isDirectory()) Files.createDirectories(resolvedPath);
        else {
          Files.createDirectories(resolvedPath.getParent());
          Files.copy(zipIn, resolvedPath);
        }
      }
    }
  }

  public static String os() {
    String o = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
    return o.contains("win") ? "Windows" : o.contains("mac") ? "Mac" : Stream.of("solaris", "sunos", "linux", "unix").anyMatch(o::contains) ? "Unix" : "Unknown";
  }

}
