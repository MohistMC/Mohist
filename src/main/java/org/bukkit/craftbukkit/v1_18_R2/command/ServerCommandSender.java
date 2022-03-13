package org.bukkit.craftbukkit.v1_18_R2.command;

import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public abstract class ServerCommandSender implements CommandSender {
    private final PermissibleBase perm = new PermissibleBase(this);

    public ServerCommandSender() {
    }

    @Override
    public boolean isPermissionSet(String name) {
        return perm.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return this.perm.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return perm.hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return this.perm.hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return perm.addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return perm.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return perm.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return perm.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        perm.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        perm.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return perm.getEffectivePermissions();
    }

    public boolean isPlayer() {
        return false;
    }

    @Override
    public Server getServer() {
        return Bukkit.getServer();
    }

    @Override
    public void sendMessage(UUID uuid, String message) {
        this.sendMessage(message); // ServerCommandSenders have no use for senders
    }

    @Override
    public void sendMessage(UUID uuid, String... messages) {
        this.sendMessage(messages); // ServerCommandSenders have no use for senders
    }

    // Spigot start
    private final Spigot spigot = new Spigot()
    {

        @Override
        public void sendMessage(net.md_5.bungee.api.chat.BaseComponent component)
        {
            ServerCommandSender.this.sendMessage(net.md_5.bungee.api.chat.TextComponent.toLegacyText(component));
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.chat.BaseComponent... components)
        {
            ServerCommandSender.this.sendMessage(net.md_5.bungee.api.chat.TextComponent.toLegacyText(components));
        }

        @Override
        public void sendMessage(UUID sender, net.md_5.bungee.api.chat.BaseComponent... components)
        {
            this.sendMessage(components);
        }

        @Override
        public void sendMessage(UUID sender, net.md_5.bungee.api.chat.BaseComponent component)
        {
            this.sendMessage(component);
        }

    };

    @Override
    public Spigot spigot()
    {
        return spigot;
    }
    // Spigot end
}
