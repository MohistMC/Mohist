package com.mohistmc.bukkit.inventory;

import net.minecraft.world.effect.MobEffect;
import org.bukkit.craftbukkit.v1_20_R2.potion.CraftPotionEffectType;

/**
 * @author Mgazul by MohistMC
 * @date 2023/8/17 22:15:01
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
