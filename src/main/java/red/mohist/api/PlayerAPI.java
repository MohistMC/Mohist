package red.mohist.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.player.EntityPlayerMP;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerAPI {

    public static Map<EntityPlayerMP, Integer> mods = new ConcurrentHashMap<EntityPlayerMP, Integer>();
    public static Map<EntityPlayerMP, String> modlist = new ConcurrentHashMap<EntityPlayerMP, String>();

    /**
     *  Get Player ping
     *
     * @param player org.bukkit.entity.player
     */
    public static String getPing(Player player) {
        int ping = getNMSPlayer(player).ping;
        return String.valueOf(ping);
    }

    public static EntityPlayerMP getNMSPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    public static Player getCBPlayer(EntityPlayerMP player) {
        return player.getBukkitEntity().getPlayer();
    }

    public static int getModSize(Player player) {
        return mods.get(getNMSPlayer(player)) == null ? 0 : mods.get(getNMSPlayer(player)) - 4;
    }

    public static String getModlist(Player player) {
        return modlist.get(getNMSPlayer(player)) == null ? "null" : modlist.get(getNMSPlayer(player));
    }

    public static Boolean hasMod(Player player, String modid){
        return getModlist(player).contains(modid);
    }
}
