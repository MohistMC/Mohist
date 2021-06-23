package net.kyori.adventure.bossbar;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.adventure.VanillaBossBarListener;
import net.minecraft.world.server.ServerBossInfo;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;

public abstract class HackyBossBarPlatformBridgeCustom {
    public ServerBossInfo vanilla$bar;
    private VanillaBossBarListener vanilla$listener;

    public final void paper$playerShow(final CraftPlayer player) {
        if (this.vanilla$bar == null) {
            final BossBar $this = (BossBar) this;
            this.vanilla$bar = new ServerBossInfo (
                    PaperAdventure.asVanilla($this.name()),
                    PaperAdventure.asVanilla($this.color()),
                    PaperAdventure.asVanilla($this.overlay())
            );
            this.vanilla$bar.adventure = $this;
            this.vanilla$listener = new VanillaBossBarListener(this.vanilla$bar::broadcast);
            $this.addListener(this.vanilla$listener);
        }
        this.vanilla$bar.addPlayer(player.getHandle());
    }

    public final void paper$playerHide(final CraftPlayer player) {
        if (this.vanilla$bar != null) {
            this.vanilla$bar.removePlayer(player.getHandle());
            if (this.vanilla$bar.getPlayers().isEmpty()) {
                ((BossBar) this).removeListener(this.vanilla$listener);
                this.vanilla$bar = null;
            }
        }
    }
}
