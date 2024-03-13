package com.mohistmc.util;

import com.mohistmc.MohistMCStart;
import com.mohistmc.tools.FileUtils;
import com.mohistmc.tools.OSUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {

    public static final HashMap<String, String> versionMap = new HashMap<>();
    public static final List<String> launchArgs = new ArrayList<>();

    public static void parseVersions() {
        versionMap.put("forge", FileUtils.readFileFromJar(DataParser.class.getClassLoader(), "versions/forge.txt").get(0));
        versionMap.put("minecraft", FileUtils.readFileFromJar(DataParser.class.getClassLoader(), "versions/minecraft.txt").get(0));
        versionMap.put("mcp", FileUtils.readFileFromJar(DataParser.class.getClassLoader(), "versions/mcp.txt").get(0));
        versionMap.put("mohist", FileUtils.readFileFromJar(DataParser.class.getClassLoader(), "versions/mohist.txt").get(0));

        MohistMCStart.MCVERSION = versionMap.get("minecraft");
    }

    public static void parseLaunchArgs() {
        launchArgs.addAll(FileUtils.readFileFromJar(DataParser.class.getClassLoader(), "data/" + (OSUtil.getOS().equals(OSUtil.OS.WINDOWS) ? "win" : "unix") + "_args.txt"));
    }
}
