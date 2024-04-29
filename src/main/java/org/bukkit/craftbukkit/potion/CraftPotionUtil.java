package org.bukkit.craftbukkit.potion;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CraftPotionUtil {

    public static MobEffectInstance fromBukkit(PotionEffect effect) {
        Holder<MobEffect> type = CraftPotionEffectType.bukkitToMinecraftHolder(effect.getType());
        return new MobEffectInstance(type, effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles());
    }

    public static PotionEffect toBukkit(MobEffectInstance effect) {
        PotionEffectType type = CraftPotionEffectType.minecraftHolderToBukkit(effect.getEffect());
        int amp = effect.getAmplifier();
        int duration = effect.getDuration();
        boolean ambient = effect.isAmbient();
        boolean particles = effect.isVisible();
        return new PotionEffect(type, duration, amp, ambient, particles);
    }

    public static boolean equals(Holder<MobEffect> mobEffect, PotionEffectType type) {
        PotionEffectType typeV = CraftPotionEffectType.minecraftHolderToBukkit(mobEffect);
        return typeV.equals(type);
    }
}
