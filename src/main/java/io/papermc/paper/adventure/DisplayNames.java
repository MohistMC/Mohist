package io.papermc.paper.adventure;

import net.minecraft.entity.player.ServerPlayerEntity;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;

public final class DisplayNames {
    private DisplayNames() {
    }

    public static String getLegacy(final CraftPlayer player) {
        return getLegacy(player.getHandle());
    }

    public static String getLegacy(final ServerPlayerEntity player) {
        final String legacy = player.displayName;
        if (legacy != null) {
            return PaperAdventure.LEGACY_SECTION_UXRC.serialize(player.adventure$displayName) + ChatColor.getLastColors(player.displayName);
        }
        return PaperAdventure.LEGACY_SECTION_UXRC.serialize(player.adventure$displayName);
    }
}
