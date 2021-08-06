package com.mohistmc;

import com.mohistmc.api.event.MohistNetworkEvent;
import com.mohistmc.configuration.MohistConfig;
import com.mohistmc.util.IOUtil;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
