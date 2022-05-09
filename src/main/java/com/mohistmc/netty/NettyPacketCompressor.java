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