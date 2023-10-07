package com.mohistmc.plugins;

import com.mohistmc.MohistConfig;
import com.mohistmc.tools.ListUtils;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/13 14:24:16
 */
public class PlayerModsCheck {

    private static AtomicBoolean canLog = new AtomicBoolean(true);

    public static boolean init(List<String> stringList) {
        if (MohistConfig.server_modlist_whitelist_enable) {
            if (!ListUtils.is(stringList, server_modlist_whitelist())) {
                canLog.set(false);
                return true;
            }
        }
        if (MohistConfig.player_modlist_blacklist_enable && MohistConfig.player_modlist_blacklist != null) {
            for (String config : MohistConfig.player_modlist_blacklist) {
                if (stringList.contains(config)) {
                    canLog.set(false);
                    return true;
                }
            }
        }
        return false;
    }

    public static List<String> server_modlist_whitelist() {
        String s = MohistConfig.server_modlist_whitelist.replaceAll("(?:\\[|null|\\]| +)", "");
        return Pattern.compile("\\s*,\\s*").splitAsStream(s).collect(Collectors.toList());
    }

    public static boolean canLog(boolean trunDef) {
        return trunDef ? canLog.getAndSet(true) : canLog.get();
    }
}
