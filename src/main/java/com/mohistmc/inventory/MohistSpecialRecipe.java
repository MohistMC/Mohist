package com.mohistmc.inventory;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftComplexRecipe;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/20 23:02:32
 */
public class MohistSpecialRecipe extends CraftComplexRecipe {

    private final IRecipe<?> recipe;

    public MohistSpecialRecipe(IRecipe<?> recipe) {
        super(null);
        this.recipe = recipe;
    }

    @Override
    public @NotNull ItemStack getResult() {
        return CraftItemStack.asCraftMirror(this.recipe.getResultItem());
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(this.recipe.getId());
    }

    @Override
    public void addToCraftingManager() {
        ServerLifecycleHooks.getCurrentServer().getRecipeManager().addRecipe(this.recipe);
    }
}
