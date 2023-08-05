package org.bukkit.craftbukkit.v1_19_R3.inventory;

import java.util.Iterator;
import java.util.Map;

import com.mohistmc.inventory.MohistSpecialRecipe;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeType;
import org.bukkit.inventory.Recipe;

public class RecipeIterator implements Iterator<Recipe> {
    private final Iterator<Map.Entry<RecipeType<?>, Object2ObjectLinkedOpenHashMap<ResourceLocation, net.minecraft.world.item.crafting.Recipe<?>>>> recipes;
    private Iterator<net.minecraft.world.item.crafting.Recipe<?>> current;

    public RecipeIterator() {
        this.recipes = MinecraftServer.getServer().getRecipeManager().recipesCB.entrySet().iterator();
    }

    @Override
    public boolean hasNext() {
        if (current != null && current.hasNext()) {
            return true;
        }

        if (recipes.hasNext()) {
            current = recipes.next().getValue().values().iterator();
            return hasNext();
        }

        return false;
    }

    @Override
    public Recipe next() {
        if (current == null || !current.hasNext()) {
            current = recipes.next().getValue().values().iterator();
            return next();
        }

        net.minecraft.world.item.crafting.Recipe<?> recipe = current.next();
        try {
            return recipe.toBukkitRecipe();
        } catch (Throwable e) {
            //throw new RuntimeException("Error converting recipe " + recipe.getId(), e);
            return new MohistSpecialRecipe(recipe);
        }
    }

    @Override
    public void remove() {
        if (current == null) {
            throw new IllegalStateException("next() not yet called");
        }

        current.remove();
    }
}
