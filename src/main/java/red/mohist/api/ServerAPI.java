package red.mohist.api;

import io.netty.util.internal.ConcurrentSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.MinecraftServer;
import red.mohist.util.i18n.Message;

public class ServerAPI {

    public static Map<String, Integer> mods = new ConcurrentHashMap();
    public static Set<String> modlists = new ConcurrentSet();
    public static Map<String, Integer> injectmaterials = new ConcurrentHashMap();
    public static Map<String, Integer> injectblock = new ConcurrentHashMap();
    public static Map<String, String> forgecmdper = new ConcurrentHashMap();

    public static int getModSize() {
        return mods.get("mods") == null ? 0 : mods.get("mods") - 4;
    }

    public static String getModList() {
        return modlists.toString();
    }

    public static Boolean hasMod(String modid) {
        return getModList().contains(modid);
    }

    public static MinecraftServer getNMSServer(){
        return MinecraftServer.getServer();
    }

    public static String getLanguage(){
        return Message.rb.getLocale().toString();
    }
}
