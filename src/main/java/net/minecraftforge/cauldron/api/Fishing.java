package net.minecraftforge.cauldron.api;

import com.google.common.base.Predicate;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * Bukkit interface to Forge's FishingHooks class.
 */
public interface Fishing {

    /**
     * Add a WeightedRandomFishable to the 'fish' results table.
     *
     * @param fish fishable item
     */
    public void addFish(WeightedRandomFishable fish);

    /**
     * Add a WeightedRandomFishable to the 'junk' results table.
     *
     * @param fish fishable item
     */
    public void addJunk(WeightedRandomFishable fish);

    /**
     * Add a WeightedRandomFishable to the 'treasure' results table.
     *
     * @param fish fishable item
     */
    public void addTreasure(WeightedRandomFishable fish);

    /**
     * Remove WeightedRandomFishables from the 'fish' results table.
     * Modifications to the Fishable objects will not be kept.
     *
     * @param test a Predicate giving the removal condition
     */
    public void removeMatchingFish(Predicate<WeightedRandomFishable> test);

    /**
     * Remove WeightedRandomFishables from the 'junk' results table.
     * Modifications to the Fishable objects will not be kept.
     *
     * @param test a Predicate giving the removal condition
     */
    public void removeMatchingJunk(Predicate<WeightedRandomFishable> test);

    /**
     * Remove WeightedRandomFishables from the 'treasure' results table.
     * Modifications to the Fishable objects will not be kept.
     *
     * @param test a Predicate giving the removal condition
     */
    public void removeMatchingTreasure(Predicate<WeightedRandomFishable> test);

    /**
     * Get the item pulled up from a simulated fishing attempt.
     *
     * @param rand                         the Random instance to use
     * @param baseChance                   roughly, a percentage chance (0-1) to
     *                                     get a fish
     * @param fishingLuckEnchantmentLevel  the value of {@link org.bukkit.enchantments.Enchantment#LUCK}
     *                                     on the fishing rod
     * @param fishingSpeedEnchantmentLevel the value of {@link org.bukkit.enchantments.Enchantment#LURE}
     *                                     on the fishing rod
     * @return the item fished
     */
    public ItemStack getRandomFishable(Random rand, float baseChance, int fishingLuckEnchantmentLevel, int fishingSpeedEnchantmentLevel);

}
