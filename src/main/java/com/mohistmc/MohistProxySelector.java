package com.mohistmc;

import com.mohistmc.api.event.MohistNetworkEvent;
import com.mohistmc.configuration.MohistConfig;
import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.List;
import org.bukkit.Bukkit;

public class MohistProxySelector extends ProxySelector {

    private ProxySelector defaultSelector;
    private List<String> intercepts;

    public MohistProxySelector(ProxySelector defaultSelector) {
        this.defaultSelector = defaultSelector;
        intercepts = MohistConfig.instance.config.getStringList("mohist.networkmanager.intercept");
    }

    @Override
    public List<Proxy> select(URI uri) {
        if (MohistConfig.instance.getBoolean("mohist.networkmanager.debug", false)) {
            MohistMC.LOGGER.error(uri.toString());
        }

        String uriString = uri.toString();
        String defaultMsg = "§6[§aNetworkManager§6] §aNetwork protection and blocked by network rules!";
        boolean intercept = false;

        /*
        if (uriString.startsWith("socket")) {
            return this.defaultSelector.select(uri);
        }
         */
        if (Bukkit.getServer() != null) {
            MohistNetworkEvent event = new MohistNetworkEvent(uri, defaultMsg);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                intercept = true;
            }
        } else {
            for (String config_uri : intercepts) {
                if (uriString.contains(config_uri)) {
                    intercept = true;
                }
            }
        }
        if (intercept) {
            try {
                throw new IOException(defaultMsg);
            } catch (Throwable ignored) {}
        }

        return this.defaultSelector.select(uri);
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        this.defaultSelector.connectFailed(uri, sa, ioe);
    }

    public ProxySelector getDefaultSelector() {
        return this.defaultSelector;
    }
}