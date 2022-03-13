package org.bukkit.craftbukkit.v1_18_R2.util;

import net.minecraft.world.damagesource.DamageSource;

// Util class to create custom DamageSources.
public final class CraftDamageSource extends DamageSource {
    public static DamageSource copyOf(final DamageSource original) {
        CraftDamageSource newSource = new CraftDamageSource(original.msgId);

        // Check ignoresArmor
        if (original.isBypassArmor()) {
            newSource.bypassArmor();
        }

        // Check magic
        if (original.isMagic()) {
            newSource.setMagic();
        }

        // Check fire
        if (original.isExplosion()) {
            newSource.setExplosion();
        }

        return newSource;
    }

    private CraftDamageSource(String identifier) {
        super(identifier);
    }
}
