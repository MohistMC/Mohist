/*
 * Copyright (c) 2017 - Daniel Ennis (Aikar) - MIT License
 *
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.destroystokyo.paper.event.profile;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.mojang.authlib.GameProfile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fires when the server needs to verify if a player is whitelisted.
 * <p>
 * Plugins may override/control the servers whitelist with this event,
 * and dynamically change the kick message.
 */
public class ProfileWhitelistVerifyEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final PlayerProfile profile;
    private final boolean whitelistEnabled;
    private final boolean isOp;
    private boolean whitelisted;
    private String kickMessage;

    public ProfileWhitelistVerifyEvent(final PlayerProfile profile, boolean whitelistEnabled, boolean whitelisted, boolean isOp, String kickMessage) {
        this.profile = profile;
        this.whitelistEnabled = whitelistEnabled;
        this.whitelisted = whitelisted;
        this.isOp = isOp;
        this.kickMessage = kickMessage;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @return the currently planned message to send to the user if they are not whitelisted
     */
    public String getKickMessage() {
        return kickMessage;
    }

    /**
     * @param kickMessage The message to send to the player on kick if not whitelisted. May set to null to use the server configured default
     */
    public void setKickMessage(String kickMessage) {
        this.kickMessage = kickMessage;
    }

    /**
     * @return the gameprofile of the player trying to connect
     * @deprecated Will be removed in 1.13, use #{@link #getPlayerProfile()}
     */
    @Deprecated
    public GameProfile getProfile() {
        return profile.getGameProfile();
    }

    /**
     * @return The profile of the player trying to connect
     */
    public PlayerProfile getPlayerProfile() {
        return profile;
    }

    /**
     * @return Whether the player is whitelisted to play on this server (whitelist may be off is why its true)
     */
    public boolean isWhitelisted() {
        return whitelisted;
    }

    /**
     * Changes the players whitelisted state. false will deny the login
     *
     * @param whitelisted The new whitelisted state
     */
    public void setWhitelisted(boolean whitelisted) {
        this.whitelisted = whitelisted;
    }

    /**
     * @return if the player obtained whitelist status by having op
     */
    public boolean isOp() {
        return isOp;
    }

    /**
     * @return if the server even has whitelist on
     */
    public boolean isWhitelistEnabled() {
        return whitelistEnabled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
