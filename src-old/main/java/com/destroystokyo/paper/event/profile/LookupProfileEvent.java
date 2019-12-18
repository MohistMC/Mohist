package com.destroystokyo.paper.event.profile;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.mojang.authlib.GameProfile;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Allows a plugin to be notified anytime AFTER a Profile has been looked up from the Mojang API
 * This is an opportunity to view the response and potentially cache things.
 *
 * No guarantees are made about thread execution context for this event. If you need to know, check
 * event.isAsync()
 */
public class LookupProfileEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final PlayerProfile profile;

    public LookupProfileEvent(@Nonnull PlayerProfile profile) {
        super(!Bukkit.isPrimaryThread());
        this.profile = profile;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @return The profile that was recently looked up. This profile can be mutated
     * @deprecated will be removed with 1.13, use {@link #getPlayerProfile()}
     */
    @Deprecated
    @Nonnull
    public GameProfile getProfile() {
        return profile.getGameProfile();
    }

    /**
     * @return The profile that was recently looked up. This profile can be mutated
     */
    @Nonnull
    public PlayerProfile getPlayerProfile() {
        return profile;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
