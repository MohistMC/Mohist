package com.mohistmc.recipe;

import net.minecraft.item.crafting.IRecipe;
import org.bukkit.inventory.Recipe;

/**
 * @author Mgazul
 * @date 2020/4/10 13:08
 */
public class RecipeUtils {

    // AbstractMethodError - handle custom recipe classes without Bukkit API equivalents
    public static Recipe toBukkitRecipe(IRecipe recipe){
        try {
            return recipe == null ? null : recipe.toBukkitRecipe();
        } catch (AbstractMethodError ex) {
            return new CustomModRecipe(recipe, recipe.getId());
        }
    }
}
