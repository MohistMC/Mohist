package red.mohist.bukkit;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import red.mohist.util.ClassJarUtil;
import red.mohist.util.i18n.Message;

public class AutoDeletePlugins {

    public static void jar() {
        System.out.println(Message.getString("update.plugins"));
        String libDir = "plugins";
        if (!new File(libDir).exists()) new File(libDir).mkdir();
        JsonElement root = null;
        URLConnection request;
        try {
            request = new URL("https://shawiizz.github.io/plugins.json").openConnection();
            request.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            request.connect();
            root = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
        } catch (JsonIOException | JsonSyntaxException | IOException | NullPointerException ignored) {
        }

        try {
            for (String classname : root.getAsJsonObject().get("list").toString().replaceAll("\"", "").split(",")) {
                ClassJarUtil.checkFiles(libDir, classname, false);
            }
        } catch (Throwable ignored) {
        }

        try {
            for (String classname : root.getAsJsonObject().get("listupdates").toString().replaceAll("\"", "").split(",")) {
                try {
                    ClassJarUtil.copyPluginYMLandCheck(classname.substring(0, classname.indexOf("|")), classname.substring(classname.indexOf("|") + 1, classname.indexOf("#")), classname.substring(classname.indexOf("#") + 1), "plugin");
                } catch (Exception ignored) {
                }
            }
        } catch (Throwable ignored) {}

        try {
            for (String classname : root.getAsJsonObject().get("listpluginstoforge").toString().replaceAll("\"", "").split(",")) {
                try {
                    ClassJarUtil.copyPluginYMLandCheck(classname.substring(0, classname.indexOf("|")), "=é", classname.substring(classname.indexOf("|") + 1), "mod");
                } catch (Exception ignored) {
                }
            }
        } catch (Throwable e) {
            System.out.println("[!] Cannot connect to GitHub to check incompatible plugins!");
        }
    }
}
