package org.bukkit.craftbukkit.v1_15_R1.inventory;

import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class CraftEntityEquipment implements EntityEquipment {

    private final CraftLivingEntity entity;

    public CraftEntityEquipment(CraftLivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public ItemStack getItemInMainHand() {
        return getEquipment(EquipmentSlotType.MAINHAND);
    }

    @Override
    public void setItemInMainHand(ItemStack item) {
        setEquipment(EquipmentSlotType.MAINHAND, item);
    }

    @Override
    public ItemStack getItemInOffHand() {
        return getEquipment(EquipmentSlotType.OFFHAND);
    }

    @Override
    public void setItemInOffHand(ItemStack item) {
        setEquipment(EquipmentSlotType.OFFHAND, item);
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
        return getEquipment(EquipmentSlotType.HEAD);
    }

    @Override
    public void setHelmet(ItemStack helmet) {
        setEquipment(EquipmentSlotType.HEAD, helmet);
    }

    @Override
    public ItemStack getChestplate() {
        return getEquipment(EquipmentSlotType.CHEST);
    }

    @Override
    public void setChestplate(ItemStack chestplate) {
        setEquipment(EquipmentSlotType.CHEST, chestplate);
    }

    @Override
    public ItemStack getLeggings() {
        return getEquipment(EquipmentSlotType.LEGS);
    }

    @Override
    public void setLeggings(ItemStack leggings) {
        setEquipment(EquipmentSlotType.LEGS, leggings);
    }

    @Override
    public ItemStack getBoots() {
        return getEquipment(EquipmentSlotType.FEET);
    }

    @Override
    public void setBoots(ItemStack boots) {
        setEquipment(EquipmentSlotType.FEET, boots);
    }

    @Override
    public ItemStack[] getArmorContents() {
        ItemStack[] armor = new ItemStack[]{
                getEquipment(EquipmentSlotType.FEET),
                getEquipment(EquipmentSlotType.LEGS),
                getEquipment(EquipmentSlotType.CHEST),
                getEquipment(EquipmentSlotType.HEAD),
        };
        return armor;
    }

    @Override
    public void setArmorContents(ItemStack[] items) {
        setEquipment(EquipmentSlotType.FEET, items.length >= 1 ? items[0] : null);
        setEquipment(EquipmentSlotType.LEGS, items.length >= 2 ? items[1] : null);
        setEquipment(EquipmentSlotType.CHEST, items.length >= 3 ? items[2] : null);
        setEquipment(EquipmentSlotType.HEAD, items.length >= 4 ? items[3] : null);
    }

    private ItemStack getEquipment(EquipmentSlotType slot) {
        return CraftItemStack.asBukkitCopy(entity.getHandle().getItemStackFromSlot(slot));
    }

    private void setEquipment(EquipmentSlotType slot, ItemStack stack) {
        entity.getHandle().setItemStackToSlot(slot, CraftItemStack.asNMSCopy(stack));
    }

    @Override
    public void clear() {
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
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
       return getDropChance(EquipmentSlotType.MAINHAND);
    }

    @Override
    public void setItemInMainHandDropChance(float chance) {
        setDropChance(EquipmentSlotType.MAINHAND, chance);
    }

    @Override
    public float getItemInOffHandDropChance() {
        return getDropChance(EquipmentSlotType.OFFHAND);
    }

    @Override
    public void setItemInOffHandDropChance(float chance) {
        setDropChance(EquipmentSlotType.OFFHAND, chance);
    }

    @Override
    public float getHelmetDropChance() {
        return getDropChance(EquipmentSlotType.HEAD);
    }

    @Override
    public void setHelmetDropChance(float chance) {
        setDropChance(EquipmentSlotType.HEAD, chance);
    }

    @Override
    public float getChestplateDropChance() {
        return getDropChance(EquipmentSlotType.CHEST);
    }

    @Override
    public void setChestplateDropChance(float chance) {
        setDropChance(EquipmentSlotType.CHEST, chance);
    }

    @Override
    public float getLeggingsDropChance() {
        return getDropChance(EquipmentSlotType.LEGS);
    }

    @Override
    public void setLeggingsDropChance(float chance) {
        setDropChance(EquipmentSlotType.LEGS, chance);
    }

    @Override
    public float getBootsDropChance() {
        return getDropChance(EquipmentSlotType.FEET);
    }

    @Override
    public void setBootsDropChance(float chance) {
        setDropChance(EquipmentSlotType.FEET, chance);
    }

    private void setDropChance(EquipmentSlotType slot, float chance) {
        if (slot == EquipmentSlotType.MAINHAND || slot == EquipmentSlotType.OFFHAND) {
            ((MobEntity) entity.getHandle()).inventoryHandsDropChances[slot.getIndex()] = chance;
        } else {
            ((MobEntity) entity.getHandle()).inventoryArmorDropChances[slot.getIndex()] = chance;
        }
    }

    private float getDropChance(EquipmentSlotType slot) {
        if (slot == EquipmentSlotType.MAINHAND || slot == EquipmentSlotType.OFFHAND) {
            return ((MobEntity) entity.getHandle()).inventoryHandsDropChances[slot.getIndex()];
        } else {
            return ((MobEntity) entity.getHandle()).inventoryArmorDropChances[slot.getIndex()];
        }
    }
}
