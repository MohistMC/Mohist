package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player changes their locale in the client settings.
 */
public class PlayerLocaleChangeEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    //
    private final String locale;

    // Paper start
    private final java.util.Locale adventure$locale;

    /**
     * @return the player's new locale
     * @see Player#getLocale()
     */
    public @NotNull java.util.Locale locale() {
        return this.adventure$locale;
    }
    // Paper end

    public PlayerLocaleChangeEvent(@NotNull Player who, @NotNull String locale) {
        super(who);
        this.locale = locale;
        this.adventure$locale = net.kyori.adventure.translation.Translator.parseLocale(locale); // Paper
    }

    /**
     * @return the player's new locale
     * @see Player#getLocale()
     * @deprecated in favour of {@link #locale()}
     */
    @NotNull
    @Deprecated // Paper
    public String getLocale() {
        return locale;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
