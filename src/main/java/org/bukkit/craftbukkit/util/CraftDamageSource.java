package org.bukkit.craftbukkit.util;

import net.minecraft.entity.damage.DamageSource;

// Util class to create custom DamageSources.
public final class CraftDamageSource extends DamageSource {
    public static DamageSource copyOf(final DamageSource original) {
        CraftDamageSource newSource = new CraftDamageSource(original.name);

        // Check ignoresArmor
        if (original.bypassesArmor()) {
            newSource.setBypassesArmor();
        }

        // Check magic
        if (original.getMagic()) {
            newSource.setUsesMagic();
        }

        // Check fire
        if (original.isExplosive()) {
            newSource.setExplosive();
        }

        return newSource;
    }

    private CraftDamageSource(String identifier) {
        super(identifier);
    }
}
