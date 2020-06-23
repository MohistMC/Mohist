package org.bukkit.craftbukkit.inventory;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import org.bukkit.inventory.Recipe;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap; // CraftBukkit

public class RecipeIterator implements Iterator<Recipe> {
    private final Iterator<Map.Entry<IRecipeType<?>, Object2ObjectLinkedOpenHashMap<ResourceLocation, IRecipe<?>>>> recipes; // fix recipe manager cascade compile error - Xiaowei Zhou
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
