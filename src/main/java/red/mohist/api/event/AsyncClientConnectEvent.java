package red.mohist.api.event;

import io.netty.channel.Channel;
import net.minecraft.network.NetworkManager;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called right after a new client connection is initialized,
 * before any packets have been exchanged.
 *
 * Obviously, this is quite hacky. Please use for good and not evil.
 */
public class AsyncClientConnectEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Channel channel;
    private final NetworkManager networkManager;

    public AsyncClientConnectEvent(Channel channel, NetworkManager networkManager) {
        super(true);
        this.channel = channel;
        this.networkManager = networkManager;
    }

    public Channel channel() {
        return channel;
    }

    public NetworkManager networkManager() {
        return networkManager;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
