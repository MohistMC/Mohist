/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2024.
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

import java.util.Set;
import java.util.UUID;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.permission.handler.IPermissionHandler;
import net.minecraftforge.server.permission.nodes.PermissionDynamicContext;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;
import org.bukkit.Bukkit;

public class BukkitPermissionsHandler implements IPermissionHandler {

    private final IPermissionHandler delegate;

    public BukkitPermissionsHandler(IPermissionHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public ResourceLocation getIdentifier() {
        return new ResourceLocation("mohist", "permission");
    }

    @Override
    public Set<PermissionNode<?>> getRegisteredNodes() {
        return delegate.getRegisteredNodes();
    }

    @Override
    public <T> T getPermission(ServerPlayer player, PermissionNode<T> node, PermissionDynamicContext<?>... context) {
        if (node.getType() == PermissionTypes.BOOLEAN) {
            return (T) (Object) player.getBukkitEntity().hasPermission(node.getNodeName());
        } else {
            return delegate.getPermission(player, node, context);
        }
    }

    @Override
    public <T> T getOfflinePermission(UUID uuid, PermissionNode<T> node, PermissionDynamicContext<?>... context) {
        var player = Bukkit.getPlayer(uuid);
        if (player != null && node.getType() == PermissionTypes.BOOLEAN) {
            return (T) (Object) player.hasPermission(node.getNodeName());
        } else {
            return delegate.getOfflinePermission(uuid, node, context);
        }
    }
}
