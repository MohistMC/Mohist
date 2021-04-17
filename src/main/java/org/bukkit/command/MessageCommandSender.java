package org.bukkit.command;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * For when all you care about is just messaging
 */
public interface MessageCommandSender extends CommandSender {

    @Override
    default void sendMessage(@NotNull String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    @Override
    default void sendMessage(@Nullable UUID sender, @NotNull String message) {
        sendMessage(message);
    }

    @Override
    default void sendMessage(@Nullable UUID sender, @NotNull String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    @NotNull
    @Override
    default Server getServer() {
        return Bukkit.getServer();
    }

    @NotNull
    @Override
    default String getName() {
        throw new NotImplementedException();
    }

    @Override
    default boolean isOp() {
        throw new NotImplementedException();
    }

    @Override
    default void setOp(boolean value) {
        throw new NotImplementedException();
    }

    @Override
    default boolean isPermissionSet(@NotNull String name) {
        throw new NotImplementedException();
    }

    @Override
    default boolean isPermissionSet(@NotNull Permission perm) {
        throw new NotImplementedException();
    }

    @Override
    default boolean hasPermission(@NotNull String name) {
        throw new NotImplementedException();
    }

    @Override
    default boolean hasPermission(@NotNull Permission perm) {
        throw new NotImplementedException();
    }

    @NotNull
    @Override
    default PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value) {
        throw new NotImplementedException();
    }

    @NotNull
    @Override
    default PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        throw new NotImplementedException();
    }

    @NotNull
    @Override
    default PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value, int ticks) {
        throw new NotImplementedException();
    }

    @NotNull
    @Override
    default PermissionAttachment addAttachment(@NotNull Plugin plugin, int ticks) {
        throw new NotImplementedException();
    }

    @Override
    default void removeAttachment(@NotNull PermissionAttachment attachment) {
        throw new NotImplementedException();
    }

    @Override
    default void recalculatePermissions() {
        throw new NotImplementedException();
    }

    @NotNull
    @Override
    default Set<PermissionAttachmentInfo> getEffectivePermissions() {
        throw new NotImplementedException();
    }

    @NotNull
    @Override
    default Spigot spigot() {
        throw new NotImplementedException();
    }

}
