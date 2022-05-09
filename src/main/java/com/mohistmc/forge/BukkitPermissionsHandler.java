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
