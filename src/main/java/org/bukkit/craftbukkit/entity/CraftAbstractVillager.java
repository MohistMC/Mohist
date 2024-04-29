package org.bukkit.craftbukkit.entity;

import java.util.List;
import net.minecraft.world.entity.npc.Villager;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftMerchant;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.MerchantRecipe;

public class CraftAbstractVillager extends CraftAgeable implements AbstractVillager, InventoryHolder {

    public CraftAbstractVillager(CraftServer server, net.minecraft.world.entity.npc.AbstractVillager entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.npc.AbstractVillager getHandle() {
        return (Villager) this.entity;
    }

    @Override
    public String toString() {
        return "CraftAbstractVillager";
    }

    @Override
    public Inventory getInventory() {
        return new CraftInventory(this.getHandle().getInventory());
    }

    private CraftMerchant getMerchant() {
        return this.getHandle().getCraftMerchant();
    }

    @Override
    public List<MerchantRecipe> getRecipes() {
        return this.getMerchant().getRecipes();
    }

    @Override
    public void setRecipes(List<MerchantRecipe> recipes) {
        this.getMerchant().setRecipes(recipes);
    }

    @Override
    public MerchantRecipe getRecipe(int i) {
        return this.getMerchant().getRecipe(i);
    }

    @Override
    public void setRecipe(int i, MerchantRecipe merchantRecipe) {
        this.getMerchant().setRecipe(i, merchantRecipe);
    }

    @Override
    public int getRecipeCount() {
        return this.getMerchant().getRecipeCount();
    }

    @Override
    public boolean isTrading() {
        return this.getTrader() != null;
    }

    @Override
    public HumanEntity getTrader() {
        return this.getMerchant().getTrader();
    }
}
