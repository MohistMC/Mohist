package com.mohistmc.mohistlauncher.util;

import com.mohistmc.mohistlauncher.Main;
import com.mohistmc.tools.FileUtils;
import java.util.HashMap;

public class DataParser {

    public static final HashMap<String, String> versionMap = new HashMap<>();

    public static void parseVersions() {
        versionMap.put("forge", FileUtils.readFileFromJar(DataParser.class.getClassLoader(), "versions/forge.txt").get(0));
        versionMap.put("minecraft", FileUtils.readFileFromJar(DataParser.class.getClassLoader(), "versions/minecraft.txt").get(0));
        versionMap.put("mcp", FileUtils.readFileFromJar(DataParser.class.getClassLoader(), "versions/mcp.txt").get(0));
        versionMap.put("mohist",FileUtils.readFileFromJar(DataParser.class.getClassLoader(), "versions/mohist.txt").get(0));

        Main.MCVERSION = versionMap.get("minecraft");
    }
}
