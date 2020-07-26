package red.mohist.forge.util;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.item.crafting.SpecialRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftShapedRecipe;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftShapelessRecipe;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;

public class MohistSpecialRecipe {

    public static class Shapeless extends CraftShapelessRecipe {

        private final IRecipe<?> recipe;

        public Shapeless(ItemStack result, IRecipe<?> recipe) {
            super(CraftNamespacedKey.fromMinecraft(recipe.getId()), result);
            this.recipe = recipe;
            this.setGroup(this.recipe.getGroup());
        }

        @Override
        public void addToCraftingManager() {
            ((RecipeManager) ((CraftServer) Bukkit.getServer()).getServer()
                    .getRecipeManager()).addRecipe(recipe);
        }
    }

    public static class Shaped extends CraftShapedRecipe {

        private final SpecialRecipe recipe;

        public Shaped(ItemStack result, SpecialRecipe recipe) {
            super(CraftNamespacedKey.fromMinecraft(recipe.getId()), result);
            this.recipe = recipe;
            this.setGroup(this.recipe.getGroup());
        }

        @Override
        public void addToCraftingManager() {
            ((RecipeManager) ((CraftServer) Bukkit.getServer()).getServer()
                    .getRecipeManager()).addRecipe(recipe);
        }
    }

    public static class Dynamic implements CraftRecipe, Keyed {

        private final IRecipe<?> recipe;

        public Dynamic(IRecipe<?> recipe) {
            this.recipe = recipe;
        }

        @Override
        public void addToCraftingManager() {
            ((RecipeManager) ((CraftServer) Bukkit.getServer()).getServer().getRecipeManager()).addRecipe(this.recipe);
        }

        @Override
        public @NotNull ItemStack getResult() {
            return CraftItemStack.asCraftMirror(this.recipe.getRecipeOutput());
        }

        @Override
        @NotNull
        public NamespacedKey getKey() {
            return CraftNamespacedKey.fromMinecraft(this.recipe.getId());
        }
    }

    public static CraftRecipe shapeless(ItemStack result, IRecipe<?> recipe, Ingredient... ingredients) {
        if (recipe.getRecipeOutput().isEmpty()) {
            return new Dynamic(recipe);
        }
        Shapeless shapeless = new Shapeless(result, recipe);
        for (Ingredient ingredient : ingredients) {
            shapeless.addIngredient(CraftRecipe.toBukkit(ingredient));
        }
        return shapeless;
    }

    public static CraftShapedRecipe shaped(ItemStack result, SpecialRecipe recipe, int width, Ingredient... ingredients) {
        Shaped shaped = new Shaped(result, recipe);
        int height = ingredients.length / width;
        String[] shape = new String[height];
        char c = 'a';
        for (int i = 0; i < height; i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < width; j++) {
                builder.append(c++);
            }
            shape[i] = builder.toString();
        }
        shaped.shape(shape);
        c = 'a';
        for (Ingredient ingredient : ingredients) {
            RecipeChoice choice = CraftRecipe.toBukkit(ingredient);
            if (choice != null) {
                shaped.setIngredient(c, choice);
            }
            c++;
        }
        return shaped;
    }
}
