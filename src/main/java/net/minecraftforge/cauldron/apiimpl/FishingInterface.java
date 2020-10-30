package net.minecraftforge.cauldron.apiimpl;

import com.google.common.base.Predicate;
import net.minecraftforge.cauldron.api.Fishing;
import net.minecraftforge.cauldron.api.WeightedRandomFishable;
import net.minecraftforge.common.FishingHooks;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class FishingInterface implements Fishing {
    private static net.minecraft.util.WeightedRandomFishable toNms(WeightedRandomFishable bukkit) {
        net.minecraft.util.WeightedRandomFishable ret =
                new net.minecraft.util.WeightedRandomFishable(
                        CraftItemStack.asNMSCopy(bukkit.getItemStack()),
                        bukkit.getWeight()).func_150709_a(bukkit.getDamageFraction());
        if (bukkit.hasRandomEnchantments()) {
            ret.func_150707_a();
        }
        return ret;
    }

    private static WeightedRandomFishable toBukkit(net.minecraft.util.WeightedRandomFishable nms) {
        return new WeightedRandomFishable(CraftItemStack.asBukkitCopy(nms.field_150711_b), nms.itemWeight)
                .withDamageFraction(nms.field_150712_c)
                .withRandomEnchantments(nms.field_150710_d);
    }

    private static class PredicateProxy implements Predicate<net.minecraft.util.WeightedRandomFishable> {
        private Predicate<WeightedRandomFishable> bukkitPredicate;

        public PredicateProxy(Predicate<WeightedRandomFishable> predicate) {
            this.bukkitPredicate = predicate;
        }

        @Override
        public boolean apply(net.minecraft.util.WeightedRandomFishable input) {
            return bukkitPredicate.apply(toBukkit(input));
        }
    }

    private static PredicateProxy toNms(Predicate<WeightedRandomFishable> predicate) {
        return new PredicateProxy(predicate);
    }

    @Override
    public void addFish(WeightedRandomFishable fish) {
        FishingHooks.addFish(toNms(fish));
    }

    @Override
    public void addJunk(WeightedRandomFishable fish) {
        FishingHooks.addJunk(toNms(fish));
    }

    @Override
    public void addTreasure(WeightedRandomFishable fish) {
        FishingHooks.addTreasure(toNms(fish));
    }

    @Override
    public void removeMatchingFish(Predicate<WeightedRandomFishable> test) {
        FishingHooks.removeFish(toNms(test));
    }

    @Override
    public void removeMatchingJunk(Predicate<WeightedRandomFishable> test) {
        FishingHooks.removeJunk(toNms(test));
    }

    @Override
    public void removeMatchingTreasure(Predicate<WeightedRandomFishable> test) {
        FishingHooks.removeTreasure(toNms(test));
    }

    @Override
    public ItemStack getRandomFishable(Random rand, float baseChance, int fishingLuckEnchantmentLevel, int fishingSpeedEnchantmentLevel) {
        return CraftItemStack.asCraftMirror(FishingHooks.getRandomFishable(rand, baseChance, fishingLuckEnchantmentLevel, fishingSpeedEnchantmentLevel));
    }
}
