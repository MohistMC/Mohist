package org.bukkit.craftbukkit.boss;

import net.minecraft.server.CustomServerBossInfo;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;

public class CraftKeyedBossbar extends CraftBossBar implements KeyedBossBar {

    public CraftKeyedBossbar(CustomServerBossInfo bossBattleCustom) {
        super(bossBattleCustom);
    }

    @Override
    public NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(getHandle().getId());
    }

    @Override
    public CustomServerBossInfo getHandle() {
        return (CustomServerBossInfo) super.getHandle();
    }
}
