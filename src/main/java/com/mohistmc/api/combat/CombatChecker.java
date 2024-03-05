package com.mohistmc.api.combat;

import com.mohistmc.api.combat.events.PlayerLeaveCombatEvent;
import com.mohistmc.plugins.MohistPlugin;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CombatChecker {

	public CombatChecker(Long frequence) {
		new BukkitRunnable() {
			List<Player> previousPlayers = Cooldown.getPlayers();

			@Override
			public void run() {
				final List<Player> currentPlayers = Cooldown.getPlayers();
				for(final Player player : previousPlayers)
					if(!currentPlayers.contains(player)) {
						final PlayerLeaveCombatEvent event = new PlayerLeaveCombatEvent(player);
						Bukkit.getServer().getPluginManager().callEvent(event);
					}
				previousPlayers = currentPlayers;
			}
		}.runTaskTimer(MohistPlugin.plugin, 0L, frequence);
	}
}
