package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.monster.MonsterEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Monster;

public class CraftMonster extends CraftCreature implements Monster {

    public CraftMonster(CraftServer server, MonsterEntity entity) {
        super(server, entity);
    }

    @Override
    public MonsterEntity getHandle() {
        return (MonsterEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftMonster";
    }
}
