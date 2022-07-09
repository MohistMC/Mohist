package com.mohistmc.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {

    public static HashMap<String, String> versionMap = new HashMap<>();
    public static List<String> launchArgs = new ArrayList<>();
    public static List<File> librariesClassPath = new ArrayList<>();

    public static void parseVersions() {
        versionMap.put("forge", FileUtil.readFileFromJar("versions/forge.txt").get(0));
        versionMap.put("minecraft", FileUtil.readFileFromJar("versions/minecraft.txt").get(0));
        versionMap.put("mcp", FileUtil.readFileFromJar("versions/mcp.txt").get(0));
        versionMap.put("mohist", FileUtil.readFileFromJar("versions/mohist.txt").get(0));
    }

    public static void parseLaunchArgs() {
        launchArgs.addAll(FileUtil.readFileFromJar("data/" + (OSUtil.getOS().equals(OSUtil.OS.WINDOWS) ? "win" : "unix") + "_args.txt"));
    }

    public static void parseLibrariesClassPath() {
        for (String lib : FileUtil.readFileFromJar("data/libraries.txt").get(0).split(";")) {
            librariesClassPath.add(new File(lib));
        }
    }
}
