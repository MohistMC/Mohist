package org.bukkit.craftbukkit.v1_20_R1.inventory;

import com.google.common.base.Preconditions;
import com.mohistmc.bukkit.inventory.MohistSpecialRecipe;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeType;
import org.bukkit.Keyed;
import org.bukkit.inventory.Recipe;

import java.util.Iterator;
import java.util.Map;

public class RecipeIterator implements Iterator<Recipe> {
    private final Iterator<Map.Entry<RecipeType<?>, Object2ObjectLinkedOpenHashMap<ResourceLocation, net.minecraft.world.item.crafting.Recipe<?>>>> recipes;
    private final Iterator<Map.Entry<RecipeType<?>, Map<ResourceLocation, net.minecraft.world.item.crafting.Recipe<?>>>> recipesV;
    private Iterator<net.minecraft.world.item.crafting.Recipe<?>> current;

    public RecipeIterator() {
        this.recipes = MinecraftServer.getServer().getRecipeManager().recipesCB.entrySet().iterator();
        this.recipesV = MinecraftServer.getServer().getRecipeManager().recipes.entrySet().iterator();
    }

    @Override
    public boolean hasNext() {
        if (current != null && current.hasNext()) {
            return true;
        }

        if (recipes.hasNext()) {
            current = recipes.next().getValue().values().iterator();
            return hasNext();
        } else {
            if (recipesV.hasNext()) {
                current = recipesV.next().getValue().values().iterator();
                return hasNext();
            }
        }

        return false;
    }

    @Override
    public Recipe next() {
        try {
            if (current == null || !current.hasNext()) {
                current = recipes.next().getValue().values().iterator();
                return next();
            }
        } catch (Throwable e) {
            if (!current.hasNext()) {
                current = recipesV.next().getValue().values().iterator();
                return next();
            }
        }

        net.minecraft.world.item.crafting.Recipe<?> recipe = current.next();

        if (recipe.toBukkitRecipe() instanceof Keyed) {
            return recipe.toBukkitRecipe();
        }
        return new MohistSpecialRecipe(recipe);

    }

    @Override
    public void remove() {
        Preconditions.checkState(current != null, "next() not yet called");
        current.remove();
    }
}
