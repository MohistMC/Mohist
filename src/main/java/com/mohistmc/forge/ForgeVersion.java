package com.mohistmc.forge;

import com.mohistmc.configuration.MohistConfigUtil;
import java.io.File;
import java.util.Map;

public class ForgeVersion {

    private static final File f = new File("mohist-config", "mohist.yml");
    public static final int major = MohistConfigUtil.getInt(f, "major:", "14");
    public static final int minor = MohistConfigUtil.getInt(f, "minor:", "23");
    public static final int revision = MohistConfigUtil.getInt(f, "revision:", "5");
    public static final int build = MohistConfigUtil.getInt(f, "build:", "2854");

    public static boolean isCompatibleLowForge(Map<String, String> modList) {
        String forgeVersion = modList.get("forge");
        if (forgeVersion != null) {
            try {
                if (Integer.parseInt(forgeVersion.split("\\.")[3]) < 2826) {
                    return false;
                }
            } catch (Exception e) {
                return true;
            }
        }
        return true;
    }
}
