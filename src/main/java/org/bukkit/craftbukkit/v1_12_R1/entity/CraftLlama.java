package org.bukkit.craftbukkit.v1_12_R1.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.passive.EntityLlama;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryLlama;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.inventory.LlamaInventory;

public class CraftLlama extends CraftChestedHorse implements Llama {

    public CraftLlama(CraftServer server, EntityLlama entity) {
        super(server, entity);
    }

    @Override
    public EntityLlama getHandle() {
        return (EntityLlama) super.getHandle();
    }

    @Override
    public Color getColor() {
        return Color.values()[getHandle().getVariant()];
    }

    @Override
    public void setColor(Color color) {
        Preconditions.checkArgument(color != null, "color");

        getHandle().setVariant(color.ordinal());
    }

    @Override
    public LlamaInventory getInventory() {
        return new CraftInventoryLlama(getHandle().horseChest);
    }

    @Override
    public int getStrength() {
        return getHandle().getStrength();
    }

    @Override
    public void setStrength(int strength) {
        Preconditions.checkArgument(1 <= strength && strength <= 5, "strength must be [1,5]");
        if (strength == getStrength()) {
            return;
        }
        getHandle().setStrength(strength);
        getHandle().initHorseChest();
    }

    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.LLAMA;
    }

    @Override
    public String toString() {
        return "CraftLlama";
    }

    @Override
    public EntityType getType() {
        return EntityType.LLAMA;
    }

    // Purpur start
    @Override
    public boolean shouldJoinCaravan() {
        return getHandle().shouldJoinCaravan;
    }

    @Override
    public void setShouldJoinCaravan(boolean shouldJoinCaravan) {
        getHandle().shouldJoinCaravan = shouldJoinCaravan;
    }

    @Override
    public boolean inCaravan() {
        return getHandle().inCaravan();
    }

    @Override
    public void joinCaravan(Llama llama) {
        if (llama != null) {
            getHandle().joinCaravan(((CraftLlama) llama).getHandle());
        }
    }

    @Override
    public void leaveCaravan() {
        getHandle().leaveCaravan();
    }

    @Override
    public boolean hasCaravanTail() {
        return getHandle().hasCaravanTail();
    }

    @Override
    public Llama getCaravanHead() {
        return getHandle().getCaravanHead() == null ? null : (Llama) getHandle().getCaravanHead().getBukkitEntity();
    }

    @Override
    public Llama getCaravanTail() {
        return getHandle().getCaravanTail() == null ? null : (Llama) getHandle().getCaravanTail().getBukkitEntity();
    }
    // Purpur end
}
