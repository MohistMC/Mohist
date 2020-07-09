package org.bukkit.craftbukkit.v1_15_R1.boss;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.world.BossInfo;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SUpdateBossInfoPacket;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;

public class CraftBossBar implements BossBar {

    private final ServerBossInfo handle;
    private Map<BarFlag, FlagContainer> flags;

    public CraftBossBar(String title, BarColor color, BarStyle style, BarFlag... flags) {
        handle = new ServerBossInfo(
                CraftChatMessage.fromString(title, true)[0],
                convertColor(color),
                convertStyle(style)
        );

        this.initialize();

        for (BarFlag flag : flags) {
            this.addFlag(flag);
        }

        this.setColor(color);
        this.setStyle(style);
    }

    public CraftBossBar(ServerBossInfo bossBattleServer) {
        this.handle = bossBattleServer;
        this.initialize();
    }

    private void initialize() {
        this.flags = new HashMap<>();
        this.flags.put(BarFlag.DARKEN_SKY, new FlagContainer(handle::shouldDarkenSky, handle::setDarkenSky));
        this.flags.put(BarFlag.PLAY_BOSS_MUSIC, new FlagContainer(handle::shouldPlayEndBossMusic, handle::setPlayEndBossMusic));
        this.flags.put(BarFlag.CREATE_FOG, new FlagContainer(handle::shouldCreateFog, handle::setCreateFog));
    }

    private BarColor convertColor(BossInfo.Color color) {
        BarColor bukkitColor = BarColor.valueOf(color.name());
        return (bukkitColor == null) ? BarColor.WHITE : bukkitColor;
    }

    private BossInfo.Color convertColor(BarColor color) {
        BossInfo.Color nmsColor = BossInfo.Color.valueOf(color.name());
        return (nmsColor == null) ? BossInfo.Color.WHITE : nmsColor;
    }

    private BossInfo.Overlay convertStyle(BarStyle style) {
        switch (style) {
            default:
            case SOLID:
                return BossInfo.Overlay.PROGRESS;
            case SEGMENTED_6:
                return BossInfo.Overlay.NOTCHED_6;
            case SEGMENTED_10:
                return BossInfo.Overlay.NOTCHED_10;
            case SEGMENTED_12:
                return BossInfo.Overlay.NOTCHED_12;
            case SEGMENTED_20:
                return BossInfo.Overlay.NOTCHED_20;
        }
    }

    private BarStyle convertStyle(BossInfo.Overlay style) {
        switch (style) {
            default:
            case PROGRESS:
                return BarStyle.SOLID;
            case NOTCHED_6:
                return BarStyle.SEGMENTED_6;
            case NOTCHED_10:
                return BarStyle.SEGMENTED_10;
            case NOTCHED_12:
                return BarStyle.SEGMENTED_12;
            case NOTCHED_20:
                return BarStyle.SEGMENTED_20;
        }
    }

    @Override
    public String getTitle() {
        return CraftChatMessage.fromComponent(handle.name);
    }

    @Override
    public void setTitle(String title) {
        handle.name = CraftChatMessage.fromString(title, true)[0];
        handle.sendUpdate(SUpdateBossInfoPacket.Operation.UPDATE_NAME);
    }

    @Override
    public BarColor getColor() {
        return convertColor(handle.color);
    }

    @Override
    public void setColor(BarColor color) {
        handle.color = convertColor(color);
        handle.sendUpdate(SUpdateBossInfoPacket.Operation.UPDATE_STYLE);
    }

    @Override
    public BarStyle getStyle() {
        return convertStyle(handle.overlay);
    }

    @Override
    public void setStyle(BarStyle style) {
        handle.overlay = convertStyle(style);
        handle.sendUpdate(SUpdateBossInfoPacket.Operation.UPDATE_STYLE);
    }

    @Override
    public void addFlag(BarFlag flag) {
        FlagContainer flagContainer = flags.get(flag);
        if (flagContainer != null) {
            flagContainer.set.accept(true);
        }
    }

    @Override
    public void removeFlag(BarFlag flag) {
        FlagContainer flagContainer = flags.get(flag);
        if (flagContainer != null) {
            flagContainer.set.accept(false);
        }
    }

    @Override
    public boolean hasFlag(BarFlag flag) {
        FlagContainer flagContainer = flags.get(flag);
        if (flagContainer != null) {
            return flagContainer.get.get();
        }
        return false;
    }

    @Override
    public void setProgress(double progress) {
        Preconditions.checkArgument(progress >= 0.0 && progress <= 1.0, "Progress must be between 0.0 and 1.0 (%s)", progress);
        handle.setPercent((float) progress);
    }

    @Override
    public double getProgress() {
        return handle.getPercent();
    }

    @Override
    public void addPlayer(Player player) {
        Preconditions.checkArgument(player != null, "player == null");
        Preconditions.checkArgument(((CraftPlayer) player).getHandle().connection != null, "player is not fully connected (wait for PlayerJoinEvent)");

        handle.addPlayer(((CraftPlayer) player).getHandle());
    }

    @Override
    public void removePlayer(Player player) {
        Preconditions.checkArgument(player != null, "player == null");

        handle.removePlayer(((CraftPlayer) player).getHandle());
    }

    @Override
    public List<Player> getPlayers() {
        ImmutableList.Builder<Player> players = ImmutableList.builder();
        for (ServerPlayerEntity p : handle.getPlayers()) {
            players.add(p.getBukkitEntity());
        }
        return players.build();
    }

    @Override
    public void setVisible(boolean visible) {
        handle.setVisible(visible);
    }

    @Override
    public boolean isVisible() {
        return handle.visible;
    }

    @Override
    public void show() {
        handle.setVisible(true);
    }

    @Override
    public void hide() {
        handle.setVisible(false);
    }

    @Override
    public void removeAll() {
        for (Player player : getPlayers()) {
            removePlayer(player);
        }
    }

    private class FlagContainer {

        private Supplier<Boolean> get;
        private Consumer<Boolean> set;

        private FlagContainer(Supplier<Boolean> get, Consumer<Boolean> set) {
            this.get = get;
            this.set = set;
        }
    }

    public ServerBossInfo getHandle() {
        return handle;
    }
}
