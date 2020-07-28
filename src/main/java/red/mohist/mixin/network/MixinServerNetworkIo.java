package red.mohist.mixin.network;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.server.ServerNetworkIo;

import io.netty.channel.ChannelFuture;
import red.mohist.extra.network.ExtraNetworkIo;

@Mixin(ServerNetworkIo.class)
public class MixinServerNetworkIo implements ExtraNetworkIo {

    @Shadow
    @Final
    public List<ChannelFuture> channels;

    @Override
    public void acceptConnections() {
        synchronized (channels) {
            for (ChannelFuture future : channels)
                future.channel().config().setAutoRead(true);
        }
    }

}