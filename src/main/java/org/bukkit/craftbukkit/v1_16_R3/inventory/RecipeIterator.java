package org.bukkit.craftbukkit.v1_16_R3.inventory;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import org.bukkit.inventory.Recipe;

public class RecipeIterator implements Iterator<Recipe> {
    private final Iterator<Entry<IRecipeType<?>, Object2ObjectLinkedOpenHashMap<ResourceLocation, IRecipe<?>>>> recipes;
    private Iterator<IRecipe<?>> current;

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
        }

        return current.next().toBukkitRecipe();
    }

    @Override
    public void remove() {
        if (current == null) {
            throw new IllegalStateException("next() not yet called");
        }

        current.remove();
    }
}
