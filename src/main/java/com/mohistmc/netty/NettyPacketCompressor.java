package com.mohistmc.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.zip.Deflater;
import net.minecraft.network.PacketBuffer;

public class NettyPacketCompressor extends MessageToByteEncoder<ByteBuf> {
    private final byte[] encodeBuf = new byte[8192];
    private final Deflater deflater;
    private int threshold;
    private static final boolean DISABLE_PACKET_DEBUG = Boolean.parseBoolean(System.getProperty("forge.disablePacketCompressionDebug", "true"));
    private static final org.apache.logging.log4j.Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger();

    public NettyPacketCompressor(int p_i46005_1_) {
        this.threshold = p_i46005_1_;
        this.deflater = new Deflater();
    }

    protected void encode(ChannelHandlerContext p_encode_1_, ByteBuf p_encode_2_, ByteBuf p_encode_3_) throws Exception {
        int i = p_encode_2_.readableBytes();
        PacketBuffer packetbuffer = new PacketBuffer(p_encode_3_);
        if (i < this.threshold) {
            packetbuffer.writeVarInt(0);
            packetbuffer.writeBytes(p_encode_2_);
        } else {
            if (!DISABLE_PACKET_DEBUG && i > 2097152) {
                p_encode_2_.markReaderIndex();
                LOGGER.error("Attempted to send packet over maximum protocol size: {} > 2097152\nData:\n{}", i,
                        net.minecraftforge.fml.common.network.ByteBufUtils.getContentDump(p_encode_2_));
                p_encode_2_.resetReaderIndex();
            }
            byte[] abyte = new byte[i];
            p_encode_2_.readBytes(abyte);
            packetbuffer.writeVarInt(abyte.length);
            this.deflater.setInput(abyte, 0, i);
            this.deflater.finish();

            while(!this.deflater.finished()) {
                int j = this.deflater.deflate(this.encodeBuf);
                packetbuffer.writeBytes(this.encodeBuf, 0, j);
            }

            this.deflater.reset();
        }

    }

    public void setThreshold(int p_179299_1_) {
        this.threshold = p_179299_1_;
    }
}
