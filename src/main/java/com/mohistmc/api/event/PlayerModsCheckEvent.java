package com.mohistmc.api.event;

import com.mohistmc.api.color.ColorsAPI;
import java.net.SocketAddress;
import java.util.List;
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
    private final SocketAddress address;
    private final List<String> mods;
    private String message = "Connection closed - PlayerModsCheck";

    public PlayerModsCheckEvent(SocketAddress address, List<String> mods) {
        super(true);
        this.address = address;
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

    public SocketAddress address() {
        return this.address;
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
