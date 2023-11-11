package com.mohistmc.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerAPI {

    public static Map<UUID, List<String>> modlist = new HashMap<>();

    public static ServerPlayer getNMSPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    public static int modsize(Player player) {
        return modlist(player).size();
    }

    public static List<String> modlist(Player player) {
        UUID uuid = player.getUniqueId();
        return modlist.get(uuid) == null ? Collections.emptyList() : modlist.get(uuid);
    }

    public static boolean hasMod(Player player, String modid) {
        return modlist(player).contains(modid);
    }

    public static boolean ignoreOp() {
        return CraftPlayer.ignoreOp.getAndSet(false);
    }

    public boolean performOpCommand(Player player, String command) {
        return player.performOpCommand(command);
    }
}
