package org.bukkit.craftbukkit.inventory;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;

import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class CraftEntityEquipment implements EntityEquipment {

    private final CraftLivingEntity entity;

    public CraftEntityEquipment(CraftLivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public void setItem(org.bukkit.inventory.EquipmentSlot slot, ItemStack item) {
        this.entity.getHandle().equipStack(CraftEquipmentSlot.getNMS(slot), CraftItemStack.asNMSCopy(item));
    }

    @Override
    public ItemStack getItem(org.bukkit.inventory.EquipmentSlot slot) {
        return CraftItemStack.asBukkitCopy(this.entity.getHandle().getEquippedStack(CraftEquipmentSlot.getNMS(slot)));
    }

    @Override
    public ItemStack getItemInMainHand() {
        return getEquipment(EquipmentSlot.MAINHAND);
    }

    @Override
    public void setItemInMainHand(ItemStack item) {
        setEquipment(EquipmentSlot.MAINHAND, item);
    }

    @Override
    public ItemStack getItemInOffHand() {
        return getEquipment(EquipmentSlot.OFFHAND);
    }

    @Override
    public void setItemInOffHand(ItemStack item) {
        setEquipment(EquipmentSlot.OFFHAND, item);
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
    public ItemStack getHelmet() {
        return getEquipment(EquipmentSlot.HEAD);
    }

    @Override
    public void setHelmet(ItemStack helmet) {
        setEquipment(EquipmentSlot.HEAD, helmet);
    }

    @Override
    public ItemStack getChestplate() {
        return getEquipment(EquipmentSlot.CHEST);
    }

    @Override
    public void setChestplate(ItemStack chestplate) {
        setEquipment(EquipmentSlot.CHEST, chestplate);
    }

    @Override
    public ItemStack getLeggings() {
        return getEquipment(EquipmentSlot.LEGS);
    }

    @Override
    public void setLeggings(ItemStack leggings) {
        setEquipment(EquipmentSlot.LEGS, leggings);
    }

    @Override
    public ItemStack getBoots() {
        return getEquipment(EquipmentSlot.FEET);
    }

    @Override
    public void setBoots(ItemStack boots) {
        setEquipment(EquipmentSlot.FEET, boots);
    }

    @Override
    public ItemStack[] getArmorContents() {
        ItemStack[] armor = new ItemStack[]{
                getEquipment(EquipmentSlot.FEET),
                getEquipment(EquipmentSlot.LEGS),
                getEquipment(EquipmentSlot.CHEST),
                getEquipment(EquipmentSlot.HEAD),
        };
        return armor;
    }

    @Override
    public void setArmorContents(ItemStack[] items) {
        setEquipment(EquipmentSlot.FEET, items.length >= 1 ? items[0] : null);
        setEquipment(EquipmentSlot.LEGS, items.length >= 2 ? items[1] : null);
        setEquipment(EquipmentSlot.CHEST, items.length >= 3 ? items[2] : null);
        setEquipment(EquipmentSlot.HEAD, items.length >= 4 ? items[3] : null);
    }

    private ItemStack getEquipment(EquipmentSlot slot) {
        return CraftItemStack.asBukkitCopy(entity.getHandle().getEquippedStack(slot));
    }

    private void setEquipment(EquipmentSlot slot, ItemStack stack) {
        entity.getHandle().equipStack(slot, CraftItemStack.asNMSCopy(stack));
    }

    @Override
    public void clear() {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            setEquipment(slot, null);
        }
    }

    @Override
    public Entity getHolder() {
        return entity;
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
       return getDropChance(EquipmentSlot.MAINHAND);
    }

    @Override
    public void setItemInMainHandDropChance(float chance) {
        setDropChance(EquipmentSlot.MAINHAND, chance);
    }

    @Override
    public float getItemInOffHandDropChance() {
        return getDropChance(EquipmentSlot.OFFHAND);
    }

    @Override
    public void setItemInOffHandDropChance(float chance) {
        setDropChance(EquipmentSlot.OFFHAND, chance);
    }

    @Override
    public float getHelmetDropChance() {
        return getDropChance(EquipmentSlot.HEAD);
    }

    @Override
    public void setHelmetDropChance(float chance) {
        setDropChance(EquipmentSlot.HEAD, chance);
    }

    @Override
    public float getChestplateDropChance() {
        return getDropChance(EquipmentSlot.CHEST);
    }

    @Override
    public void setChestplateDropChance(float chance) {
        setDropChance(EquipmentSlot.CHEST, chance);
    }

    @Override
    public float getLeggingsDropChance() {
        return getDropChance(EquipmentSlot.LEGS);
    }

    @Override
    public void setLeggingsDropChance(float chance) {
        setDropChance(EquipmentSlot.LEGS, chance);
    }

    @Override
    public float getBootsDropChance() {
        return getDropChance(EquipmentSlot.FEET);
    }

    @Override
    public void setBootsDropChance(float chance) {
        setDropChance(EquipmentSlot.FEET, chance);
    }

    private void setDropChance(EquipmentSlot slot, float chance) {
        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            ((MobEntity) entity.getHandle()).handDropChances[slot.getEntitySlotId()] = chance;
        } else {
            ((MobEntity) entity.getHandle()).armorDropChances[slot.getEntitySlotId()] = chance;
        }
    }

    private float getDropChance(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            return ((MobEntity) entity.getHandle()).handDropChances[slot.getEntitySlotId()];
        } else {
            return ((MobEntity) entity.getHandle()).armorDropChances[slot.getEntitySlotId()];
        }
    }
}
