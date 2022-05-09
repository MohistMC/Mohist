/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2022.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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
