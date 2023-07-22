package com.mohistmc.inventory;

import net.minecraft.world.item.crafting.Ingredient;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/20 23:18:51
 */
public class MohistSpecialIngredient implements RecipeChoice {

    private final Ingredient ingredient;

    public MohistSpecialIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @NotNull
    @Override
    public ItemStack getItemStack() {
        net.minecraft.world.item.ItemStack[] items = ingredient.getItems();
        return items.length > 0 ? CraftItemStack.asCraftMirror(items[0]) : new ItemStack(Material.AIR, 0);
    }

    @NotNull
    @Override
    public RecipeChoice clone() {
        try {
            return (RecipeChoice) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public boolean test(@NotNull ItemStack itemStack) {
        return ingredient.test(CraftItemStack.asNMSCopy(itemStack));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MohistSpecialIngredient that = (MohistSpecialIngredient) o;
        return Objects.equals(ingredient, that.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient);
    }
}
