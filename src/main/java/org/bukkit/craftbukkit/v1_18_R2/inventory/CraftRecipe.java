package org.bukkit.craftbukkit.v1_18_R2.inventory;

import java.util.ArrayList;
import java.util.List;

import com.mohistmc.bukkit.inventory.MohistSpecialIngredient;
import net.minecraft.world.item.crafting.Ingredient;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;

public interface CraftRecipe extends Recipe {

    void addToCraftingManager();

    default Ingredient toNMS(RecipeChoice bukkit, boolean requireNotEmpty) {
        Ingredient stack;

        if (bukkit == null) {
            stack = Ingredient.EMPTY;
        } else if (bukkit instanceof RecipeChoice.MaterialChoice) {
            stack = new Ingredient(((RecipeChoice.MaterialChoice) bukkit).getChoices().stream().map((mat) -> new net.minecraft.world.item.crafting.Ingredient.ItemValue(CraftItemStack.asNMSCopy(new ItemStack(mat)))));
        } else if (bukkit instanceof RecipeChoice.ExactChoice) {
            stack = new Ingredient(((RecipeChoice.ExactChoice) bukkit).getChoices().stream().map((mat) -> new net.minecraft.world.item.crafting.Ingredient.ItemValue(CraftItemStack.asNMSCopy(mat))));
            stack.exact = true;
        }else if (bukkit instanceof MohistSpecialIngredient mohistSpecialIngredient) {
            stack = mohistSpecialIngredient.getIngredient();
        } else {
            throw new IllegalArgumentException("Unknown recipe stack instance " + bukkit);
        }

        stack.dissolve();
        if (stack.isVanilla() && requireNotEmpty && stack.getItems().length == 0) {
            throw new IllegalArgumentException("Recipe requires at least one non-air choice!");
        } else {
            return stack;
        }
    }

    public static RecipeChoice toBukkit(Ingredient list) {
        list.dissolve();
        if (!list.isVanilla()) {
            return new MohistSpecialIngredient(list);
        }

        net.minecraft.world.item.ItemStack[] items = list.getItems();
        if (items.length == 0) {
            return null;
        }

        if (list.exact) {
            List<org.bukkit.inventory.ItemStack> choices = new ArrayList<>(items.length);
            for (net.minecraft.world.item.ItemStack i : items) {
                choices.add(CraftItemStack.asBukkitCopy(i));
            }

            return new RecipeChoice.ExactChoice(choices);
        } else {

            List<org.bukkit.Material> choices = new ArrayList<>(items.length);
            for (net.minecraft.world.item.ItemStack i : items) {
                choices.add(CraftMagicNumbers.getMaterial(i.getItem()));
            }

            return new RecipeChoice.MaterialChoice(choices);
        }
    }
}
