package org.bukkit.craftbukkit.v1_12_R1.command;

import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public abstract class ServerCommandSender implements CommandSender {
    private static PermissibleBase blockPermInst;
    private final PermissibleBase perm;
    // Spigot start
    private final Spigot spigot = new Spigot() {
        @Override
        public void sendMessage(net.md_5.bungee.api.chat.BaseComponent component) {
            ServerCommandSender.this.sendMessage(net.md_5.bungee.api.chat.TextComponent.toLegacyText(component));
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.chat.BaseComponent... components) {
            ServerCommandSender.this.sendMessage(net.md_5.bungee.api.chat.TextComponent.toLegacyText(components));
        }
    };

    public ServerCommandSender() {
        if (this instanceof CraftBlockCommandSender) {
            if (blockPermInst == null) {
                blockPermInst = new PermissibleBase(this);
            }
            this.perm = blockPermInst;
        } else {
            this.perm = new PermissibleBase(this);
        }
    }

    public boolean isPermissionSet(String name) {
        return perm.isPermissionSet(name);
    }

    public boolean isPermissionSet(Permission perm) {
        return this.perm.isPermissionSet(perm);
    }

    public boolean hasPermission(String name) {
        return perm.hasPermission(name);
    }

    public boolean hasPermission(Permission perm) {
        return this.perm.hasPermission(perm);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return perm.addAttachment(plugin, name, value);
    }

    public PermissionAttachment addAttachment(Plugin plugin) {
        return perm.addAttachment(plugin);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return perm.addAttachment(plugin, name, value, ticks);
    }

    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return perm.addAttachment(plugin, ticks);
    }

    public void removeAttachment(PermissionAttachment attachment) {
        perm.removeAttachment(attachment);
    }

    public void recalculatePermissions() {
        perm.recalculatePermissions();
    }

    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return perm.getEffectivePermissions();
    }

    public boolean isPlayer() {
        return false;
    }

    public Server getServer() {
        return Bukkit.getServer();
    }

    @Override
    public Spigot spigot() {
        return spigot;
    }
    // Spigot end
}
