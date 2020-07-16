package red.mohist.extra.entity;

import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;

public interface ExtraServerBossBar {

    void getsendPacket(BossBarS2CPacket.Type updateName);

    boolean getVisible();
}
