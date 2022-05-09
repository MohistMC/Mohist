/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2022.
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

package com.mohistmc.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import java.util.List;
import java.util.zip.Inflater;
import net.minecraft.network.PacketBuffer;

public class NettyPacketDecompressor extends ByteToMessageDecoder {
   private final Inflater inflater;
   private int threshold;

   public NettyPacketDecompressor(int p_i46006_1_) {
      this.threshold = p_i46006_1_;
      this.inflater = new Inflater();
   }

   protected void decode(ChannelHandlerContext p_decode_1_, ByteBuf p_decode_2_, List<Object> p_decode_3_) throws Exception {
      if (p_decode_2_.readableBytes() != 0) {
         PacketBuffer packetbuffer = new PacketBuffer(p_decode_2_);
         int i = packetbuffer.readVarInt();
         if (i == 0) {
            p_decode_3_.add(packetbuffer.readBytes(packetbuffer.readableBytes()));
         } else {
            if (i < this.threshold) {
               throw new DecoderException("Badly compressed packet - size of " + i + " is below server threshold of " + this.threshold);
            }

            if (i > 16777216) {
               throw new DecoderException("Badly compressed packet - size of " + i + " is larger than protocol maximum of " + 16777216);
            }

            byte[] abyte = new byte[packetbuffer.readableBytes()];
            packetbuffer.readBytes(abyte);
            this.inflater.setInput(abyte);
            byte[] abyte1 = new byte[i];
            this.inflater.inflate(abyte1);
            p_decode_3_.add(Unpooled.wrappedBuffer(abyte1));
            this.inflater.reset();
         }

      }
   }

   public void setThreshold(int p_179303_1_) {
      this.threshold = p_179303_1_;
   }
}