package red.mohist.bukkit;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import net.minecraftforge.server.permission.DefaultPermissionHandler;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.IPermissionHandler;
import net.minecraftforge.server.permission.context.IContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import red.mohist.forge.MohistMod;

import javax.annotation.Nullable;
import java.util.*;

public class BukkitPermissionsHandler implements IPermissionHandler {

    private final Map<String, String> registeredNodes = new HashMap<>();

    private static IPermissionHandler permissionHandler = DefaultPermissionHandler.INSTANCE;

    @Override
    public void registerNode(String node, DefaultPermissionLevel level, String desc) {
        Preconditions.checkNotNull(node, "Permission node can't be null!");
        Preconditions.checkNotNull(level, "Permission level can't be null!");
        Preconditions.checkNotNull(desc, "Permission description can't be null!");
        Preconditions.checkArgument(!node.isEmpty(), "Permission node can't be empty!");
        // TODO Loader states Preconditions.checkState(Loader.instance().getLoaderState().ordinal() > LoaderState.PREINITIALIZATION.ordinal(), "Can't register permission nodes before Init!");
        permissionHandler.registerNode(node, level, desc);
        registeredNodes.put(node, desc);
    }

    public static void setPermissionHandler(IPermissionHandler handler)
    {
        Preconditions.checkNotNull(handler, "Permission handler can't be null!");
        // TODO Loader states Preconditions.checkState(Loader.instance().getLoaderState().ordinal() <= LoaderState.PREINITIALIZATION.ordinal(), "Can't register after IPermissionHandler PreInit!");
        MohistMod.LOGGER.warn("Replacing {} with {}", permissionHandler.getClass().getName(), handler.getClass().getName());
        permissionHandler = handler;
    }


    @Override
    public Collection<String> getRegisteredNodes() {
        return registeredNodes.keySet();
    }

    @Override
    public boolean hasPermission(GameProfile profile, String node, @Nullable IContext context) {
        Preconditions.checkNotNull(profile, "GameProfile can't be null!");
        Preconditions.checkNotNull(node, "Permission node can't be null!");
        Preconditions.checkArgument(!node.isEmpty(), "Permission node can't be empty!");
        Player player = Bukkit.getServer().getPlayer(profile.getId());

        return player != null && player.hasPermission(node);
    }

    @Override
    public String getNodeDescription(String node) {
        return registeredNodes.getOrDefault(node, "No Description Set");
    }
}
