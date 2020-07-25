package red.mohist.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerAPI {

    public static Map<PlayerEntity, Integer> mods = new ConcurrentHashMap<>();
    public static Map<PlayerEntity, String> modlist = new ConcurrentHashMap<>();

    /**
     *  Get Player ping
     *
     * @param player org.bukkit.entity.player
     */
    public static String getPing(Player player) {
        return String.valueOf(getNMSPlayer(player).ping);
    }

    public static ServerPlayerEntity getNMSPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    public static Player getCBPlayer(PlayerEntity player) {
        return player.getBukkitEntity().getPlayer();
    }

    // Don't count the default number of mods
    public static int getModSize(Player player) {
        return mods.get(getNMSPlayer(player)) == null ? 0 : mods.get(getNMSPlayer(player)) - 4;
    }

    public static String getModlist(Player player) {
        return modlist.get(getNMSPlayer(player)) == null ? "null" : modlist.get(getNMSPlayer(player));
    }

    public static Boolean hasMod(Player player, String modid){
        return getModlist(player).contains(modid);
    }

    public static boolean isOp(PlayerEntity ep)
    {
        return MinecraftServer.getServer().getPlayerList().canSendCommands(ep.getGameProfile());
    }
}
