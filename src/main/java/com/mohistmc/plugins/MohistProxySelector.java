package com.mohistmc.plugins;

import com.mohistmc.MohistConfig;
import com.mohistmc.MohistMC;
import com.mohistmc.api.event.MohistNetworkEvent;
import com.mohistmc.tools.IOUtil;
import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.List;
import org.bukkit.Bukkit;

public class MohistProxySelector extends ProxySelector {

    private final ProxySelector defaultSelector;

    public MohistProxySelector(ProxySelector defaultSelector) {
        this.defaultSelector = defaultSelector;
    }

    @Override
    public List<Proxy> select(URI uri) {
        if (MohistConfig.networkmanager_debug) {
            MohistMC.LOGGER.error(uri.toString());
        }

        String uriString = uri.toString();
        String defaultMsg = "[NetworkManager] Network protection and blocked by network rules!";
        boolean intercept = false;
        if (MohistConfig.networkmanager_intercept != null) {
            for (String config_uri : MohistConfig.networkmanager_intercept) {
                if (uriString.contains(config_uri)) {
                    intercept = true;
                    break;
                }
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
