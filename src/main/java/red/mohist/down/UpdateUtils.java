package red.mohist.down;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import red.mohist.Mohist;
import red.mohist.configuration.MohistConfigUtil;
import red.mohist.util.i18n.Message;

public class UpdateUtils {
    static String ci_sha, jar_sha, time;

    public static void versionCheck() throws Exception {
        System.out.println(Message.getString("update.check"));
        System.out.println(Message.getString("update.stopcheck"));

        JsonElement root = null;
        URLConnection request;

        try {
            request = new URL("https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.12.2/lastSuccessfulBuild/api/json").openConnection();
            request.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            request.connect();
            root = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
        } catch (Exception e) {
            System.out.println("[!] Cannot connect to Jenkins server to check updates! " + e.toString());
        }
        try {
            time = root.getAsJsonObject().get("changeSet").getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject().get("date").toString().replace("+0800", "").replaceAll("\"", "");
            ci_sha = "1.12.2-" + root.getAsJsonObject().get("changeSet").getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject().get("commitId").toString().replaceAll("\"", "").substring(0, 7);
            jar_sha = UpdateUtils.class.getPackage().getImplementationVersion();
        } catch (Throwable e) {}
        try {
            if (jar_sha.equals(ci_sha)) {
                System.out.println(Message.getFormatString("update.latest", new Object[]{"1.8", jar_sha, ci_sha}));
            } else {
                System.out.println(Message.getFormatString("update.detect", new Object[]{ci_sha, jar_sha, time.substring(0, 10), time.substring(11, 19)}));
                if (isDownload()) {
                    downloadNewJar();
                }
            }
        } catch (Throwable e) {}
    }

    private static void downloadNewJar() throws IOException {
        String url = "https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.12.2/lastSuccessfulBuild/artifact/build/distributions/Mohist-" + ci_sha + "-server.jar";
        File f = new File(new File(Mohist.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1)).getName());

        System.out.println(Message.getFormatString("update.dl", new Object[]{UpdateUtils.getSize(getConnLength(url)), f}));
        UpdateUtils.downloadFile(url, f);
        System.out.println(Message.getString("update.finish.restart"));
        System.exit(0);
    }

    public static int getConnLength(String URL) throws IOException {
        URLConnection conn = new URL(URL).openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
        conn.connect();
        return conn.getContentLength();
    }

    //GET THE CORRECT SIZE OF A FILE
    public static String getSize(long size) {
        if (size >= 1048576L) return (float) size / 1048576.0F + " MB";
        if (size >= 1024) return (float) size / 1024.0F + " KB";
        return size + " B";
    }

    /*GET THE SIZE OF THE LIBRARIES DIRECTORY TO MAKE GLOBAL PERCENTAGE*/
    public static long getSizeOfDirectory(File file) {
        long size = 0;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File value : files) {
                size += getSizeOfDirectory(value);
            }
        } else size += file.length();
        return size;
    }

    public static void downloadFile(String URL, File f) throws IOException {
        URLConnection conn = new URL(URL).openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
        conn.connect();
        if (!f.exists()) f.createNewFile();
        Runnable percentage = () -> System.out.println(String.valueOf((float) f.length() / conn.getContentLength() * 100).substring(0, 2).replace(".", "") + "%");
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(percentage, 0, 500, TimeUnit.MILLISECONDS);
        FileUtils.copyInputStreamToFile(conn.getInputStream(), f); //Download
        executor.shutdown();
    }

    public static boolean isCheckLibs() {
        return MohistConfigUtil.getBoolean(new File("mohist-config", "mohist.yml"), "check_libraries:");
    }

    public static boolean isCheckUpdate() {
        return MohistConfigUtil.getBoolean(new File("mohist-config", "mohist.yml"), "check_update:");
    }

    public static boolean isDownload() {
        return MohistConfigUtil.getBoolean(new File("mohist-config", "mohist.yml"), "check_update_auto_download:");
    }
}
