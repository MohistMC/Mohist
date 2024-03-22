package com.mohistmc.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class RecipeManagerFix {

    public static Map<RecipeType<?>, Object2ObjectLinkedOpenHashMap<ResourceLocation, Recipe<?>>> immutableMap(Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> old) {
        Map<RecipeType<?>, Object2ObjectLinkedOpenHashMap<ResourceLocation, Recipe<?>>> recipesCB;

        Map<RecipeType<?>, Object2ObjectLinkedOpenHashMap<ResourceLocation, Recipe<?>>> newMap = Maps.newHashMap();
        for (Entry<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> oldMap : old.entrySet()) {
            Object2ObjectLinkedOpenHashMap<ResourceLocation, Recipe<?>> object2ObjectLinkedOpenHashMap = new Object2ObjectLinkedOpenHashMap<>();
            for (Entry<ResourceLocation, Recipe<?>> ff : oldMap.getValue().entrySet()) {
                object2ObjectLinkedOpenHashMap.putAndMoveToFirst(ff.getKey(), ff.getValue());
            }
            newMap.put(oldMap.getKey(), object2ObjectLinkedOpenHashMap);
        }
        // CraftBukkit
        recipesCB = newMap.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue));
        return recipesCB;
    }
}
