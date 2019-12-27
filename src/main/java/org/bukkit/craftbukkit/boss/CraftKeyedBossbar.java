package org.bukkit.craftbukkit.boss;

import net.minecraft.server.CustomServerBossInfo;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;

public class CraftKeyedBossbar extends CraftBossBar implements KeyedBossBar {

    public CraftKeyedBossbar(BossInfoCustom bossBattleCustom) {
        super(bossBattleCustom);
    }

    @Override
    public NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(getHandle().getKey());
    }

    @Override
    public BossInfoCustom getHandle() {
        return (BossInfoCustom) super.getHandle();
    }
}
