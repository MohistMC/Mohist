package com.mohistmc.forge;

import java.util.Arrays;
import com.mohistmc.configuration.MohistConfig;

public class MohistForgeUtils {

    // Some mods such as Twilight Forest listen for specific events as their WorldProvider loads to hotload its dimension. This prevents this from happening so MV can create worlds using the same provider without issue.
    public static boolean craftWorldLoading = false;

    public static boolean modsblacklist(String modslist) {
        String[] clientMods = modslist.split(",");
        if (MohistConfig.instance.modsblacklistenable.getValue()) {
            return !Arrays.asList(clientMods).containsAll(Arrays.asList(MohistConfig.instance.modsblacklist.getValue().split(",")));
        }
        return false;
    }
}