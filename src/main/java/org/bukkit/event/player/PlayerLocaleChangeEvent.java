package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Called when a player changes their locale in the client settings.
 */
public class PlayerLocaleChangeEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    //
    private final String locale;

    public PlayerLocaleChangeEvent(Player who, String locale) {
        super(who);
        this.locale = locale;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @return the player's new locale
     * @see Player#getLocale()
     */
    public String getLocale() {
        return locale;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
