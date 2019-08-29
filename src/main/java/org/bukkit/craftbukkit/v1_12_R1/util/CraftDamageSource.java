package org.bukkit.craftbukkit.v1_12_R1.util;

import net.minecraft.util.DamageSource;

// Util class to create custom DamageSources.
public final class CraftDamageSource extends DamageSource {
    private CraftDamageSource(String identifier) {
        super(identifier);
    }

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
            newSource.setExplosion();
        }

        return newSource;
    }
}
