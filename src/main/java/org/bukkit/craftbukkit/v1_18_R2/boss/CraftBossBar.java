package org.bukkit.craftbukkit.v1_18_R2.boss;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftChatMessage;
import org.bukkit.entity.Player;

public class CraftBossBar implements BossBar {

    private final ServerBossEvent handle;
    private Map<BarFlag, FlagContainer> flags;

    public CraftBossBar(String title, BarColor color, BarStyle style, BarFlag... flags) {
        handle = new ServerBossEvent(
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

    public CraftBossBar(ServerBossEvent bossBattleServer) {
        this.handle = bossBattleServer;
        this.initialize();
    }

    private void initialize() {
        this.flags = new HashMap<>();
        this.flags.put(BarFlag.DARKEN_SKY, new FlagContainer(handle::shouldDarkenScreen, handle::setDarkenScreen));
        this.flags.put(BarFlag.PLAY_BOSS_MUSIC, new FlagContainer(handle::shouldPlayBossMusic, handle::setPlayBossMusic));
        this.flags.put(BarFlag.CREATE_FOG, new FlagContainer(handle::shouldCreateWorldFog, handle::setCreateWorldFog));
    }

    private BarColor convertColor(BossEvent.BossBarColor color) {
        BarColor bukkitColor = BarColor.valueOf(color.name());
        return (bukkitColor == null) ? BarColor.WHITE : bukkitColor;
    }

    private BossEvent.BossBarColor convertColor(BarColor color) {
        BossEvent.BossBarColor nmsColor = BossEvent.BossBarColor.valueOf(color.name());
        return (nmsColor == null) ? BossEvent.BossBarColor.WHITE : nmsColor;
    }

    private BossEvent.BossBarOverlay convertStyle(BarStyle style) {
        switch (style) {
            default:
            case SOLID:
                return BossEvent.BossBarOverlay.PROGRESS;
            case SEGMENTED_6:
                return BossEvent.BossBarOverlay.NOTCHED_6;
            case SEGMENTED_10:
                return BossEvent.BossBarOverlay.NOTCHED_10;
            case SEGMENTED_12:
                return BossEvent.BossBarOverlay.NOTCHED_12;
            case SEGMENTED_20:
                return BossEvent.BossBarOverlay.NOTCHED_20;
        }
    }

    private BarStyle convertStyle(BossEvent.BossBarOverlay style) {
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
        handle.broadcast(ClientboundBossEventPacket::createUpdateNamePacket);
    }

    @Override
    public BarColor getColor() {
        return convertColor(handle.color);
    }

    @Override
    public void setColor(BarColor color) {
        handle.color = convertColor(color);
        handle.broadcast(ClientboundBossEventPacket::createUpdateStylePacket);
    }

    @Override
    public BarStyle getStyle() {
        return convertStyle(handle.overlay);
    }

    @Override
    public void setStyle(BarStyle style) {
        handle.overlay = convertStyle(style);
        handle.broadcast(ClientboundBossEventPacket::createUpdateStylePacket);
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
        handle.setProgress((float) progress);
    }

    @Override
    public double getProgress() {
        return handle.getProgress();
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
        for (ServerPlayer p : handle.getPlayers()) {
            players.add((Player) p.getBukkitEntity());
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

    private final class FlagContainer {

        private Supplier<Boolean> get;
        private Consumer<Boolean> set;

        private FlagContainer(Supplier<Boolean> get, Consumer<Boolean> set) {
            this.get = get;
            this.set = set;
        }
    }

    public ServerBossEvent getHandle() {
        return handle;
    }
}
