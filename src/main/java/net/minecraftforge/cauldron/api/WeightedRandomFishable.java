package net.minecraftforge.cauldron.api;

import org.bukkit.inventory.ItemStack;

public class WeightedRandomFishable {
    private ItemStack itemStack;
    private int weight;
    private boolean hasRandomEnchantments;
    private float damageFraction;

    public WeightedRandomFishable(ItemStack itemStack, int weight) {
        this.itemStack = itemStack;
        this.weight = weight;
    }

    /**
     * Setting this value results in fished items having random damage when fished. The damage is in a triangular
     * random distribution (think about rolling 2 dice), with the wide end at 100% and
     * the narrow end at (100% - damageFraction).
     *
     * For use in a chaining constructor.
     *
     * @param damageFraction low boundary for random distribution
     * @return this WeightedRandomFishable, for chaining
     */
    public final WeightedRandomFishable withDamageFraction(float damageFraction) {
        damageFraction = damageFraction;
        return this;
    }

    /**
     * Mark this WeightedRandomFishable as receiving random enchantments.
     * @return this WeightedRandomFishable, for chaining
     */
    public final WeightedRandomFishable withRandomEnchantments() {
        hasRandomEnchantments = true;
        return this;
    }

    /**
     * Set whether this WeightedRandomFishable receives random enchantments.
     * (Use this method if loading from another source, such as a config file.)
     * @return this WeightedRandomFishable, for chaining
     */
    public final WeightedRandomFishable withRandomEnchantments(boolean hasEnchants) {
        hasRandomEnchantments = hasEnchants;
        return this;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getWeight() {
        return weight;
    }

    public boolean hasRandomEnchantments() {
        return hasRandomEnchantments;
    }

    public float getDamageFraction() {
        return damageFraction;
    }
}
