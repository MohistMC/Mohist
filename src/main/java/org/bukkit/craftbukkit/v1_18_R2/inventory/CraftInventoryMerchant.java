package org.bukkit.craftbukkit.v1_18_R2.inventory;

import net.minecraft.world.inventory.MerchantContainer;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

public class CraftInventoryMerchant extends CraftInventory implements MerchantInventory {

    private final net.minecraft.world.item.trading.Merchant merchant;

    public CraftInventoryMerchant(net.minecraft.world.item.trading.Merchant merchant, MerchantContainer inventory) {
        super(inventory);
        this.merchant = merchant;
    }

    @Override
    public int getSelectedRecipeIndex() {
        return getInventory().selectionHint;
    }

    @Override
    public MerchantRecipe getSelectedRecipe() {
        net.minecraft.world.item.trading.MerchantOffer nmsRecipe = getInventory().getActiveOffer();
        return (nmsRecipe == null) ? null : nmsRecipe.asBukkit();
    }

    @Override
    public MerchantContainer getInventory() {
        return (MerchantContainer) inventory;
    }

    @Override
    public Merchant getMerchant() {
        return merchant.getCraftMerchant();
    }
}
