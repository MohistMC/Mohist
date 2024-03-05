package com.mohistmc.api.combat;

import com.google.common.cache.CacheBuilder;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CombatAPI extends JavaPlugin {

	public static void initialize(Long frequency) {
		new CombatChecker(frequency);
	}

	public static void initialize(Long frequency, boolean isPotionFighting) {
		Variables.isPotionFighting = isPotionFighting;
		new CombatChecker(frequency);
	}

	public static void initialize(Long frequency, int duration) {
		Cooldown.setCache(CacheBuilder.newBuilder().expireAfterWrite(duration, TimeUnit.SECONDS).build());
		new CombatChecker(frequency);
	}

	public static void initialize(Long frequency, int duration, boolean isPotionFighting) {
		Variables.isPotionFighting = isPotionFighting;
		Cooldown.setCache(CacheBuilder.newBuilder().expireAfterWrite(duration, TimeUnit.SECONDS).build());
		new CombatChecker(frequency);
	}

	public static void initialize(Long frequency, int duration, boolean isPotionFighting, boolean anyPotion, List<String> potionList, boolean isSplashFighting, boolean isLingeringFighting) {
		Variables.isPotionFighting = isPotionFighting;
		Variables.anyPotion = anyPotion;
		Variables.potionList = potionList;
		Variables.isSplashFighting = isSplashFighting;
		Variables.isLingeringFighting = isLingeringFighting;
		Cooldown.setCache(CacheBuilder.newBuilder().expireAfterWrite(duration, TimeUnit.SECONDS).build());
		new CombatChecker(frequency);
	}

	public static boolean isInCombat(Player player) {
		return Cooldown.cache.getIfPresent(player.getUniqueId()) != null;
	}

	public static List<Player> getPlayers() {
		return Cooldown.playersInCombat;
	}

	public static void removePlayer(Player player) {
		Cooldown.cache.invalidate(player.getUniqueId());
	}
}
