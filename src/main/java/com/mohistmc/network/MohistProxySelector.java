package com.mohistmc.network;

import com.mohistmc.api.event.MohistNetworkEvent;
import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.List;
import org.bukkit.Bukkit;

public class MohistProxySelector extends ProxySelector {

    private ProxySelector defaultSelector;

    public MohistProxySelector(ProxySelector defaultSelector) {
        this.defaultSelector = defaultSelector;
    }

    @Override
    public List<Proxy> select(URI uri) {
        if (uri.toString().startsWith("socket")) {
            return this.defaultSelector.select(uri);
        }
        MohistNetworkEvent event = new MohistNetworkEvent(uri, "§6[§aNetworkManager§6] §aNetwork protection and blocked by network rules!");
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            try {
                throw new IOException(event.getMsg());
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
