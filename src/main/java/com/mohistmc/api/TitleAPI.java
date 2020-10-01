package com.mohistmc.api;

import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketTitle.Type;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TitleAPI {

    public static void sendPlayerActionbar(Player p, String text) {
        ITextComponent icbc = ITextComponent.Serializer.fromJsonLenient("{\"text\": \"" + text + "\"}");
        SPacketChat ppoc = new SPacketChat(icbc, ChatType.GAME_INFO);
        ((CraftPlayer) p).getHandle().connection.sendPacket(ppoc);
    }

    public static void sendPlayerTitle(Player player, int fadeIn, int stay, int fadeOut, String title) {
        NetHandlerPlayServer connection = ((CraftPlayer) player).getHandle().connection;
        SPacketTitle packetPlayOutTimes = new SPacketTitle(Type.TIMES, null, fadeIn, stay, fadeOut);
        connection.sendPacket(packetPlayOutTimes);

        ITextComponent titleMain = ITextComponent.Serializer.fromJsonLenient("{\"text\": \"" + title + "\"}");
        SPacketTitle SPacketTitle = new SPacketTitle(Type.TIMES, titleMain);
        connection.sendPacket(SPacketTitle);
    }

    public static void sendPlayerSubTitle(Player player, int fadeIn, int stay, int fadeOut, String subTitle) {
        NetHandlerPlayServer connection = ((CraftPlayer) player).getHandle().connection;
        SPacketTitle packetPlayOutTimes = new SPacketTitle(Type.TIMES, null, fadeIn, stay, fadeOut);
        connection.sendPacket(packetPlayOutTimes);
        ITextComponent titleSub = ITextComponent.Serializer.fromJsonLenient("{\"text\": \"" + subTitle + "\"}");
        SPacketTitle packetPlayOutSubTitle = new SPacketTitle(Type.SUBTITLE, titleSub);
        connection.sendPacket(packetPlayOutSubTitle);

        ITextComponent titleMain = ITextComponent.Serializer.fromJsonLenient("{\"text\": \"\"}");
        SPacketTitle SPacketTitle = new SPacketTitle(Type.TITLE, titleMain);
        connection.sendPacket(SPacketTitle);
    }

    public static void sendFullTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
        NetHandlerPlayServer connection = ((CraftPlayer) player).getHandle().connection;
        SPacketTitle packetPlayOutTimes = new SPacketTitle(Type.TIMES, null, fadeIn, stay, fadeOut);
        connection.sendPacket(packetPlayOutTimes);
        ITextComponent titleSub = ITextComponent.Serializer.fromJsonLenient("{\"text\": \"" + subtitle + "\"}");
        SPacketTitle packetPlayOutSubTitle = new SPacketTitle(Type.SUBTITLE, titleSub);
        connection.sendPacket(packetPlayOutSubTitle);
        ITextComponent titleMain = ITextComponent.Serializer.fromJsonLenient("{\"text\": \"" + title + "\"}");
        SPacketTitle SPacketTitle = new SPacketTitle(Type.TITLE, titleMain);
        connection.sendPacket(SPacketTitle);
    }
}
