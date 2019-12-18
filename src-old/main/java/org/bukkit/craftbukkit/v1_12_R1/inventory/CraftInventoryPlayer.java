package org.bukkit.craftbukkit.v1_12_R1.inventory;

import com.google.common.base.Preconditions;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketSetSlot;
import org.apache.commons.lang3.Validate;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryPlayer extends CraftInventory implements org.bukkit.inventory.PlayerInventory, EntityEquipment {
    public CraftInventoryPlayer(InventoryPlayer inventory) {
        super(inventory);
    }

    @Override
    public InventoryPlayer getInventory() {
        return (InventoryPlayer) inventory;
    }

    @Override
    public ItemStack[] getStorageContents() {
        return asCraftMirror(getInventory().mainInventory);
    }

    @Override
    public void setStorageContents(ItemStack[] items) throws IllegalArgumentException {
        setSlots(items, 0, getInventory().mainInventory.size());
    }

    @Override
    public ItemStack getItemInMainHand() {
        return CraftItemStack.asCraftMirror(getInventory().getCurrentItem());
    }

    @Override
    public void setItemInMainHand(ItemStack item) {
        setItem(getHeldItemSlot(), item);
    }

    @Override
    public ItemStack getItemInOffHand() {
        return CraftItemStack.asCraftMirror(getInventory().offHandInventory.get(0));
    }

    @Override
    public void setItemInOffHand(ItemStack item) {
        ItemStack[] extra = getExtraContents();
        extra[0] = item;
        setExtraContents(extra);
    }

    @Override
    public ItemStack getItemInHand() {
        return getItemInMainHand();
    }

    @Override
    public void setItemInHand(ItemStack stack) {
        setItemInMainHand(stack);
    }

    @Override
    public void setItem(int index, ItemStack item) {
        super.setItem(index, item);
        if (this.getHolder() == null) {
            return;
        }
        EntityPlayerMP player = ((CraftPlayer) this.getHolder()).getHandle();
        if (player.connection == null) {
            return;
        }
        if (index < InventoryPlayer.getHotbarSize()) {
            index += 36;
        } else if (index > 39) {
            index += 5; // Off hand
        } else if (index > 35) {
            index = 8 - (index - 36);
        }
        player.connection.sendPacket(new SPacketSetSlot(player.inventoryContainer.windowId, index, CraftItemStack.asNMSCopy(item)));
    }

    public int getHeldItemSlot() {
        return getInventory().currentItem;
    }

    public void setHeldItemSlot(int slot) {
        Validate.isTrue(slot >= 0 && slot < InventoryPlayer.getHotbarSize(), "Slot is not between 0 and 8 inclusive");
        this.getInventory().currentItem = slot;
        ((CraftPlayer) this.getHolder()).getHandle().connection.sendPacket(new SPacketHeldItemChange(slot));
    }

    public ItemStack getHelmet() {
        return getItem(getSize() - 2);
    }

    public void setHelmet(ItemStack helmet) {
        setItem(getSize() - 2, helmet);
    }

    public ItemStack getChestplate() {
        return getItem(getSize() - 3);
    }

    public void setChestplate(ItemStack chestplate) {
        setItem(getSize() - 3, chestplate);
    }

    public ItemStack getLeggings() {
        return getItem(getSize() - 4);
    }

    public void setLeggings(ItemStack leggings) {
        setItem(getSize() - 4, leggings);
    }

    public ItemStack getBoots() {
        return getItem(getSize() - 5);
    }

    public void setBoots(ItemStack boots) {
        setItem(getSize() - 5, boots);
    }

    public ItemStack[] getArmorContents() {
        return asCraftMirror(getInventory().armorInventory);
    }

    @Override
    public void setArmorContents(ItemStack[] items) {
        setSlots(items, getInventory().mainInventory.size(), getInventory().armorInventory.size());
    }

    private void setSlots(ItemStack[] items, int baseSlot, int length) {
        if (items == null) {
            items = new ItemStack[length];
        }
        Preconditions.checkArgument(items.length <= length, "items.length must be < %s", length);

        for (int i = 0; i < length; i++) {
            if (i >= items.length) {
                setItem(baseSlot + i, null);
            } else {
                setItem(baseSlot + i, items[i]);
            }
        }
    }

    @Override
    public ItemStack[] getExtraContents() {
        return asCraftMirror(getInventory().offHandInventory);
    }

    @Override
    public void setExtraContents(ItemStack[] items) {
        setSlots(items, getInventory().mainInventory.size() + getInventory().armorInventory.size(), getInventory().offHandInventory.size());
    }

    public int clear(int id, int data) {
        int count = 0;
        ItemStack[] items = getContents();

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            if (item == null) {
                continue;
            }
            if (id > -1 && item.getTypeId() != id) {
                continue;
            }
            if (data > -1 && item.getData().getData() != data) {
                continue;
            }

            count += item.getAmount();
            setItem(i, null);
        }

        return count;
    }

    @Override
    public HumanEntity getHolder() {
        return (HumanEntity) inventory.getOwner();
    }

    @Override
    public float getItemInHandDropChance() {
        return getItemInMainHandDropChance();
    }

    @Override
    public void setItemInHandDropChance(float chance) {
        setItemInMainHandDropChance(chance);
    }

    @Override
    public float getItemInMainHandDropChance() {
        return 1;
    }

    @Override
    public void setItemInMainHandDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getItemInOffHandDropChance() {
        return 1;
    }

    @Override
    public void setItemInOffHandDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    public float getHelmetDropChance() {
        return 1;
    }

    public void setHelmetDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    public float getChestplateDropChance() {
        return 1;
    }

    public void setChestplateDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    public float getLeggingsDropChance() {
        return 1;
    }

    public void setLeggingsDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    public float getBootsDropChance() {
        return 1;
    }

    public void setBootsDropChance(float chance) {
        throw new UnsupportedOperationException();
    }
}
