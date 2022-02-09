package com.mohistmc.forge;

import com.mohistmc.api.PlayerAPI;
import com.mojang.authlib.GameProfile;
import java.util.Collection;
import java.util.stream.Collectors;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.IPermissionHandler;
import net.minecraftforge.server.permission.context.IContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

/**
 * https://github.com/CJ-MC-Mods/ForgeToBukkitPermissions
 */
public class BukkitPermissionsHandler implements IPermissionHandler {

    @Override
    public void registerNode(@NotNull String node, @NotNull DefaultPermissionLevel level, @NotNull String desc) {
        ForgeInjectBukkit.registerDefaultPermission(node, level, desc);
    }

    @Override
    public @NotNull Collection<String> getRegisteredNodes() {
        return Bukkit.getPluginManager().getPermissions().stream().map(Permission::getName).collect(Collectors.toList());
    }

    @Override
    public boolean hasPermission(@NotNull GameProfile profile, @NotNull String node, @NotNull IContext context) {
        if (context != null) {
            PlayerEntity player = context.getPlayer();
            if (player != null) {
                return player.getBukkitEntity().hasPermission(node);
            }
        }
        Player player = Bukkit.getPlayer(profile.getId());
        if (player != null) {
            return player.hasPermission(node);
        } else {
            Permission permission = Bukkit.getPluginManager().getPermission(node);
            boolean op = PlayerAPI.isOp(profile);
            if (permission != null) {
                return permission.getDefault().getValue(op);
            } else {
                return Permission.DEFAULT_PERMISSION.getValue(op);
            }
        }
    }

    @Override
    public @NotNull String getNodeDescription(@NotNull String desc) {
        Permission permission = Bukkit.getPluginManager().getPermission(desc);
        return permission !=null ? permission.getDescription() : "No Description Set";
    }
}
