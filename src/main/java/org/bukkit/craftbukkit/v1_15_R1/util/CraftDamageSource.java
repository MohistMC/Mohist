package org.bukkit.craftbukkit.v1_15_R1.util;

import net.minecraft.util.DamageSource;

// Util class to create custom DamageSources.
public final class CraftDamageSource extends DamageSource {
    public static DamageSource copyOf(final DamageSource original) {
        CraftDamageSource newSource = new CraftDamageSource(original.damageType);

        // Check ignoresArmor
        if (original.isUnblockable()) {
            newSource.setDamageBypassesArmor();
        }

        // Check magic
        if (original.isMagicDamage()) {
            newSource.setMagicDamage();
        }

        // Check fire
        if (original.isExplosion()) {
            newSource.setFireDamage();
        }

        return newSource;
    }

    private CraftDamageSource(String identifier) {
        super(identifier);
    }
}
