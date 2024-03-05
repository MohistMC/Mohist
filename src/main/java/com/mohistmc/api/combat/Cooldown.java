package com.mohistmc.api.combat;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Cooldown {

    public static Cache<UUID, Long> cache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS).build();

    public static List<Player> playersInCombat = new ArrayList<Player>();

    public static List<Player> getPlayers() {
        final List<Player> players = new ArrayList<Player>();
        for(final Player player : Bukkit.getOnlinePlayers())
            if(Cooldown.cache.getIfPresent(player.getUniqueId()) !=  null)
                players.add(player);
        return players;
    }

    public static void setCache(Cache<UUID, Long> cache) {
        Cooldown.cache = cache;
    }
}
