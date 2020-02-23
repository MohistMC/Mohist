package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.boss.WitherEntity;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.boss.CraftBossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wither;

public class CraftWither extends CraftMonster implements Wither {

    private BossBar bossBar;

    public CraftWither(CraftServer server, WitherEntity entity) {
        super(server, entity);

        if (entity.bossInfo != null) {
            this.bossBar = new CraftBossBar(entity.bossInfo);
        }
    }

    @Override
    public WitherEntity getHandle() {
        return (WitherEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftWither";
    }

    @Override
    public EntityType getType() {
        return EntityType.WITHER;
    }

    @Override
    public BossBar getBossBar() {
        return bossBar;
    }
}
