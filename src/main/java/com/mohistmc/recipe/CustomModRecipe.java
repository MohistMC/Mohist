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
import net.minecraft.util.ResourceLocation;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

/**
 * Bukkit API wrapper for non-vanilla IRecipe classes
 *
 * Form net.minecraftforge.cauldron.inventory.CustomModRecipe
 */
public class CustomModRecipe implements Recipe, Keyed {
    private final IRecipe iRecipe;
    private NamespacedKey key;

    public CustomModRecipe(IRecipe iRecipe) {
        this(iRecipe, null);
    }

    public CustomModRecipe(IRecipe iRecipe, ResourceLocation key) {
        this.iRecipe = iRecipe;
        try {
            this.key = (key != null ? CraftNamespacedKey.fromMinecraft(key) : NamespacedKey.randomKey());
        } catch (Exception e) {
            this.key = NamespacedKey.randomKey();
        }
    }

    @Override
    public ItemStack getResult() {
        return CraftItemStack.asCraftMirror(iRecipe.getResultItem());
    }

    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    public IRecipe getHandle() {
        return iRecipe;
    }
}
