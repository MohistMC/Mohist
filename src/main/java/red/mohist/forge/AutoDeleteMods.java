package red.mohist.forge;

import com.google.gson.JsonParser;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import red.mohist.util.ClassJarUtil;
import red.mohist.util.i18n.Message;

public class AutoDeleteMods {
    private static String libDir = "mods";

    private static String[] getInfos(byte type) {
        String[] l = null;
        URLConnection request = null;
        try {
            request = new URL("https://shawiizz.github.io/mods.json").openConnection();
            request.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            request.connect();
            if (type == 1) {
                l = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject().get("list").toString().replaceAll("\"", "").split(",");
            }
            if (type == 2) {
                l = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject().get("implemented").toString().replaceAll("\"", "").split(",");
            }
        } catch (Throwable e) {
            l = new String[] {"org.spongepowered.mod.SpongeMod" /*SpongeForge*/,
                    "org.dimdev.vanillafix.VanillaFix" /*VanillaFix*/,
                    "lumien.custommainmenu.CustomMainMenu" /*CustomMainMenu*/,
                    "com.performant.coremod.Performant" /*Performant*/,
                    "shadows.fastbench.proxy.BenchServerProxy" /*FastWorkbench*/,
                    "optifine.Differ" };
        }

        return l;
    }

    public static void jar() {
        System.out.println(Message.getString("update.mods"));
        if (!new File(libDir).exists()) new File(libDir).mkdir();
        try {
            for (String classname : getInfos((byte) 1)) {
                ClassJarUtil.checkFiles(libDir, classname, false);
            }
        } catch (Throwable e) {
        }
    }

    public static void jarDisabled() {
        System.out.println(Message.getString("update.mods.implemented"));
        if (!new File(libDir).exists()) new File(libDir).mkdir();
        try {
            for (String classname : getInfos((byte) 2)) {
                ClassJarUtil.checkFiles(libDir, classname, true);
            }
        } catch (Throwable e) {
            System.out.println("[!] Cannot connect to GitHub to check incompatible mods!");
        }
    }
}
