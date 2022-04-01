package com.mohistmc.forge;

import com.mohistmc.configuration.MohistConfig;
import java.util.Arrays;
import java.util.Map;

public class MohistForgeUtils {

    // Some mods such as Twilight Forest listen for specific events as their WorldProvider loads to hotload its dimension. This prevents this from happening so MV can create worlds using the same provider without issue.
    public static boolean craftWorldLoading = false;

    public static boolean modsblacklist(String modslist) {
        String[] clientMods = modslist.split(",");
        if (MohistConfig.instance.modsblacklistenable.getValue() && !MohistConfig.instance.modswhitelistenable.getValue()) {
            return Arrays.asList(clientMods).containsAll(Arrays.asList(MohistConfig.instance.modsblacklist.getValue().split(",")));
        }
        return false;
    }

    public static boolean modswhittelist(String modslist) {
        String[] clientMods = modslist.split(",");
        if (!MohistConfig.instance.modsblacklistenable.getValue() && MohistConfig.instance.modswhitelistenable.getValue()) {
            if (MohistConfig.instance.modsnumber.getValue() >= 0)
                return Arrays.asList(clientMods).containsAll(Arrays.asList(MohistConfig.instance.modswhitelist.getValue().split(","))) && clientMods.length == MohistConfig.instance.modsnumber.getValue();
            else
                return Arrays.asList(clientMods).containsAll(Arrays.asList(MohistConfig.instance.modswhitelist.getValue().split(",")));
        }
        return true;
    }

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