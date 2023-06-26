package com.mohistmc.plugins.world;

import com.mohistmc.api.ServerAPI;
import com.mohistmc.plugins.world.utils.ConfigByWorlds;
import org.bukkit.Bukkit;

import java.io.File;


// TODO 核心内置 处理方式是否与插件相同？

public class WorldManage {

    public static final String command = "worlds";

    public static void onEnable() {
        ConfigByWorlds.createFile();
        ConfigByWorlds.loadWorlds();
        ConfigByWorlds.addWorld(ServerAPI.getNMSServer().server.getServer().getProperties().levelName);
    }


    public static void deleteDir(File path) {
        if (path.exists()) {
            File[] allContents = path.listFiles();
            if (allContents != null) {
                File[] array;
                for (int length = (array = allContents).length, i = 0; i < length; ++i) {
                    File file = array[i];
                    deleteDir(file);
                }
            }
            path.delete();
        }
    }

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}
