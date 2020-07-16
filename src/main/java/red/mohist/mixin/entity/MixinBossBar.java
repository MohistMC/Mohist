package red.mohist.mixin.entity;

import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import red.mohist.extra.entity.ExtraBossBar;

@Mixin(BossBar.class)
public class MixinBossBar implements ExtraBossBar {

    @Shadow
    protected BossBar.Style style;

    @Shadow
    protected BossBar.Color color;

    @Shadow
    protected Text name;

    @Override
    public Text getName() {
        return this.name;
    }

    @Override
    public BossBar.Style getStyle() {
        return this.style;
    }

    @Override
    public BossBar.Color getColor() {
        return this.color;
    }
}
