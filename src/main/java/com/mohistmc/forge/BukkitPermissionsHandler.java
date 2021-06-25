package com.mohistmc.forge;

import com.mohistmc.api.ServerAPI;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.IPermissionHandler;
import net.minecraftforge.server.permission.context.IContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

/**
 * https://github.com/CJ-MC-Mods/ForgeToBukkitPermissions
 */
public class BukkitPermissionsHandler implements IPermissionHandler {

    @Override
    public void registerNode(String node, DefaultPermissionLevel level, String desc) {
        ForgeInjectBukkit.registerDefaultPermission(node, level, desc);
    }

    @Override
    public Collection<String> getRegisteredNodes() {
        List<String> list = new ArrayList<>();
        for (Permission permission : Bukkit.getPluginManager().getPermissions()) {
            String name = permission.getName();
            list.add(name);
        }
        return list;
    }

    @Override
    public boolean hasPermission(GameProfile profile, String node, @Nullable IContext context) {
        if (context != null) {
            EntityPlayer player = context.getPlayer();
            if (player != null) {
                return player.getBukkitEntity().hasPermission(node);
            }

        }
        Player player = Bukkit.getServer().getPlayer(profile.getId());
        if (player != null) {
            return player.hasPermission(node);
        } else {
            Permission perm = Bukkit.getServer().getPluginManager().getPermission(node);
            boolean isOp = ServerAPI.getNMSServer().getPlayerList().canSendCommands(profile);
            if (perm != null) {
                return perm.getDefault().getValue(isOp);
            } else {
                return Permission.DEFAULT_PERMISSION.getValue(isOp);
            }
        }
    }

    @Override
    public String getNodeDescription(String node) {
        Permission permission = Bukkit.getPluginManager().getPermission(node);
        return permission == null ? "" : permission.getDescription();
    }
}
