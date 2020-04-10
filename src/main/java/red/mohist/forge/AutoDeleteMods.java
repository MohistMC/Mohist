package red.mohist.forge;

import com.google.gson.JsonParser;
import red.mohist.util.ClassJarUtil;
import red.mohist.util.i18n.Message;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class AutoDeleteMods {
    private static String libDir = "mods";

    private static String[] getInfos(byte type) throws IOException {
        String[] l = null;
        URLConnection request = null;
        try {
            request = new URL("https://shawiizz.github.io/mods.json").openConnection();
            request.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            request.connect();
        } catch (Exception ignored) {
        }
        if(type == 1)
            l = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject().get("list").toString().replaceAll("\"", "").split(",");
        if(type == 2)
            l = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject().get("implemented").toString().replaceAll("\"", "").split(",");
        return l;
    }

    public static void jar() throws Exception {
        System.out.println(Message.getString("update.mods"));
        if(!new File(libDir).exists()) new File(libDir).mkdir();
        for (String classname : getInfos((byte) 1)) {
            ClassJarUtil.checkFiles(libDir, classname, false);
        }
    }

    public static void jarDisabled() throws Exception {
        System.out.println(Message.getString("update.mods.implemented"));
        if(!new File(libDir).exists()) new File(libDir).mkdir();
        for (String classname : getInfos((byte) 2)) {
            ClassJarUtil.checkFiles(libDir, classname, true);
        }
    }
}
