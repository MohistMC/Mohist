package org.bukkit.craftbukkit.v1_15_R1.inventory;

import net.minecraft.server.MinecraftServer;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftNamespacedKey;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

public class CraftBlastingRecipe extends BlastingRecipe implements CraftRecipe {
    public CraftBlastingRecipe(NamespacedKey key, ItemStack result, RecipeChoice source, float experience, int cookingTime) {
        super(key, result, source, experience, cookingTime);
    }

    public static CraftBlastingRecipe fromBukkitRecipe(BlastingRecipe recipe) {
        if (recipe instanceof CraftBlastingRecipe) {
            return (CraftBlastingRecipe) recipe;
        }
        CraftBlastingRecipe ret = new CraftBlastingRecipe(recipe.getKey(), recipe.getResult(), recipe.getInputChoice(), recipe.getExperience(), recipe.getCookingTime());
        ret.setGroup(recipe.getGroup());
        return ret;
    }

    @Override
    public void addToCraftingManager() {
        ItemStack result = this.getResult();

        MinecraftServer.getServer().getRecipeManager().addRecipe(new net.minecraft.item.crafting.BlastingRecipe(CraftNamespacedKey.toMinecraft(this.getKey()), this.getGroup(), toNMS(this.getInputChoice(), true), CraftItemStack.asNMSCopy(result), getExperience(), getCookingTime()));
    }
}
