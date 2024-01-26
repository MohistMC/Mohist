package com.mohistmc.bukkit.potion;

import net.minecraft.world.effect.MobEffect;
import org.bukkit.craftbukkit.v1_18_R2.potion.CraftPotionEffectType;

/**
 * @author Mgazul by MohistMC
 * @date 2023/8/5 13:53:31
 */
public class MohistPotionEffect extends CraftPotionEffectType {

    private final String name;

    public MohistPotionEffect(MobEffect handle, String name) {
        super(handle);
        this.name = name;
    }

    @Override
    public String getName() {
        String name = super.getName();
        if (name.startsWith("UNKNOWN_EFFECT_TYPE_")) {
            return this.name;
        } else {
            return name;
        }
    }
}
