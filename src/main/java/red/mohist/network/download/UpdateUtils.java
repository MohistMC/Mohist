package red.mohist.network.download;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import red.mohist.Mohist;
import red.mohist.util.i18n.Message;

import static red.mohist.configuration.MohistConfigUtil.bMohist;
import static red.mohist.network.download.NetworkUtil.getConn;
import static red.mohist.network.download.NetworkUtil.getConnLength;
import static red.mohist.network.download.NetworkUtil.getInput;

public class UpdateUtils {

  public static void versionCheck() {
    System.out.println(Message.getString("update.check"));
    System.out.println(Message.getString("update.stopcheck"));

    try {
      JsonElement root = new JsonParser().parse(new InputStreamReader((InputStream) getConn("https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.12.2/api/json").getContent()));
      JsonElement root0 = new JsonParser().parse(new InputStreamReader((InputStream) getConn("https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.12.2/lastSuccessfulBuild/api/json").getContent()));

      String jar_sha = Mohist.getVersion();
      String build_number = root.getAsJsonObject().get("builds").getAsJsonArray().get(0).getAsJsonObject().get("number").toString();
      String timestamp = root0.getAsJsonObject().get("timestamp").toString();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String time = sdf.format(new Date(Long.parseLong(timestamp)));

      if(jar_sha.equals(build_number))
        System.out.println(Message.getFormatString("update.latest", new Object[]{"1.9", jar_sha, build_number}));
      else {
        System.out.println(Message.getFormatString("update.detect", new Object[]{build_number, jar_sha, time}));
        if(bMohist("check_update_auto_download"))
          downloadFile("https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.12.2/lastSuccessfulBuild/artifact/projects/mohist/build/libs/mohist-" + build_number + "-server.jar", new File(new File(Mohist.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1)).getName()));
      }
    } catch (Throwable e) {
      System.out.println(Message.getString("check.update.noci"));
    }
  }

  private static int percentage = 0;
  private static int fS = 0;
  public static void downloadFile(String URL, File f) throws Exception {
    fS = getConnLength(URL);
    System.out.println(Message.getFormatString("file.download.start", new Object[]{f.getName(), getSize(fS)}));
    Timer t = new Timer();
    t.schedule(new TimerTask() {
      @Override
      public void run() {
        if(percentage != Math.round((float)f.length()/fS*100))
          System.out.println(Message.getFormatString("file.download.percentage", new Object[]{f.getName(), percentage}));
        percentage = Math.round((float)f.length()/fS*100);
      }
    }, 3000, 1000);
    FileChannel.open(Paths.get(f.getAbsolutePath()), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING).transferFrom(Channels.newChannel(getInput(URL)), 0L, Long.MAX_VALUE);
    t.cancel();
    if(URL.contains("codemc.io"))
      restartServer(new ArrayList<>(Arrays.asList("java", "-jar", new File(Mohist.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1)).getName())));
    else System.out.println(Message.getFormatString("file.download.ok", new Object[]{f.getName()}));
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
