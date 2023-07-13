package com.mohistmc.plugins;

import com.mohistmc.MohistConfig;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/13 14:24:16
 */
public class PlayerModsCheck {

    private static AtomicBoolean canLog = new AtomicBoolean(true);

    public static boolean init(List<String> stringList) {
        if (MohistConfig.player_modlist_blacklist_enable) {
            for (String config : MohistConfig.player_modlist_blacklist) {
                if (stringList.contains(config)) {
                    canLog.set(false);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean canLog(boolean trunDef) {
        return trunDef ? canLog.getAndSet(true) : canLog.get();
    }
}
