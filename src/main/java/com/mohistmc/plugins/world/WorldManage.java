package com.mohistmc.plugins.world;

import com.mohistmc.api.ServerAPI;
import com.mohistmc.plugins.world.utils.ConfigByWorlds;
import java.io.File;

public class WorldManage {

    public static void onEnable() {
        ConfigByWorlds.init();
        ConfigByWorlds.loadWorlds();
        ConfigByWorlds.addWorld(ServerAPI.getNMSServer().server.getServer().getProperties().levelName, false);
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
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
