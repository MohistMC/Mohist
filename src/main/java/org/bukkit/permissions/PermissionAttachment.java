package org.bukkit.permissions;

import org.bukkit.plugin.Plugin;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Holds information about a permission attachment on a {@link Permissible}
 * object
 */
public class PermissionAttachment {
    private final Map<String, Boolean> permissions = new LinkedHashMap<>();
    private final Permissible permissible;
    private final Plugin plugin;
    private PermissionRemovedExecutor removed;

    public PermissionAttachment(Plugin plugin, Permissible Permissible) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        } else if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
        }

        this.permissible = Permissible;
        this.plugin = plugin;
    }

    /**
     * Gets the plugin responsible for this attachment
     *
     * @return Plugin responsible for this permission attachment
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Gets the class that was previously set to be called when this
     * attachment was removed from a {@link Permissible}. May be null.
     *
     * @return Object to be called when this is removed
     */
    public PermissionRemovedExecutor getRemovalCallback() {
        return removed;
    }

    /**
     * Sets an object to be called for when this attachment is removed from a
     * {@link Permissible}. May be null.
     *
     * @param ex Object to be called when this is removed
     */
    public void setRemovalCallback(PermissionRemovedExecutor ex) {
        removed = ex;
    }

    /**
     * Gets the Permissible that this is attached to
     *
     * @return Permissible containing this attachment
     */
    public Permissible getPermissible() {
        return permissible;
    }

    /**
     * Gets a copy of all set permissions and values contained within this
     * attachment.
     * <p>
     * This map may be modified but will not affect the attachment, as it is a
     * copy.
     *
     * @return Copy of all permissions and values expressed by this attachment
     */
    public Map<String, Boolean> getPermissions() {
        return new LinkedHashMap<>(permissions);
    }

    /**
     * Sets a permission to the given value, by its fully qualified name
     *
     * @param name  Name of the permission
     * @param value New value of the permission
     */
    public void setPermission(String name, boolean value) {
        permissions.put(name.toLowerCase(java.util.Locale.ENGLISH), value);
        permissible.recalculatePermissions();
    }

    /**
     * Sets a permission to the given value
     *
     * @param perm  Permission to set
     * @param value New value of the permission
     */
    public void setPermission(Permission perm, boolean value) {
        setPermission(perm.getName(), value);
    }

    /**
     * Removes the specified permission from this attachment.
     * <p>
     * If the permission does not exist in this attachment, nothing will
     * happen.
     *
     * @param name Name of the permission to remove
     */
    public void unsetPermission(String name) {
        permissions.remove(name.toLowerCase(java.util.Locale.ENGLISH));
        permissible.recalculatePermissions();
    }

    /**
     * Removes the specified permission from this attachment.
     * <p>
     * If the permission does not exist in this attachment, nothing will
     * happen.
     *
     * @param perm Permission to remove
     */
    public void unsetPermission(Permission perm) {
        unsetPermission(perm.getName());
    }

    /**
     * Removes this attachment from its registered {@link Permissible}
     *
     * @return true if the permissible was removed successfully, false if it
     * did not exist
     */
    public boolean remove() {
        try {
            permissible.removeAttachment(this);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
