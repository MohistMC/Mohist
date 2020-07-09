package org.bukkit.craftbukkit.v1_15_R1.inventory;

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
        this.recipes = MinecraftServer.getServer().getRecipeManager().recipes.entrySet().iterator();
    }

    @Override
    public boolean hasNext() {
        return (current != null && current.hasNext()) || recipes.hasNext();
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
