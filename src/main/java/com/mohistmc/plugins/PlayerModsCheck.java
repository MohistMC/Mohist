package com.mohistmc.plugins;

import com.mohistmc.MohistConfig;
import com.mohistmc.api.PlayerAPI;
import com.mohistmc.api.event.PlayerModsCheckEvent;
import com.mohistmc.tools.ListUtils;
import com.mojang.authlib.GameProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/13 14:24:16
 */
public class PlayerModsCheck {

    private static final AtomicBoolean canLog = new AtomicBoolean(true);

    public static void init(GameProfile profile, String mods) {
        List<String> modlist = fixList(mods);
        PlayerAPI.modlist.put(profile.getId(), modlist);
        var event = new PlayerModsCheckEvent(profile, modlist);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            throw new IllegalStateException(event.message());
        }
        List<String> whitelist_modlist_Map = new ArrayList<>();
        List<String> blacklist_modlist_Map = new ArrayList<>();
        if (MohistConfig.modlist_check_whitelist_enable) {
            for (String config : modlist) {
                if (!server_modlist_whitelist().contains(config)) {
                    whitelist_modlist_Map.add(config);
                }
            }
            if (!whitelist_modlist_Map.isEmpty()) {
                canLog.set(false);
                throw new IllegalStateException(MohistConfig.modlist_check_whitelist_message.replace("%modlist%", whitelist_modlist_Map.toString()));
            }
        }
        if (MohistConfig.modlist_check_blacklist_enable && MohistConfig.modlist_check_blacklist != null) {
            for (String config : MohistConfig.modlist_check_blacklist) {
                if (modlist.contains(config)) {
                    blacklist_modlist_Map.add(config);
                }
            }
            if (!blacklist_modlist_Map.isEmpty()) {
                canLog.set(false);
                throw new IllegalStateException(MohistConfig.modlist_check_whitelist_message.replace("%modlist%", blacklist_modlist_Map.toString()));
            }
        }
    }

    public static List<String> server_modlist_whitelist() {
        return fixList(MohistConfig.modlist_check_whitelist);
    }

    public static List<String> fixList(String string) {
        var s = string.replaceAll("\\[|null|]| +", "");
        return Pattern.compile("\\s*,\\s*").splitAsStream(s).collect(Collectors.toList());
    }
}
