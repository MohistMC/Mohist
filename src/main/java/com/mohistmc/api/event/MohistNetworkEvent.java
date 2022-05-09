/*
 * MohistMC
 * Copyright (C) 2019-2022.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.api.event;

import java.net.URI;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MohistNetworkEvent extends Event implements Cancellable {

    private static HandlerList handlers = new HandlerList();
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
    public HandlerList getHandlers() {
        return handlers;
    }
}
