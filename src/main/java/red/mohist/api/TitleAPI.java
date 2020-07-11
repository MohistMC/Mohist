package red.mohist.api;

import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.server.SChatPacket;
import net.minecraft.network.play.server.STitlePacket.Type;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TitleAPI {

    public static void sendPlayerAbar(Player p, String text) {
        ITextComponent icbc = ITextComponent.Serializer.fromJsonLenient("{\"text\": \"" + text + "\"}");
        SChatPacket ppoc = new SChatPacket(icbc, ChatType.GAME_INFO);
        ((CraftPlayer) p).getHandle().connection.sendPacket(ppoc);
    }

    public static void sendPlayerTitle(Player player, int fadeIn, int stay, int fadeOut, String title) {
        ServerPlayNetHandler connection = ((CraftPlayer) player).getHandle().connection;
        STitlePacket packetPlayOutTimes = new STitlePacket(Type.TIMES, null, fadeIn, stay, fadeOut);
        connection.sendPacket(packetPlayOutTimes);

        ITextComponent titleMain = ITextComponent.Serializer.fromJsonLenient("{\"text\": \"" + title + "\"}");
        STitlePacket SPacketTitle = new STitlePacket(Type.TIMES, titleMain);
        connection.sendPacket(SPacketTitle);
    }

    public static void sendPlayerSubTitle(Player player, int fadeIn, int stay, int fadeOut, String subTitle) {
        ServerPlayNetHandler connection = ((CraftPlayer) player).getHandle().connection;
        STitlePacket packetPlayOutTimes = new STitlePacket(Type.TIMES, null, fadeIn, stay, fadeOut);
        connection.sendPacket(packetPlayOutTimes);
        ITextComponent titleSub = ITextComponent.Serializer.fromJsonLenient("{\"text\": \"" + subTitle + "\"}");
        STitlePacket packetPlayOutSubTitle = new STitlePacket(Type.SUBTITLE, titleSub);
        connection.sendPacket(packetPlayOutSubTitle);

        ITextComponent titleMain = ITextComponent.Serializer.fromJsonLenient("{\"text\": \"\"}");
        STitlePacket SPacketTitle = new STitlePacket(Type.TITLE, titleMain);
        connection.sendPacket(SPacketTitle);
    }

    public static void sendFullTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
        ServerPlayNetHandler connection = ((CraftPlayer) player).getHandle().connection;
        STitlePacket packetPlayOutTimes = new STitlePacket(Type.TIMES, null, fadeIn, stay, fadeOut);
        connection.sendPacket(packetPlayOutTimes);
        ITextComponent titleSub = ITextComponent.Serializer.fromJsonLenient("{\"text\": \"" + subtitle + "\"}");
        STitlePacket packetPlayOutSubTitle = new STitlePacket(Type.SUBTITLE, titleSub);
        connection.sendPacket(packetPlayOutSubTitle);
        ITextComponent titleMain = ITextComponent.Serializer.fromJsonLenient("{\"text\": \"" + title + "\"}");
        STitlePacket SPacketTitle = new STitlePacket(Type.TITLE, titleMain);
        connection.sendPacket(SPacketTitle);
    }
}
