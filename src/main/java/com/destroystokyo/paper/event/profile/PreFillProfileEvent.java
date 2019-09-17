/*
 * Copyright (c) 2018 Daniel Ennis (Aikar) MIT License
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
import com.destroystokyo.paper.profile.ProfileProperty;
import java.util.Collection;
import javax.annotation.Nonnull;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fired when the server is requesting to fill in properties of an incomplete profile, such as textures.
 *
 * Allows plugins to pre populate cached properties and avoid a call to the Mojang API
 */
public class PreFillProfileEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final PlayerProfile profile;

    public PreFillProfileEvent(PlayerProfile profile) {
        super(!org.bukkit.Bukkit.isPrimaryThread());
        this.profile = profile;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @return The profile that needs its properties filled
     */
    public PlayerProfile getPlayerProfile() {
        return profile;
    }

    /**
     * Sets the properties on the profile, avoiding the call to the Mojang API
     * Same as .getPlayerProfile().setProperties(properties);
     *
     * @see PlayerProfile#setProperties(Collection)
     * @param properties The properties to set/append
     */
    public void setProperties(@Nonnull Collection<ProfileProperty> properties) {
        profile.setProperties(properties);
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
