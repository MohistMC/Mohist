package org.bukkit.craftbukkit.v1_16_R1.entity;

import java.util.Collection;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R1.CraftServer;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import net.minecraft.entity.player.PlayerEntity;

public class CraftHumanEntity extends CraftLivingEntity implements HumanEntity {

    private CraftInventoryPlayer inventory;
    protected GameMode gm;
    protected final PermissibleBase perm = new PermissibleBase(this);
    private boolean op;

    public CraftHumanEntity(PlayerEntity entity) {
        super(entity);
        this.nms = entity;
        this.gm = CraftServer.INSTANCE.getDefaultGameMode();
        this.inventory = new CraftInventoryPlayer(entity.inventory);
    }

    @Override
    public void closeInventory() {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean discoverRecipe(NamespacedKey arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int discoverRecipes(Collection<NamespacedKey> arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getAttackCooldown() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Location getBedLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getCooldown(Material arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Inventory getEnderChest() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getExpToLevel() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public GameMode getGameMode() {
        return gm;
    }

    @Override
    public PlayerInventory getInventory() {
        return inventory;
    }

    @Override
    public ItemStack getItemInHand() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ItemStack getItemOnCursor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MainHand getMainHand() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InventoryView getOpenInventory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public  Entity getShoulderEntityLeft() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public  Entity getShoulderEntityRight() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getSleepTicks() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean hasCooldown(Material arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isBlocking() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isHandRaised() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public  InventoryView openEnchanting( Location arg0, boolean arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public  InventoryView openInventory(Inventory arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void openInventory(InventoryView arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public  InventoryView openMerchant(Villager arg0, boolean arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public  InventoryView openMerchant(Merchant arg0, boolean arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public  InventoryView openWorkbench( Location arg0, boolean arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCooldown(Material arg0, int arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setGameMode(GameMode arg0) {
        this.gm = arg0;
    }

    @Override
    public void setItemInHand( ItemStack arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setItemOnCursor( ItemStack arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setShoulderEntityLeft( Entity arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setShoulderEntityRight( Entity arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean setWindowProperty(Property arg0, int arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean sleep(Location arg0, boolean arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean undiscoverRecipe(NamespacedKey arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int undiscoverRecipes(Collection<NamespacedKey> arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void wakeup(boolean arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isOp() {
        return op;
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
    public void setOp(boolean value) {
        this.op = value;
        perm.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return perm.getEffectivePermissions();
    }

    @Override
    public Set<NamespacedKey> getDiscoveredRecipes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasDiscoveredRecipe(NamespacedKey arg0) {
        // TODO Auto-generated method stub
        return false;
    }

}