package org.bukkit.craftbukkit.v1_12_R1.command;

import net.minecraft.command.ICommandSender;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class ProxiedNativeCommandSender implements ProxiedCommandSender {

    private final ICommandSender orig;
    private final CommandSender caller;
    private final CommandSender callee;

    public ProxiedNativeCommandSender(ICommandSender orig, CommandSender caller, CommandSender callee) {
        this.orig = orig;
        this.caller = caller;
        this.callee = callee;
    }

    public ICommandSender getHandle() {
        return orig;
    }

    @Override
    public CommandSender getCaller() {
        return caller;
    }

    @Override
    public CommandSender getCallee() {
        return callee;
    }

    @Override
    public void sendMessage(String message) {
        getCaller().sendMessage(message);
    }

    @Override
    public void sendMessage(String[] messages) {
        getCaller().sendMessage(messages);
    }

    @Override
    public Server getServer() {
        return getCallee().getServer();
    }

    @Override
    public String getName() {
        return getCallee().getName();
    }

    @Override
    public boolean isPermissionSet(String name) {
        return getCaller().isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return getCaller().isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return getCaller().hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return getCaller().hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return getCaller().addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return getCaller().addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return getCaller().addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return getCaller().addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        getCaller().removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        getCaller().recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return getCaller().getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return getCaller().isOp();
    }

    @Override
    public void setOp(boolean value) {
        getCaller().setOp(value);
    }

    // Spigot start
    @Override
    public Spigot spigot() {
        return getCaller().spigot();
    }
    // Spigot end
}
