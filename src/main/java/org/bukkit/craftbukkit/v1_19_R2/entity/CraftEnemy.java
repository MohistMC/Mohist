package org.bukkit.craftbukkit.v1_19_R2.entity;

import org.bukkit.entity.Enemy;

public interface CraftEnemy extends Enemy {

    net.minecraft.world.entity.monster.Enemy getHandle();
}
