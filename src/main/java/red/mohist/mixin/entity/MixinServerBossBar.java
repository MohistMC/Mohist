package red.mohist.mixin.entity;

import com.google.common.collect.Sets;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import red.mohist.extra.entity.ExtraServerBossBar;

import java.util.Iterator;
import java.util.Set;

@Mixin(ServerBossBar.class)
public class MixinServerBossBar implements ExtraServerBossBar {

    public Text name;

    @Shadow
    private boolean visible;

    public BossBarS2CPacket.Type type;

    @Shadow
    @Final
    private Set<ServerPlayerEntity> players = Sets.newHashSet();

    @Shadow
    private void sendPacket(BossBarS2CPacket.Type type) {
        if (this.visible) {
            BossBarS2CPacket bossBarS2CPacket = new BossBarS2CPacket(type, ((ServerBossBar) (Object) this));
            Iterator var3 = this.players.iterator();

            while(var3.hasNext()) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)var3.next();
                serverPlayerEntity.networkHandler.sendPacket(bossBarS2CPacket);
            }
        }

    }

    @Override
    public void getsendPacket(BossBarS2CPacket.Type updateName) {
        this.sendPacket(type);
    }

    @Override
    public boolean getVisible() {
        return this.visible;
    }

}
