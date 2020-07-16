package org.bukkit.craftbukkit.boss;

import net.minecraft.entity.boss.CommandBossBar;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;

public class CraftKeyedBossbar extends CraftBossBar implements KeyedBossBar {

    public CraftKeyedBossbar(CommandBossBar bossBattleCustom) {
        super(bossBattleCustom);
    }

    @Override
    public NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(getHandle().getId());
    }

    @Override
    public CommandBossBar getHandle() {
        return (CommandBossBar) super.getHandle();
    }
}
