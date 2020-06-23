package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

public class CraftMerchantRecipe extends MerchantRecipe {

    private final net.minecraft.item.MerchantOffer handle;

    public CraftMerchantRecipe(net.minecraft.item.MerchantOffer merchantRecipe) {
        super(CraftItemStack.asBukkitCopy(merchantRecipe.sellingStack), 0);
        this.handle = merchantRecipe;
        addIngredient(CraftItemStack.asBukkitCopy(merchantRecipe.buyingStackFirst));
        addIngredient(CraftItemStack.asBukkitCopy(merchantRecipe.buyingStackSecond));
    }

    public CraftMerchantRecipe(ItemStack result, int uses, int maxUses, boolean experienceReward, int experience, float priceMultiplier) {
        super(result, uses, maxUses, experienceReward, experience, priceMultiplier);
        this.handle = new net.minecraft.item.MerchantOffer(
                net.minecraft.item.ItemStack.EMPTY,
                net.minecraft.item.ItemStack.EMPTY,
                CraftItemStack.asNMSCopy(result),
                uses,
                maxUses,
                experience,
                priceMultiplier,
                this
        );
        this.setExperienceReward(experienceReward);
    }

    @Override
    public int getUses() {
        return handle.uses;
    }

    @Override
    public void setUses(int uses) {
        handle.uses = uses;
    }

    @Override
    public int getMaxUses() {
        return handle.maxUses;
    }

    @Override
    public void setMaxUses(int maxUses) {
        handle.maxUses = maxUses;
    }

    @Override
    public boolean hasExperienceReward() {
        return handle.doesRewardEXP;
    }

    @Override
    public void setExperienceReward(boolean flag) {
        handle.doesRewardEXP = flag;
    }

    @Override
    public int getVillagerExperience() {
        return handle.givenEXP;
    }

    @Override
    public void setVillagerExperience(int villagerExperience) {
        handle.givenEXP = villagerExperience;
    }

    @Override
    public float getPriceMultiplier() {
        return handle.priceMultiplier;
    }

    @Override
    public void setPriceMultiplier(float priceMultiplier) {
        handle.priceMultiplier = priceMultiplier;
    }

    public net.minecraft.item.MerchantOffer toMinecraft() {
        List<ItemStack> ingredients = getIngredients();
        Preconditions.checkState(!ingredients.isEmpty(), "No offered ingredients");
        handle.buyingStackFirst = CraftItemStack.asNMSCopy(ingredients.get(0));
        if (ingredients.size() > 1) {
            handle.buyingStackSecond = CraftItemStack.asNMSCopy(ingredients.get(1));
        }
        return handle;
    }

    public static CraftMerchantRecipe fromBukkit(MerchantRecipe recipe) {
        if (recipe instanceof CraftMerchantRecipe) {
            return (CraftMerchantRecipe) recipe;
        } else {
            CraftMerchantRecipe craft = new CraftMerchantRecipe(recipe.getResult(), recipe.getUses(), recipe.getMaxUses(), recipe.hasExperienceReward(), recipe.getVillagerExperience(), recipe.getPriceMultiplier());
            craft.setIngredients(recipe.getIngredients());

            return craft;
        }
    }
}
