package red.mohist.recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

/**
 * Bukkit API wrapper for non-vanilla IRecipe classes
 * <p>
 * Form net.minecraftforge.cauldron.inventory.CustomModRecipe
 */
public class CustomModRecipe implements Recipe, Keyed {
    private final IRecipe iRecipe;
    private NamespacedKey key;

    public CustomModRecipe(IRecipe iRecipe) {
        this(iRecipe, null);
    }

    public CustomModRecipe(IRecipe iRecipe, ResourceLocation key) {
        this.iRecipe = iRecipe;
        try {
            this.key = (key != null ? CraftNamespacedKey.fromMinecraft(key) : NamespacedKey.randomKey());
        } catch (Exception e) {
            this.key = NamespacedKey.randomKey();
        }
    }

    @Override
    public ItemStack getResult() {
        return CraftItemStack.asCraftMirror(iRecipe.getRecipeOutput());
    }

    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    public IRecipe getHandle() {
        return iRecipe;
    }
}
