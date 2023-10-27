package com.mohistmc.plugins;

import com.mohistmc.MohistConfig;
import com.mohistmc.api.event.PlayerModsCheckEvent;
import com.mohistmc.tools.ListUtils;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.minecraft.network.Connection;
import org.bukkit.Bukkit;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/13 14:24:16
 */
public class PlayerModsCheck {

    private static AtomicBoolean canLog = new AtomicBoolean(true);

    public static void init(Connection connection, List<String> mods) {
        var event = new PlayerModsCheckEvent(connection.address, mods);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            throw new IllegalStateException(event.message());
        }
        if (MohistConfig.modlist_check_whitelist_enable) {
            if (!ListUtils.is(mods, server_modlist_whitelist())) {
                canLog.set(false);
                throw new IllegalStateException(MohistConfig.modlist_check_whitelist_message);
            }
        }
        if (MohistConfig.modlist_check_blacklist_enable && MohistConfig.modlist_check_blacklist != null) {
            for (String config : MohistConfig.modlist_check_blacklist) {
                if (mods.contains(config)) {
                    canLog.set(false);
                    throw new IllegalStateException(MohistConfig.modlist_check_blacklist_message);
                }
            }
        }
    }

    public static List<String> server_modlist_whitelist() {
        var s = MohistConfig.modlist_check_whitelist.replaceAll("\\[|null|]| +", "");
        return Pattern.compile("\\s*,\\s*").splitAsStream(s).collect(Collectors.toList());
    }

    public static boolean canLog() {
        return canLog.getAndSet(true);
    }

    public static boolean canLog(boolean trunDef) {
        return trunDef ? canLog.getAndSet(true) : canLog.get();
    }
}
