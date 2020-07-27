package org.bukkit.command;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Set;

/**
 * For when all you care about is just messaging
 */
public interface MessageCommandSender extends CommandSender {

    @Override
    default void sendMessage(String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    @Override
    default Server getServer() {
        return Bukkit.getServer();
    }

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
    default boolean isPermissionSet(String name) {
        throw new NotImplementedException();
    }

    @Override
    default boolean isPermissionSet(Permission perm) {
        throw new NotImplementedException();
    }

    @Override
    default boolean hasPermission(String name) {
        throw new NotImplementedException();
    }

    @Override
    default boolean hasPermission(Permission perm) {
        throw new NotImplementedException();
    }

    @Override
    default PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        throw new NotImplementedException();
    }

    @Override
    default PermissionAttachment addAttachment(Plugin plugin) {
        throw new NotImplementedException();
    }

    @Override
    default PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        throw new NotImplementedException();
    }

    @Override
    default PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        throw new NotImplementedException();
    }

    @Override
    default void removeAttachment(PermissionAttachment attachment) {
        throw new NotImplementedException();
    }

    @Override
    default void recalculatePermissions() {
        throw new NotImplementedException();
    }

    @Override
    default Set<PermissionAttachmentInfo> getEffectivePermissions() {
        throw new NotImplementedException();
    }

    @Override
    default Spigot spigot() {
        throw new NotImplementedException();
    }

}
