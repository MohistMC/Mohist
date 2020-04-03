package red.mohist.down;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.FileUtils;
import red.mohist.Mohist;
import red.mohist.configuration.MohistConfigUtil;
import red.mohist.util.i18n.Message;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Update {
    static String ci_sha, jar_sha, time;

    public static void versionCheck() throws Exception {
        System.out.println(Message.getString("update.check"));
        System.out.println(Message.getString("update.stopcheck"));

        JsonElement root;
        URLConnection request;

        try {
            request = new URL("https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.12.2/lastSuccessfulBuild/api/json").openConnection();
            request.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            request.connect();
            root = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
        } catch (JsonIOException | JsonSyntaxException | IOException | NullPointerException e) { return; }

        time = root.getAsJsonObject().get("changeSet").getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject().get("date").toString().replace("+0800", "").replaceAll("\"", "");
        ci_sha = root.getAsJsonObject().get("changeSet").getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject().get("commitId").toString().replaceAll("\"", "").substring(0, 7);
        jar_sha = Update.class.getPackage().getImplementationVersion();

        if(jar_sha.equals(ci_sha)) {
            System.out.println(Message.getFormatString("update.latest", new Object[]{"1.7", jar_sha, ci_sha}));
        } else {
            if(isDownload()) {
                System.out.println(Message.getFormatString("update.detect", new Object[]{ci_sha, jar_sha, time.substring(0, 10), time.substring(11, 19)}));
                System.out.println(Message.getString("update.select"));
                if(new Scanner(System.in).next().equals("yes")) {
                    downloadLatestJar();
                }
            }
        }
    }

    private static void downloadLatestJar() throws IOException {
        URLConnection conn = new URL("https://ci.codemc.io/job/Mohist-Community/job/Mohist-1.12.2/lastSuccessfulBuild/artifact/build/distributions/Mohist-" + Update.ci_sha + "-server.jar").openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
        conn.connect();
        System.out.println(Message.getFormatString("update.dl", new Object[]{getSize(conn.getContentLength())}));
        FileUtils.copyInputStreamToFile(conn.getInputStream(), new File(new File(Mohist.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1)).getName())); //Download

        System.out.println(Message.getString("update.finish"));
        System.exit(0);
    }

    public static String getSize(long size) {
        if (size >= 1048576L) return (float) size / 1048576.0F + " MB";
        if (size >= 1024) return (float) size / 1024.0F + " KB";
        return size + " B";
    }

    public static boolean isCheck() { return MohistConfigUtil.getBoolean(new File("mohist-config", "mohist.yml"), "check_update:"); }
    public static boolean isDownload() { return MohistConfigUtil.getBoolean(new File("mohist-config", "mohist.yml"), "check_update_auto_download:"); }
}
