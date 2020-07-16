package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.BaseEncoding;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeInstanceImpl;
import net.minecraft.item.map.MapIcon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityAttributesS2CPacket;
import net.minecraft.network.packet.s2c.play.ExperienceBarUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.MapUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerSpawnPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldEventS2CPacket;
import net.minecraft.server.WhitelistEntry;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.Validate;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.WeatherType;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.bukkit.craftbukkit.CraftEffect;
import org.bukkit.craftbukkit.CraftOfflinePlayer;
import org.bukkit.craftbukkit.CraftParticle;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.craftbukkit.CraftStatistic;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.advancement.CraftAdvancement;
import org.bukkit.craftbukkit.advancement.CraftAdvancementProgress;
import org.bukkit.craftbukkit.block.CraftSign;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.conversations.ConversationTracker;
import org.bukkit.craftbukkit.map.CraftMapView;
import org.bukkit.craftbukkit.map.RenderData;
import org.bukkit.craftbukkit.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.scoreboard.Scoreboard;

@DelegateDeserialization(CraftOfflinePlayer.class)
public class CraftPlayer extends CraftHumanEntity implements Player {
    private static final WeakHashMap<Plugin, WeakReference<Plugin>> pluginWeakReferences = new WeakHashMap<>();
    private final ConversationTracker conversationTracker = new ConversationTracker();
    private final Set<String> channels = new HashSet<String>();
    private final Map<UUID, Set<WeakReference<Plugin>>> hiddenPlayers = new HashMap<>();
    private long firstPlayed = 0;
    private long lastPlayed = 0;
    private boolean hasPlayedBefore = false;
    private int hash = 0;
    private double health = 20;
    private boolean scaledHealth = false;
    private double healthScale = 20;
    private Text playerListHeader;
    private Text playerListFooter;

    public CraftPlayer(CraftServer server, ServerPlayerEntity entity) {
        super(server, entity);

        firstPlayed = System.currentTimeMillis();
    }

    @Nullable
    private static WeakReference<Plugin> getPluginWeakReference(@Nullable Plugin plugin) {
        return (plugin == null) ? null : pluginWeakReferences.computeIfAbsent(plugin, WeakReference::new);
    }

    public GameProfile getProfile() {
        return getHandle().getGameProfile();
    }

    @Override
    public boolean isOp() {
        return server.getHandle().isOperator(getProfile());
    }

    @Override
    public void setOp(boolean value) {
        if (value == isOp()) return;

        if (value) {
            server.getHandle().addToOperators(getProfile());
        } else {
            server.getHandle().removeFromOperators(getProfile());
        }

        perm.recalculatePermissions();
    }

    @Override
    public boolean isOnline() {
        return server.getPlayer(getUniqueId()) != null;
    }

    @Override
    public InetSocketAddress getAddress() {
        if (getHandle().networkHandler == null) return null;

        SocketAddress addr = getHandle().networkHandler.connection.getAddress();
        if (addr instanceof InetSocketAddress) {
            return (InetSocketAddress) addr;
        } else {
            return null;
        }
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        if (ignorePose) {
            return 1.62D;
        } else {
            return getEyeHeight();
        }
    }

    @Override
    public void sendRawMessage(String message) {
        if (getHandle().networkHandler == null) return;

        for (Text component : CraftChatMessage.fromString(message)) {
            getHandle().networkHandler.sendPacket(new ChatMessageS2CPacket(component));
        }
    }

    @Override
    public void sendMessage(String message) {
        if (!conversationTracker.isConversingModaly()) {
            this.sendRawMessage(message);
        }
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    @Override
    public String getDisplayName() {
        return getHandle().displayName;
    }

    @Override
    public void setDisplayName(final String name) {
        getHandle().displayName = name == null ? getName() : name;
    }

    @Override
    public String getPlayerListName() {
        return getHandle().listName == null ? getName() : CraftChatMessage.fromComponent(getHandle().listName, Formatting.WHITE);
    }

    @Override
    public void setPlayerListName(String name) {
        if (name == null) {
            name = getName();
        }
        getHandle().listName = name.equals(getName()) ? null : CraftChatMessage.fromStringOrNull(name);
        for (ServerPlayerEntity player : (List<ServerPlayerEntity>) server.getHandle().players) {
            if (player.getBukkitEntity().canSee(this)) {
                player.networkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, getHandle()));
            }
        }
    }

    @Override
    public String getPlayerListHeader() {
        return (playerListHeader == null) ? null : CraftChatMessage.fromComponent(playerListHeader);
    }

    @Override
    public void setPlayerListHeader(String header) {
        this.playerListHeader = CraftChatMessage.fromStringOrNull(header, true);
        updatePlayerListHeaderFooter();
    }

    @Override
    public String getPlayerListFooter() {
        return (playerListFooter == null) ? null : CraftChatMessage.fromComponent(playerListFooter);
    }

    @Override
    public void setPlayerListFooter(String footer) {
        this.playerListFooter = CraftChatMessage.fromStringOrNull(footer, true);
        updatePlayerListHeaderFooter();
    }

    @Override
    public void setPlayerListHeaderFooter(String header, String footer) {
        this.playerListHeader = CraftChatMessage.fromStringOrNull(header, true);
        this.playerListFooter = CraftChatMessage.fromStringOrNull(footer, true);
        updatePlayerListHeaderFooter();
    }

    private void updatePlayerListHeaderFooter() {
        if (getHandle().networkHandler == null) return;

        PlayerListHeaderS2CPacket packet = new PlayerListHeaderS2CPacket();
        packet.header = (this.playerListHeader == null) ? new LiteralText("") : this.playerListHeader;
        packet.footer = (this.playerListFooter == null) ? new LiteralText("") : this.playerListFooter;
        getHandle().networkHandler.sendPacket(packet);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OfflinePlayer)) {
            return false;
        }
        OfflinePlayer other = (OfflinePlayer) obj;
        if ((this.getUniqueId() == null) || (other.getUniqueId() == null)) {
            return false;
        }

        boolean uuidEquals = this.getUniqueId().equals(other.getUniqueId());
        boolean idEquals = true;

        if (other instanceof CraftPlayer) {
            idEquals = this.getEntityId() == ((CraftPlayer) other).getEntityId();
        }

        return uuidEquals && idEquals;
    }

    @Override
    public void kickPlayer(String message) {
        if (getHandle().networkHandler == null) return;

        getHandle().networkHandler.disconnect(message == null ? "" : message);
    }

    @Override
    public Location getCompassTarget() {
        return getHandle().compassTarget;
    }

    @Override
    public void setCompassTarget(Location loc) {
        if (getHandle().networkHandler == null) return;

        // Do not directly assign here, from the packethandler we'll assign it.
        getHandle().networkHandler.sendPacket(new PlayerSpawnPositionS2CPacket(new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())));
    }

    @Override
    public void chat(String msg) {
        if (getHandle().networkHandler == null) return;

        getHandle().networkHandler.chat(msg, false);
    }

    @Override
    public boolean performCommand(String command) {
        return server.dispatchCommand(this, command);
    }

    @Override
    public void playNote(Location loc, byte instrument, byte note) {
        if (getHandle().networkHandler == null) return;

        String instrumentName = null;
        switch (instrument) {
            case 0:
                instrumentName = "harp";
                break;
            case 1:
                instrumentName = "basedrum";
                break;
            case 2:
                instrumentName = "snare";
                break;
            case 3:
                instrumentName = "hat";
                break;
            case 4:
                instrumentName = "bass";
                break;
            case 5:
                instrumentName = "flute";
                break;
            case 6:
                instrumentName = "bell";
                break;
            case 7:
                instrumentName = "guitar";
                break;
            case 8:
                instrumentName = "chime";
                break;
            case 9:
                instrumentName = "xylophone";
                break;
        }

        float f = (float) Math.pow(2.0D, (note - 12.0D) / 12.0D);
        getHandle().networkHandler.sendPacket(new PlaySoundS2CPacket(CraftSound.getSoundEffect("block.note_block." + instrumentName), net.minecraft.sound.SoundCategory.RECORDS, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 3.0f, f));
    }

    @Override
    public void playNote(Location loc, Instrument instrument, Note note) {
        if (getHandle().networkHandler == null) return;

        String instrumentName = null;
        switch (instrument.ordinal()) {
            case 0:
                instrumentName = "harp";
                break;
            case 1:
                instrumentName = "basedrum";
                break;
            case 2:
                instrumentName = "snare";
                break;
            case 3:
                instrumentName = "hat";
                break;
            case 4:
                instrumentName = "bass";
                break;
            case 5:
                instrumentName = "flute";
                break;
            case 6:
                instrumentName = "bell";
                break;
            case 7:
                instrumentName = "guitar";
                break;
            case 8:
                instrumentName = "chime";
                break;
            case 9:
                instrumentName = "xylophone";
                break;
            case 10:
                instrumentName = "iron_xylophone";
                break;
            case 11:
                instrumentName = "cow_bell";
                break;
            case 12:
                instrumentName = "didgeridoo";
                break;
            case 13:
                instrumentName = "bit";
                break;
            case 14:
                instrumentName = "banjo";
                break;
            case 15:
                instrumentName = "pling";
                break;
            case 16:
                instrumentName = "xylophone";
                break;
        }
        float f = (float) Math.pow(2.0D, (note.getId() - 12.0D) / 12.0D);
        getHandle().networkHandler.sendPacket(new PlaySoundS2CPacket(CraftSound.getSoundEffect("block.note_block." + instrumentName), net.minecraft.sound.SoundCategory.RECORDS, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 3.0f, f));
    }

    @Override
    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        playSound(loc, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, String sound, float volume, float pitch) {
        playSound(loc, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, Sound sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        if (loc == null || sound == null || category == null || getHandle().networkHandler == null) return;

        PlaySoundS2CPacket packet = new PlaySoundS2CPacket(CraftSound.getSoundEffect(CraftSound.getSound(sound)), net.minecraft.sound.SoundCategory.valueOf(category.name()), loc.getX(), loc.getY(), loc.getZ(), volume, pitch);
        getHandle().networkHandler.sendPacket(packet);
    }

    @Override
    public void playSound(Location loc, String sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        if (loc == null || sound == null || category == null || getHandle().networkHandler == null) return;

        PlaySoundIdS2CPacket packet = new PlaySoundIdS2CPacket(new Identifier(sound), net.minecraft.sound.SoundCategory.valueOf(category.name()), new Vec3d(loc.getX(), loc.getY(), loc.getZ()), volume, pitch);
        getHandle().networkHandler.sendPacket(packet);
    }

    @Override
    public void stopSound(Sound sound) {
        stopSound(sound, null);
    }

    @Override
    public void stopSound(String sound) {
        stopSound(sound, null);
    }

    @Override
    public void stopSound(Sound sound, org.bukkit.SoundCategory category) {
        stopSound(CraftSound.getSound(sound), category);
    }

    @Override
    public void stopSound(String sound, org.bukkit.SoundCategory category) {
        if (getHandle().networkHandler == null) return;

        getHandle().networkHandler.sendPacket(new StopSoundS2CPacket(new Identifier(sound), category == null ? net.minecraft.sound.SoundCategory.MASTER : net.minecraft.sound.SoundCategory.valueOf(category.name())));
    }

    @Override
    public void playEffect(Location loc, Effect effect, int data) {
        if (getHandle().networkHandler == null) return;

        int packetData = effect.getId();
        WorldEventS2CPacket packet = new WorldEventS2CPacket(packetData, new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), data, false);
        getHandle().networkHandler.sendPacket(packet);
    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data) {
        if (data != null) {
            Validate.isTrue(effect.getData() != null && effect.getData().isAssignableFrom(data.getClass()), "Wrong kind of data for this effect!");
        } else {
            Validate.isTrue(effect.getData() == null, "Wrong kind of data for this effect!");
        }

        int datavalue = data == null ? 0 : CraftEffect.getDataValue(effect, data);
        playEffect(loc, effect, datavalue);
    }

    @Override
    public void sendBlockChange(Location loc, Material material, byte data) {
        if (getHandle().networkHandler == null) return;

        BlockUpdateS2CPacket packet = new BlockUpdateS2CPacket(((CraftWorld) loc.getWorld()).getHandle(), new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));

        packet.state = CraftMagicNumbers.getBlock(material, data);
        getHandle().networkHandler.sendPacket(packet);
    }

    @Override
    public void sendBlockChange(Location loc, BlockData block) {
        if (getHandle().networkHandler == null) return;

        BlockUpdateS2CPacket packet = new BlockUpdateS2CPacket(((CraftWorld) loc.getWorld()).getHandle(), new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));

        packet.state = ((CraftBlockData) block).getState();
        getHandle().networkHandler.sendPacket(packet);
    }

    @Override
    public void sendSignChange(Location loc, String[] lines) {
        sendSignChange(loc, lines, DyeColor.BLACK);
    }

    @Override
    public void sendSignChange(Location loc, String[] lines, DyeColor dyeColor) {
        if (getHandle().networkHandler == null) {
            return;
        }

        if (lines == null) {
            lines = new String[4];
        }

        Validate.notNull(loc, "Location can not be null");
        Validate.notNull(dyeColor, "DyeColor can not be null");
        if (lines.length < 4) {
            throw new IllegalArgumentException("Must have at least 4 lines");
        }

        Text[] components = CraftSign.sanitizeLines(lines);
        SignBlockEntity sign = new SignBlockEntity();
        sign.setPos(new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        sign.setTextColor(net.minecraft.util.DyeColor.byId(dyeColor.getWoolData()));
        System.arraycopy(components, 0, sign.text, 0, sign.text.length);

        getHandle().networkHandler.sendPacket(sign.toUpdatePacket());
    }

    @Override
    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        if (getHandle().networkHandler == null) return false;

        /*
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        int cx = x >> 4;
        int cz = z >> 4;

        if (sx <= 0 || sy <= 0 || sz <= 0) {
            return false;
        }

        if ((x + sx - 1) >> 4 != cx || (z + sz - 1) >> 4 != cz || y < 0 || y + sy > 128) {
            return false;
        }

        if (data.length != (sx * sy * sz * 5) / 2) {
            return false;
        }

        Packet51MapChunk packet = new Packet51MapChunk(x, y, z, sx, sy, sz, data);

        getHandle().playerConnection.sendPacket(packet);

        return true;
        */

        throw new NotImplementedException("Chunk changes do not yet work"); // TODO: Chunk changes.
    }

    @Override
    public void sendMap(MapView map) {
        if (getHandle().networkHandler == null) return;

        RenderData data = ((CraftMapView) map).render(this);
        Collection<MapIcon> icons = new ArrayList<MapIcon>();
        for (MapCursor cursor : data.cursors) {
            if (cursor.isVisible()) {
                icons.add(new MapIcon(MapIcon.Type.byId(cursor.getRawType()), cursor.getX(), cursor.getY(), cursor.getDirection(), CraftChatMessage.fromStringOrNull(cursor.getCaption())));
            }
        }

        MapUpdateS2CPacket packet = new MapUpdateS2CPacket(map.getId(), map.getScale().getValue(), true, map.isLocked(), icons, data.buffer, 0, 0, 128, 128);
        getHandle().networkHandler.sendPacket(packet);
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        throw new UnsupportedOperationException("Cannot set rotation of players. Consider teleporting instead.");
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        Preconditions.checkArgument(location != null, "location");
        Preconditions.checkArgument(location.getWorld() != null, "location.world");
        location.checkFinite();

        ServerPlayerEntity entity = getHandle();

        if (getHealth() == 0 || entity.removed) {
            return false;
        }

        if (entity.networkHandler == null) {
            return false;
        }

        if (entity.hasPassengers()) {
            return false;
        }

        // From = Players current Location
        Location from = this.getLocation();
        // To = Players new Location if Teleport is Successful
        Location to = location;
        // Create & Call the Teleport Event.
        PlayerTeleportEvent event = new PlayerTeleportEvent(this, from, to, cause);
        server.getPluginManager().callEvent(event);

        // Return False to inform the Plugin that the Teleport was unsuccessful/cancelled.
        if (event.isCancelled()) {
            return false;
        }

        // If this player is riding another entity, we must dismount before teleporting.
        entity.stopRiding();

        // SPIGOT-5509: Wakeup, similar to riding
        if (this.isSleeping()) {
            this.wakeup(false);
        }

        // Update the From Location
        from = event.getFrom();
        // Grab the new To Location dependent on whether the event was cancelled.
        to = event.getTo();
        // Grab the To and From World Handles.
        ServerWorld fromWorld = ((CraftWorld) from.getWorld()).getHandle();
        ServerWorld toWorld = ((CraftWorld) to.getWorld()).getHandle();

        // Close any foreign inventory
        if (getHandle().container != getHandle().playerContainer) {
            getHandle().closeContainer();
        }

        // Check if the fromWorld and toWorld are the same.
        if (fromWorld == toWorld) {
            entity.networkHandler.teleport(to);
        } else {
            server.getHandle().moveToWorld(entity, toWorld.getDimension().getType(), true, to, true);
        }
        return true;
    }

    @Override
    public boolean isSneaking() {
        return getHandle().isSneaking();
    }

    @Override
    public void setSneaking(boolean sneak) {
        getHandle().setSneaking(sneak);
    }

    @Override
    public boolean isSprinting() {
        return getHandle().isSprinting();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        getHandle().setSprinting(sprinting);
    }

    @Override
    public void loadData() {
        server.getHandle().saveHandler.loadPlayerData(getHandle());
    }

    @Override
    public void saveData() {
        server.getHandle().saveHandler.savePlayerData(getHandle());
    }

    @Deprecated
    @Override
    public void updateInventory() {
        getHandle().openContainer(getHandle().container);
    }

    @Override
    public boolean isSleepingIgnored() {
        return getHandle().fauxSleeping;
    }

    @Override
    public void setSleepingIgnored(boolean isSleeping) {
        getHandle().fauxSleeping = isSleeping;
        ((CraftWorld) getWorld()).getHandle().updatePlayersSleeping();
    }

    @Override
    public void incrementStatistic(Statistic statistic) {
        CraftStatistic.incrementStatistic(getHandle().getStatHandler(), statistic);
    }

    @Override
    public void decrementStatistic(Statistic statistic) {
        CraftStatistic.decrementStatistic(getHandle().getStatHandler(), statistic);
    }

    @Override
    public int getStatistic(Statistic statistic) {
        return CraftStatistic.getStatistic(getHandle().getStatHandler(), statistic);
    }

    @Override
    public void incrementStatistic(Statistic statistic, int amount) {
        CraftStatistic.incrementStatistic(getHandle().getStatHandler(), statistic, amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, int amount) {
        CraftStatistic.decrementStatistic(getHandle().getStatHandler(), statistic, amount);
    }

    @Override
    public void setStatistic(Statistic statistic, int newValue) {
        CraftStatistic.setStatistic(getHandle().getStatHandler(), statistic, newValue);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material) {
        CraftStatistic.incrementStatistic(getHandle().getStatHandler(), statistic, material);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material) {
        CraftStatistic.decrementStatistic(getHandle().getStatHandler(), statistic, material);
    }

    @Override
    public int getStatistic(Statistic statistic, Material material) {
        return CraftStatistic.getStatistic(getHandle().getStatHandler(), statistic, material);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material, int amount) {
        CraftStatistic.incrementStatistic(getHandle().getStatHandler(), statistic, material, amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material, int amount) {
        CraftStatistic.decrementStatistic(getHandle().getStatHandler(), statistic, material, amount);
    }

    @Override
    public void setStatistic(Statistic statistic, Material material, int newValue) {
        CraftStatistic.setStatistic(getHandle().getStatHandler(), statistic, material, newValue);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType) {
        CraftStatistic.incrementStatistic(getHandle().getStatHandler(), statistic, entityType);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType) {
        CraftStatistic.decrementStatistic(getHandle().getStatHandler(), statistic, entityType);
    }

    @Override
    public int getStatistic(Statistic statistic, EntityType entityType) {
        return CraftStatistic.getStatistic(getHandle().getStatHandler(), statistic, entityType);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        CraftStatistic.incrementStatistic(getHandle().getStatHandler(), statistic, entityType, amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        CraftStatistic.decrementStatistic(getHandle().getStatHandler(), statistic, entityType, amount);
    }

    @Override
    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
        CraftStatistic.setStatistic(getHandle().getStatHandler(), statistic, entityType, newValue);
    }

    @Override
    public void setPlayerTime(long time, boolean relative) {
        getHandle().timeOffset = time;
        getHandle().relativeTime = relative;
    }

    @Override
    public long getPlayerTimeOffset() {
        return getHandle().timeOffset;
    }

    @Override
    public long getPlayerTime() {
        return getHandle().getPlayerTime();
    }

    @Override
    public boolean isPlayerTimeRelative() {
        return getHandle().relativeTime;
    }

    @Override
    public void resetPlayerTime() {
        setPlayerTime(0, true);
    }

    @Override
    public WeatherType getPlayerWeather() {
        return getHandle().getPlayerWeather();
    }

    @Override
    public void setPlayerWeather(WeatherType type) {
        getHandle().setPlayerWeather(type, true);
    }

    @Override
    public void resetPlayerWeather() {
        getHandle().resetPlayerWeather();
    }

    @Override
    public boolean isBanned() {
        return server.getBanList(BanList.Type.NAME).isBanned(getName());
    }

    @Override
    public boolean isWhitelisted() {
        return server.getHandle().getWhitelist().isAllowed(getProfile());
    }

    @Override
    public void setWhitelisted(boolean value) {
        if (value) {
            server.getHandle().getWhitelist().add(new WhitelistEntry(getProfile()));
        } else {
            server.getHandle().getWhitelist().remove(getProfile());
        }
    }

    @Override
    public GameMode getGameMode() {
        return GameMode.getByValue(getHandle().interactionManager.getGameMode().getId());
    }

    @Override
    public void setGameMode(GameMode mode) {
        if (getHandle().networkHandler == null) return;

        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null");
        }

        getHandle().setGameMode(net.minecraft.world.GameMode.byId(mode.getValue()));
    }

    @Override
    public void giveExp(int exp) {
        getHandle().addExperience(exp);
    }

    @Override
    public void giveExpLevels(int levels) {
        getHandle().addExperienceLevels(levels);
    }

    @Override
    public float getExp() {
        return getHandle().experienceProgress;
    }

    @Override
    public void setExp(float exp) {
        Preconditions.checkArgument(exp >= 0.0 && exp <= 1.0, "Experience progress must be between 0.0 and 1.0 (%s)", exp);
        getHandle().experienceProgress = exp;
        getHandle().syncedExperience = -1;
    }

    @Override
    public int getLevel() {
        return getHandle().experienceLevel;
    }

    @Override
    public void setLevel(int level) {
        Preconditions.checkArgument(level >= 0, "Experience level must not be negative (%s)", level);
        getHandle().experienceLevel = level;
        getHandle().syncedExperience = -1;
    }

    @Override
    public int getTotalExperience() {
        return getHandle().totalExperience;
    }

    @Override
    public void setTotalExperience(int exp) {
        Preconditions.checkArgument(exp >= 0, "Total experience points must not be negative (%s)", exp);
        getHandle().totalExperience = exp;
    }

    @Override
    public void sendExperienceChange(float progress) {
        sendExperienceChange(progress, getLevel());
    }

    @Override
    public void sendExperienceChange(float progress, int level) {
        Preconditions.checkArgument(progress >= 0.0 && progress <= 1.0, "Experience progress must be between 0.0 and 1.0 (%s)", progress);
        Preconditions.checkArgument(level >= 0, "Experience level must not be negative (%s)", level);

        if (getHandle().networkHandler == null) {
            return;
        }

        ExperienceBarUpdateS2CPacket packet = new ExperienceBarUpdateS2CPacket(progress, getTotalExperience(), level);
        getHandle().networkHandler.sendPacket(packet);
    }

    @Override
    public float getExhaustion() {
        return getHandle().getHungerManager().exhaustion;
    }

    @Override
    public void setExhaustion(float value) {
        getHandle().getHungerManager().exhaustion = value;
    }

    @Override
    public float getSaturation() {
        return getHandle().getHungerManager().foodSaturationLevel;
    }

    @Override
    public void setSaturation(float value) {
        getHandle().getHungerManager().foodSaturationLevel = value;
    }

    @Override
    public int getFoodLevel() {
        return getHandle().getHungerManager().foodLevel;
    }

    @Override
    public void setFoodLevel(int value) {
        getHandle().getHungerManager().foodLevel = value;
    }

    @Override
    @Deprecated
    public void hidePlayer(Player player) {
        hidePlayer0(null, player);
    }

    @Override
    public void hidePlayer(Plugin plugin, Player player) {
        Validate.notNull(plugin, "Plugin cannot be null");
        Validate.isTrue(plugin.isEnabled(), "Plugin attempted to hide player while disabled");

        hidePlayer0(plugin, player);
    }

    private void hidePlayer0(@Nullable Plugin plugin, Player player) {
        Validate.notNull(player, "hidden player cannot be null");
        if (getHandle().networkHandler == null) return;
        if (equals(player)) return;

        Set<WeakReference<Plugin>> hidingPlugins = hiddenPlayers.get(player.getUniqueId());
        if (hidingPlugins != null) {
            // Some plugins are already hiding the player. Just mark that this
            // plugin wants the player hidden too and end.
            hidingPlugins.add(getPluginWeakReference(plugin));
            return;
        }
        hidingPlugins = new HashSet<>();
        hidingPlugins.add(getPluginWeakReference(plugin));
        hiddenPlayers.put(player.getUniqueId(), hidingPlugins);

        // Remove this player from the hidden player's EntityTrackerEntry
        ThreadedAnvilChunkStorage tracker = ((ServerWorld) entity.world).getChunkManager().threadedAnvilChunkStorage;
        ServerPlayerEntity other = ((CraftPlayer) player).getHandle();
        ThreadedAnvilChunkStorage.EntityTracker entry = tracker.entityTrackers.get(other.getEntityId());
        if (entry != null) {
            entry.stopTracking(getHandle());
        }

        // Remove the hidden player from this player user list, if they're on it
        if (other.sentListPacket) {
            getHandle().networkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.REMOVE_PLAYER, other));
        }
    }

    @Override
    @Deprecated
    public void showPlayer(Player player) {
        showPlayer0(null, player);
    }

    @Override
    public void showPlayer(Plugin plugin, Player player) {
        Validate.notNull(plugin, "Plugin cannot be null");
        // Don't require that plugin be enabled. A plugin must be allowed to call
        // showPlayer during its onDisable() method.
        showPlayer0(plugin, player);
    }

    private void showPlayer0(@Nullable Plugin plugin, Player player) {
        Validate.notNull(player, "shown player cannot be null");
        if (getHandle().networkHandler == null) return;
        if (equals(player)) return;

        Set<WeakReference<Plugin>> hidingPlugins = hiddenPlayers.get(player.getUniqueId());
        if (hidingPlugins == null) {
            return; // Player isn't hidden
        }
        hidingPlugins.remove(getPluginWeakReference(plugin));
        if (!hidingPlugins.isEmpty()) {
            return; // Some other plugins still want the player hidden
        }
        hiddenPlayers.remove(player.getUniqueId());

        ThreadedAnvilChunkStorage tracker = ((ServerWorld) entity.world).getChunkManager().threadedAnvilChunkStorage;
        ServerPlayerEntity other = ((CraftPlayer) player).getHandle();

        getHandle().networkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.ADD_PLAYER, other));

        ThreadedAnvilChunkStorage.EntityTracker entry = tracker.entityTrackers.get(other.getEntityId());
        if (entry != null && !entry.playersTracking.contains(getHandle())) {
            entry.updateCameraPosition(getHandle());
        }
    }

    public void removeDisconnectingPlayer(Player player) {
        hiddenPlayers.remove(player.getUniqueId());
    }

    @Override
    public boolean canSee(Player player) {
        return !hiddenPlayers.containsKey(player.getUniqueId());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        result.put("name", getName());

        return result;
    }

    @Override
    public Player getPlayer() {
        return this;
    }

    @Override
    public ServerPlayerEntity getHandle() {
        return (ServerPlayerEntity) entity;
    }

    public void setHandle(final ServerPlayerEntity entity) {
        super.setHandle(entity);
    }

    @Override
    public String toString() {
        return "CraftPlayer{" + "name=" + getName() + '}';
    }

    @Override
    public int hashCode() {
        if (hash == 0 || hash == 485) {
            hash = 97 * 5 + (this.getUniqueId() != null ? this.getUniqueId().hashCode() : 0);
        }
        return hash;
    }

    @Override
    public long getFirstPlayed() {
        return firstPlayed;
    }

    public void setFirstPlayed(long firstPlayed) {
        this.firstPlayed = firstPlayed;
    }

    @Override
    public long getLastPlayed() {
        return lastPlayed;
    }

    @Override
    public boolean hasPlayedBefore() {
        return hasPlayedBefore;
    }

    public void readExtraData(CompoundTag nbttagcompound) {
        hasPlayedBefore = true;
        if (nbttagcompound.contains("bukkit")) {
            CompoundTag data = nbttagcompound.getCompound("bukkit");

            if (data.contains("firstPlayed")) {
                firstPlayed = data.getLong("firstPlayed");
                lastPlayed = data.getLong("lastPlayed");
            }

            if (data.contains("newExp")) {
                ServerPlayerEntity handle = getHandle();
                handle.newExp = data.getInt("newExp");
                handle.newTotalExp = data.getInt("newTotalExp");
                handle.newLevel = data.getInt("newLevel");
                handle.expToDrop = data.getInt("expToDrop");
                handle.keepLevel = data.getBoolean("keepLevel");
            }
        }
    }

    public void setExtraData(CompoundTag nbttagcompound) {
        if (!nbttagcompound.contains("bukkit")) {
            nbttagcompound.put("bukkit", new CompoundTag());
        }

        CompoundTag data = nbttagcompound.getCompound("bukkit");
        ServerPlayerEntity handle = getHandle();
        data.putInt("newExp", handle.newExp);
        data.putInt("newTotalExp", handle.newTotalExp);
        data.putInt("newLevel", handle.newLevel);
        data.putInt("expToDrop", handle.expToDrop);
        data.putBoolean("keepLevel", handle.keepLevel);
        data.putLong("firstPlayed", getFirstPlayed());
        data.putLong("lastPlayed", System.currentTimeMillis());
        data.putString("lastKnownName", handle.getEntityName());
    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        return conversationTracker.beginConversation(conversation);
    }

    @Override
    public void abandonConversation(Conversation conversation) {
        conversationTracker.abandonConversation(conversation, new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        conversationTracker.abandonConversation(conversation, details);
    }

    @Override
    public void acceptConversationInput(String input) {
        conversationTracker.acceptConversationInput(input);
    }

    @Override
    public boolean isConversing() {
        return conversationTracker.isConversing();
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(server.getMessenger(), source, channel, message);
        if (getHandle().networkHandler == null) return;

        if (channels.contains(channel)) {
            channel = StandardMessenger.validateAndCorrectChannel(channel);
            CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(new Identifier(channel), new PacketByteBuf(Unpooled.wrappedBuffer(message)));
            getHandle().networkHandler.sendPacket(packet);
        }
    }

    @Override
    public void setTexturePack(String url) {
        setResourcePack(url);
    }

    @Override
    public void setResourcePack(String url) {
        Validate.notNull(url, "Resource pack URL cannot be null");

        getHandle().sendResourcePackUrl(url, "null");
    }

    @Override
    public void setResourcePack(String url, byte[] hash) {
        Validate.notNull(url, "Resource pack URL cannot be null");
        Validate.notNull(hash, "Resource pack hash cannot be null");
        Validate.isTrue(hash.length == 20, "Resource pack hash should be 20 bytes long but was " + hash.length);

        getHandle().sendResourcePackUrl(url, BaseEncoding.base16().lowerCase().encode(hash));
    }

    public void addChannel(String channel) {
        Preconditions.checkState(channels.size() < 128, "Cannot register channel '%s'. Too many channels registered!", channel);
        channel = StandardMessenger.validateAndCorrectChannel(channel);
        if (channels.add(channel)) {
            server.getPluginManager().callEvent(new PlayerRegisterChannelEvent(this, channel));
        }
    }

    public void removeChannel(String channel) {
        channel = StandardMessenger.validateAndCorrectChannel(channel);
        if (channels.remove(channel)) {
            server.getPluginManager().callEvent(new PlayerUnregisterChannelEvent(this, channel));
        }
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return ImmutableSet.copyOf(channels);
    }

    public void sendSupportedChannels() {
        if (getHandle().networkHandler == null) return;
        Set<String> listening = server.getMessenger().getIncomingChannels();

        if (!listening.isEmpty()) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            for (String channel : listening) {
                try {
                    stream.write(channel.getBytes("UTF8"));
                    stream.write((byte) 0);
                } catch (IOException ex) {
                    Logger.getLogger(CraftPlayer.class.getName()).log(Level.SEVERE, "Could not send Plugin Channel REGISTER to " + getName(), ex);
                }
            }

            getHandle().networkHandler.sendPacket(new CustomPayloadS2CPacket(new Identifier("register"), new PacketByteBuf(Unpooled.wrappedBuffer(stream.toByteArray()))));
        }
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        server.getPlayerMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return server.getPlayerMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return server.getPlayerMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        server.getPlayerMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    @Override
    public boolean setWindowProperty(Property prop, int value) {
        Container container = getHandle().container;
        if (container.getBukkitView().getType() != prop.getType()) {
            return false;
        }
        getHandle().onContainerPropertyUpdate(container, prop.getId(), value);
        return true;
    }

    public void disconnect(String reason) {
        conversationTracker.abandonAllConversations();
        perm.clearPermissions();
    }

    @Override
    public boolean isFlying() {
        return getHandle().abilities.flying;
    }

    @Override
    public void setFlying(boolean value) {
        if (!getAllowFlight() && value) {
            throw new IllegalArgumentException("Cannot make player fly if getAllowFlight() is false");
        }

        getHandle().abilities.flying = value;
        getHandle().sendAbilitiesUpdate();
    }

    @Override
    public boolean getAllowFlight() {
        return getHandle().abilities.allowFlying;
    }

    @Override
    public void setAllowFlight(boolean value) {
        if (isFlying() && !value) {
            getHandle().abilities.flying = false;
        }

        getHandle().abilities.allowFlying = value;
        getHandle().sendAbilitiesUpdate();
    }

    @Override
    public int getNoDamageTicks() {
        if (getHandle().field_13998 > 0) {
            return Math.max(getHandle().field_13998, getHandle().timeUntilRegen);
        } else {
            return getHandle().timeUntilRegen;
        }
    }

    @Override
    public float getFlySpeed() {
        return (float) getHandle().abilities.flySpeed * 2f;
    }

    @Override
    public void setFlySpeed(float value) {
        validateSpeed(value);
        ServerPlayerEntity player = getHandle();
        player.abilities.flySpeed = value / 2f;
        player.sendAbilitiesUpdate();

    }

    @Override
    public float getWalkSpeed() {
        return getHandle().abilities.walkSpeed * 2f;
    }

    @Override
    public void setWalkSpeed(float value) {
        validateSpeed(value);
        ServerPlayerEntity player = getHandle();
        player.abilities.walkSpeed = value / 2f;
        player.sendAbilitiesUpdate();
    }

    private void validateSpeed(float value) {
        if (value < 0) {
            if (value < -1f) {
                throw new IllegalArgumentException(value + " is too low");
            }
        } else {
            if (value > 1f) {
                throw new IllegalArgumentException(value + " is too high");
            }
        }
    }

    @Override
    public void setMaxHealth(double amount) {
        super.setMaxHealth(amount);
        this.health = Math.min(this.health, health);
        getHandle().markHealthDirty();
    }

    @Override
    public void resetMaxHealth() {
        super.resetMaxHealth();
        getHandle().markHealthDirty();
    }

    @Override
    public CraftScoreboard getScoreboard() {
        System.out.println(server.getScoreboardManager());
        return this.server.getScoreboardManager().getPlayerBoard(this);
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) {
        Validate.notNull(scoreboard, "Scoreboard cannot be null");
        ServerPlayNetworkHandler playerConnection = getHandle().networkHandler;
        if (playerConnection == null) {
            throw new IllegalStateException("Cannot set scoreboard yet");
        }
        if (playerConnection.isDisconnected()) {
            throw new IllegalStateException("Cannot set scoreboard for invalid CraftPlayer");
        }

        this.server.getScoreboardManager().setPlayerBoard(this, scoreboard);
    }

    @Override
    public double getHealthScale() {
        return healthScale;
    }

    @Override
    public void setHealthScale(double value) {
        Validate.isTrue((float) value > 0F, "Must be greater than 0");
        healthScale = value;
        scaledHealth = true;
        updateScaledHealth();
    }

    @Override
    public boolean isHealthScaled() {
        return scaledHealth;
    }

    @Override
    public void setHealthScaled(boolean scale) {
        if (scaledHealth != (scaledHealth = scale)) {
            updateScaledHealth();
        }
    }

    public float getScaledHealth() {
        return (float) (isHealthScaled() ? getHealth() * getHealthScale() / getMaxHealth() : getHealth());
    }

    @Override
    public double getHealth() {
        return health;
    }

    public void setRealHealth(double health) {
        this.health = health;
    }

    public void updateScaledHealth() {
        updateScaledHealth(true);
    }

    public void updateScaledHealth(boolean sendHealth) {
        EntityAttributeContainer attributemapserver = (EntityAttributeContainer) getHandle().getAttributes();
        Collection<EntityAttributeInstance> set = attributemapserver.buildTrackedAttributesCollection(); // PAIL: Rename

        injectScaledMaxHealth(set, true);

        // SPIGOT-3813: Attributes before health
        if (getHandle().networkHandler != null) {
            getHandle().networkHandler.sendPacket(new EntityAttributesS2CPacket(getHandle().getEntityId(), set));
            if (sendHealth) {
                sendHealthUpdate();
            }
        }
        getHandle().getDataTracker().set(LivingEntity.HEALTH, (float) getScaledHealth());

        getHandle().maxHealthCache = getMaxHealth();
    }

    public void sendHealthUpdate() {
        getHandle().networkHandler.sendPacket(new HealthUpdateS2CPacket(getScaledHealth(), getHandle().getHungerManager().getFoodLevel(), getHandle().getHungerManager().getSaturationLevel()));
    }

    public void injectScaledMaxHealth(Collection<EntityAttributeInstance> collection, boolean force) {
        if (!scaledHealth && !force) {
            return;
        }
        for (EntityAttributeInstance genericInstance : collection) {
            if (genericInstance.getAttribute().getId().equals("generic.maxHealth")) {
                collection.remove(genericInstance);
                break;
            }
        }
        collection.add(new EntityAttributeInstanceImpl(getHandle().getAttributes(), (new ClampedEntityAttribute(null, "generic.maxHealth", scaledHealth ? healthScale : getMaxHealth(), 0.0D, Float.MAX_VALUE)).setName("Max Health").setTracked(true)));
    }

    @Override
    public org.bukkit.entity.Entity getSpectatorTarget() {
        Entity followed = getHandle().getCameraEntity();
        return followed == getHandle() ? null : followed.getBukkitEntity();
    }

    @Override
    public void setSpectatorTarget(org.bukkit.entity.Entity entity) {
        Preconditions.checkArgument(getGameMode() == GameMode.SPECTATOR, "Player must be in spectator mode");
        getHandle().setCameraEntity((entity == null) ? null : ((CraftEntity) entity).getHandle());
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        sendTitle(title, subtitle, 10, 70, 20);
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        TitleS2CPacket times = new TitleS2CPacket(fadeIn, stay, fadeOut);
        getHandle().networkHandler.sendPacket(times);

        if (title != null) {
            TitleS2CPacket packetTitle = new TitleS2CPacket(TitleS2CPacket.Action.TITLE, CraftChatMessage.fromStringOrNull(title));
            getHandle().networkHandler.sendPacket(packetTitle);
        }

        if (subtitle != null) {
            TitleS2CPacket packetSubtitle = new TitleS2CPacket(TitleS2CPacket.Action.SUBTITLE, CraftChatMessage.fromStringOrNull(subtitle));
            getHandle().networkHandler.sendPacket(packetSubtitle);
        }
    }

    @Override
    public void resetTitle() {
        TitleS2CPacket packetReset = new TitleS2CPacket(TitleS2CPacket.Action.RESET, null);
        getHandle().networkHandler.sendPacket(packetReset);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count) {
        spawnParticle(particle, x, y, z, count, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, T data) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {
        spawnParticle(particle, x, y, z, count, 0, 0, 0, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, 1, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        if (data != null && !particle.getDataType().isInstance(data)) {
            throw new IllegalArgumentException("data should be " + particle.getDataType() + " got " + data.getClass());
        }
        ParticleS2CPacket packetplayoutworldparticles = new ParticleS2CPacket(CraftParticle.toNMS(particle, data), true, (float) x, (float) y, (float) z, (float) offsetX, (float) offsetY, (float) offsetZ, (float) extra, count);
        getHandle().networkHandler.sendPacket(packetplayoutworldparticles);

    }

    @Override
    public org.bukkit.advancement.AdvancementProgress getAdvancementProgress(org.bukkit.advancement.Advancement advancement) {
        Preconditions.checkArgument(advancement != null, "advancement");

        CraftAdvancement craft = (CraftAdvancement) advancement;
        PlayerAdvancementTracker data = getHandle().getAdvancementTracker();
        AdvancementProgress progress = data.getProgress(craft.getHandle());

        return new CraftAdvancementProgress(craft, data, progress);
    }

    @Override
    public int getClientViewDistance() {
        return (getHandle().clientViewDistance == null) ? Bukkit.getViewDistance() : getHandle().clientViewDistance;
    }

    @Override
    public String getLocale() {
        return getHandle().clientLanguage;
    }

    @Override
    public void updateCommands() {
        if (getHandle().networkHandler == null) return;

        getHandle().server.getCommandManager().sendCommandTree(getHandle());
    }

    @Override
    public void openBook(ItemStack book) {
        Validate.isTrue(book != null, "book == null");
        Validate.isTrue(book.getType() == Material.WRITTEN_BOOK, "Book must be Material.WRITTEN_BOOK");

        ItemStack hand = getInventory().getItemInMainHand();
        getInventory().setItemInMainHand(book);
        getHandle().openEditBookScreen(org.bukkit.craftbukkit.inventory.CraftItemStack.asNMSCopy(book), net.minecraft.util.Hand.MAIN_HAND);
        getInventory().setItemInMainHand(hand);
    }
}
