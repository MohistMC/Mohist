package red.mohist.forge;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayerMP;
import red.mohist.configuration.MohistConfig;

public class MohistForgeUtils {

    public static boolean modsblacklist(EntityPlayerMP epmp, String modslist) {

        for (String m : MohistConfig.instance.getStringList("forge.modsblacklist.list", ImmutableList.of("aaaa", "bbbb"))){
            if (MohistConfig.instance.modsblacklist.getValue().contains(m) && modslist.contains(m)) {
                return true;
            }
        }
        return false;
    }
}
