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

package com.mohistmc;

import com.mohistmc.api.event.MohistNetworkEvent;
import com.mohistmc.configuration.MohistConfig;
import com.mohistmc.util.IOUtil;
import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;

public class MohistProxySelector extends ProxySelector {

    private final ProxySelector defaultSelector;
    private List<String> intercepts = new ArrayList<>();

    public MohistProxySelector(ProxySelector defaultSelector) {
        this.defaultSelector = defaultSelector;
    }

    @Override
    public List<Proxy> select(URI uri) {
        if (MohistConfig.getBoolean0("mohist.networkmanager.debug", false)) {
            MohistMC.LOGGER.error(uri.toString());
        }

        String uriString = uri.toString();
        String defaultMsg = "[NetworkManager] Network protection and blocked by network rules!";
        boolean intercept = false;

        /*
        if (uriString.startsWith("socket")) {
            return this.defaultSelector.select(uri);
        }
         */
        if (intercepts.isEmpty()) {
            intercepts = MohistConfig.getStringList0("mohist.networkmanager.intercept", new ArrayList<>());
        }
        for (String config_uri : intercepts) {
            if (uriString.contains(config_uri)) {
                intercept = true;
            }
        }
        if (Bukkit.getServer() != null && Bukkit.getServer().isPrimaryThread()) {
            MohistNetworkEvent event = new MohistNetworkEvent(uri, defaultMsg);
            Bukkit.getPluginManager().callEvent(event);
            event.setCancelled(intercept);
            if (event.isCancelled()) {
                intercept = true;
            }
        }
        if (intercept) {
            try {
                IOUtil.throwException(new IOException(defaultMsg));
            } catch (Throwable throwable) {
                MohistMC.LOGGER.error(throwable.getMessage());
            }
        } else {
            return this.defaultSelector.select(uri);
        }
        return null;
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        this.defaultSelector.connectFailed(uri, sa, ioe);
    }

    public ProxySelector getDefaultSelector() {
        return this.defaultSelector;
    }
}
