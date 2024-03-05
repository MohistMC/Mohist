package com.mohistmc.api.combat.events;

import com.mohistmc.api.combat.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerLeaveCombatEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	private final Player player;

	public PlayerLeaveCombatEvent(Player player) {
		this.player = player;
		Cooldown.playersInCombat.remove(player);
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	public Player getPlayer() {
		return player;
	}
}
