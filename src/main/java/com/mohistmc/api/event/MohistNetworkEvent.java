package com.mohistmc.api.event;

import java.net.URI;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MohistNetworkEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private URI url;
    private String msg;

    public MohistNetworkEvent(final URI url, final String msg) {
        super(!Bukkit.getServer().isPrimaryThread());
        this.url = url;
        this.msg = msg;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public URI getUrl() {
        return this.url;
    }

    public void setUrl(final URI url) {
        this.url = url;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
