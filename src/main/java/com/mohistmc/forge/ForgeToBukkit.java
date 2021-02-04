package com.mohistmc.forge;

import net.minecraft.util.DamageSource;
import org.bukkit.event.entity.EntityDamageEvent;

public class ForgeToBukkit {

    public static EntityDamageEvent.DamageCause getDamageCause(DamageSource source) {
        return ForgeToBukkit.damageCause(source);
    }

    public static EntityDamageEvent.DamageCause damageCause(DamageSource ds) {
        EntityDamageEvent.DamageCause dc;
        if (ds == DamageSource.IN_FIRE) {
            dc = EntityDamageEvent.DamageCause.FIRE;
        } else if (ds == DamageSource.LIGHTNING_BOLT) {
            dc = EntityDamageEvent.DamageCause.LIGHTNING;
        } else if (ds == DamageSource.ON_FIRE) {
            dc = EntityDamageEvent.DamageCause.FIRE_TICK;
        } else if (ds == DamageSource.LAVA) {
            dc = EntityDamageEvent.DamageCause.LAVA;
        } else if (ds == DamageSource.HOT_FLOOR) {
            dc = EntityDamageEvent.DamageCause.HOT_FLOOR;
        } else if (ds == DamageSource.IN_WALL) {
            dc = EntityDamageEvent.DamageCause.SUFFOCATION;
        } else if (ds == DamageSource.CRAMMING) {
            dc = EntityDamageEvent.DamageCause.CRAMMING;
        } else if (ds == DamageSource.DROWN) {
            dc = EntityDamageEvent.DamageCause.DROWNING;
        } else if (ds == DamageSource.STARVE) {
            dc = EntityDamageEvent.DamageCause.STARVATION;
        } else if (ds == DamageSource.CACTUS) {
            dc = EntityDamageEvent.DamageCause.CONTACT;
        } else if (ds == DamageSource.FALL) {
            dc = EntityDamageEvent.DamageCause.FALL;
        } else if (ds == DamageSource.FLY_INTO_WALL) {
            dc = EntityDamageEvent.DamageCause.FLY_INTO_WALL;
        } else if (ds == DamageSource.OUT_OF_WORLD) {
            dc = EntityDamageEvent.DamageCause.VOID;
        } else if (ds == DamageSource.GENERIC) {
            dc = EntityDamageEvent.DamageCause.CUSTOM;
        } else if (ds == DamageSource.MAGIC) {
            dc = EntityDamageEvent.DamageCause.MAGIC;
        } else if (ds == DamageSource.WITHER) {
            dc = EntityDamageEvent.DamageCause.WITHER;
        } else if (ds == DamageSource.ANVIL) {
            dc = EntityDamageEvent.DamageCause.CUSTOM;
        } else if (ds == DamageSource.FALLING_BLOCK) {
            dc = EntityDamageEvent.DamageCause.FALLING_BLOCK;
        } else if (ds == DamageSource.DRAGON_BREATH) {
            dc = EntityDamageEvent.DamageCause.DRAGON_BREATH;
        } else if (ds == DamageSource.DRYOUT) {
            dc = EntityDamageEvent.DamageCause.DRYOUT;
        } else if (ds == DamageSource.SWEET_BERRY_BUSH) {
            dc = EntityDamageEvent.DamageCause.THORNS;
        } else {
            dc = EntityDamageEvent.DamageCause.CUSTOM;
        }
        return dc;
    }

}
