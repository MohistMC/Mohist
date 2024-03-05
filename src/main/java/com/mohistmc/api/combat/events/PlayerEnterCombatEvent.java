package com.mohistmc.api.combat.events;

import com.mohistmc.api.combat.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerEnterCombatEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();

	private boolean isCancelled;
	private final Player player;

	public PlayerEnterCombatEvent(Player player) {
		this.player = player;
		if(!Cooldown.playersInCombat.contains(player))
			Cooldown.playersInCombat.add(player);
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.isCancelled = cancel;
	}

	public Player getPlayer() {
		return player;
	}
}
