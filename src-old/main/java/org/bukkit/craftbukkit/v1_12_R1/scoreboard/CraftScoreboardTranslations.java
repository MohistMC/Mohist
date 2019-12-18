package org.bukkit.craftbukkit.v1_12_R1.scoreboard;

import com.google.common.collect.ImmutableBiMap;
import net.minecraft.scoreboard.Scoreboard;
import org.bukkit.scoreboard.DisplaySlot;

class CraftScoreboardTranslations {
    static final int MAX_DISPLAY_SLOT = 3;
    static ImmutableBiMap<DisplaySlot, String> SLOTS = ImmutableBiMap.of(
            DisplaySlot.BELOW_NAME, "belowName",
            DisplaySlot.PLAYER_LIST, "list",
            DisplaySlot.SIDEBAR, "sidebar");

    private CraftScoreboardTranslations() {
    }

    static DisplaySlot toBukkitSlot(int i) {
        return SLOTS.inverse().get(Scoreboard.getObjectiveDisplaySlot(i));
    }

    static int fromBukkitSlot(DisplaySlot slot) {
        return Scoreboard.getObjectiveDisplaySlotNumber(SLOTS.get(slot));
    }

}
