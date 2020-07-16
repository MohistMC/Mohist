package red.mohist.extra.entity;

import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;

public interface ExtraBossBar {

    Text getName();

    BossBar.Style getStyle();

    BossBar.Color getColor();
}
