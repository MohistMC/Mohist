package com.mohistmc.api.event;

import com.mohistmc.api.color.ColorsAPI;
import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.UUID;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Mgazul by MohistMC
 * @date 2023/10/27 17:11:27
 */
public class PlayerModsCheckEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private final String name;
    private final UUID uuid;

    private final List<String> mods;
    private String message = "Connection closed - PlayerModsCheck";

    public PlayerModsCheckEvent(GameProfile profile, List<String> mods) {
        super(true);
        this.name = profile.getName();
        this.uuid = profile.getId();
        this.mods = mods;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Will prevent players from entering your server
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public String name() {
        return this.name;
    }

    public UUID uuid() {
        return this.uuid;
    }

    /**
     *
     * @return Player's mod list, this is not 100% accurate as the client can hide modids
     */
    public List<String> mods() {
        return this.mods;
    }

    /**
     * Support RGB color
     * @return
     */
    public String message() {
        return ColorsAPI.of(message);
    }

    public void message(String message) {
        this.message = message;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
