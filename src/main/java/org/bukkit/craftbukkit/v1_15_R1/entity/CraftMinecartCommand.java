package org.bukkit.craftbukkit.v1_15_R1.entity;

import java.util.Set;
import net.minecraft.entity.item.minecart.MinecartCommandBlockEntity;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftChatMessage;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class CraftMinecartCommand extends CraftMinecart implements CommandMinecart {
    private final PermissibleBase perm = new PermissibleBase(this);

    public CraftMinecartCommand(CraftServer server, MinecartCommandBlockEntity entity) {
        super(server, entity);
    }

    @Override
    public MinecartCommandBlockEntity getHandle() {
        return (MinecartCommandBlockEntity) entity;
    }

    @Override
    public String getCommand() {
        return getHandle().getCommandBlockLogic().getCommand();
    }

    @Override
    public void setCommand(String command) {
        getHandle().getCommandBlockLogic().setCommand(command != null ? command : "");
        getHandle().getDataManager().set(MinecartCommandBlockEntity.COMMAND, getHandle().getCommandBlockLogic().getCommand());
    }

    @Override
    public void setName(String name) {
        getHandle().getCommandBlockLogic().setName(CraftChatMessage.fromStringOrNull(name));
    }

    @Override
    public String toString() {
        return "CraftMinecartCommand";
    }

    @Override
    public EntityType getType() {
        return EntityType.MINECART_COMMAND;
    }

    @Override
    public void sendMessage(String message) {
    }

    @Override
    public void sendMessage(String[] messages) {
    }

    @Override
    public String getName() {
        return CraftChatMessage.fromComponent(getHandle().getCommandBlockLogic().getName());
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of a minecart");
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

    @Override
    public Server getServer() {
        return Bukkit.getServer();
    }
}
