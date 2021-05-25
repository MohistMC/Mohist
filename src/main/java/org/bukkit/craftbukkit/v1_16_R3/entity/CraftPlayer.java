package org.bukkit.craftbukkit.v1_16_R3.entity;

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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.server.*;
import net.minecraft.server.management.WhitelistEntry;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.MapDecoration;
import net.minecraftforge.common.util.ITeleporter;

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
import org.bukkit.NamespacedKey;
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
import org.bukkit.craftbukkit.v1_16_R3.CraftEffect;
import org.bukkit.craftbukkit.v1_16_R3.CraftOfflinePlayer;
import org.bukkit.craftbukkit.v1_16_R3.CraftParticle;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftSound;
import org.bukkit.craftbukkit.v1_16_R3.CraftStatistic;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.advancement.CraftAdvancement;
import org.bukkit.craftbukkit.v1_16_R3.advancement.CraftAdvancementProgress;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftSign;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_16_R3.conversations.ConversationTracker;
import org.bukkit.craftbukkit.v1_16_R3.map.CraftMapView;
import org.bukkit.craftbukkit.v1_16_R3.map.RenderData;
import org.bukkit.craftbukkit.v1_16_R3.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
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
public class CraftPlayer extends org.bukkit.craftbukkit.v1_16_R3.entity.CraftHumanEntity implements Player {
    private long firstPlayed = 0;
    private long lastPlayed = 0;
    private boolean hasPlayedBefore = false;
    private final ConversationTracker conversationTracker = new ConversationTracker();
    private final Set<String> channels = new HashSet<String>();
    private final Map<UUID, Set<WeakReference<Plugin>>> hiddenPlayers = new HashMap<>();
    private static final WeakHashMap<Plugin, WeakReference<Plugin>> pluginWeakReferences = new WeakHashMap<>();
    private int hash = 0;
    private double health = 20;
    private boolean scaledHealth = false;
    private double healthScale = 20;

    public CraftPlayer(CraftServer server, ServerPlayerEntity entity) {
        super(server, entity);

        firstPlayed = System.currentTimeMillis();
    }

    public GameProfile getProfile() {
        return getHandle().getGameProfile();
    }

    @Override
    public boolean isOp() {
        return server.getHandle().isOp(getProfile());
    }

    @Override
    public void setOp(boolean value) {
        if (value == isOp()) return;

        if (value) {
            server.getHandle().op(getProfile());
        } else {
            server.getHandle().deop(getProfile());
        }

        perm.recalculatePermissions();
    }

    @Override
    public boolean isOnline() {
        return server.getPlayer(getUniqueId()) != null;
    }

    @Override
    public InetSocketAddress getAddress() {
        if (getHandle().connection == null) return null;

        SocketAddress addr = getHandle().connection.connection.getRemoteAddress();
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
        if (getHandle().connection == null) return;

        for (ITextComponent component : CraftChatMessage.fromString(message)) {
            getHandle().connection.send(new SChatPacket(component, ChatType.SYSTEM, Util.NIL_UUID));
        }
    }

    @Override
    public void sendRawMessage(UUID sender, String message) {
        if (getHandle().connection == null) return;

        for (ITextComponent component : CraftChatMessage.fromString(message)) {
            getHandle().connection.send(new SChatPacket(component, ChatType.CHAT, (sender == null) ? Util.NIL_UUID : sender));
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
    public void sendMessage(UUID sender, String message) {
        if (!conversationTracker.isConversingModaly()) {
            this.sendRawMessage(sender, message);
        }
    }

    @Override
    public void sendMessage(UUID sender, String[] messages) {
        for (String message : messages) {
            sendMessage(sender, message);
        }
    }

    @Override
    public String getDisplayName() {
        if (true) return io.papermc.paper.adventure.DisplayNames.getLegacy(this); // Paper
        return getHandle().displayName;
    }

    @Override
    public void setDisplayName(final String name) {
        this.getHandle().adventure$displayName = name != null ? io.papermc.paper.adventure.PaperAdventure.LEGACY_SECTION_UXRC.deserialize(name) : net.kyori.adventure.text.Component.text(this.getName()); // Paper
        getHandle().displayName = name == null ? getName() : name;
    }

    // Paper start
    @Override
    public void playerListName(net.kyori.adventure.text.Component name) {
        getHandle().listName = name == null ? null : io.papermc.paper.adventure.PaperAdventure.asVanilla(name);
        for (ServerPlayerEntity player : server.getHandle().players) {
            if (player.getBukkitEntity().canSee(this)) {
                player.connection.send(new SPlayerListItemPacket(SPlayerListItemPacket.Action.UPDATE_DISPLAY_NAME, getHandle()));
            }
        }
    }

    @Override
    public net.kyori.adventure.text.Component playerListName() {
        return getHandle().listName == null ? net.kyori.adventure.text.Component.text(getName()) : io.papermc.paper.adventure.PaperAdventure.asAdventure(getHandle().listName);
    }

    @Override
    public net.kyori.adventure.text.Component playerListHeader() {
        return playerListHeader;
    }

    @Override
    public net.kyori.adventure.text.Component playerListFooter() {
        return playerListFooter;
    }
    // Paper end

    @Override
    public String getPlayerListName() {
        return getHandle().listName == null ? getName() : CraftChatMessage.fromComponent(getHandle().listName);
    }

    @Override
    public void setPlayerListName(String name) {
        if (name == null) {
            name = getName();
        }
        getHandle().listName = name.equals(getName()) ? null : CraftChatMessage.fromStringOrNull(name);
        for (ServerPlayerEntity player : server.getHandle().players) {
            if (player.getBukkitEntity().canSee(this)) {
                player.connection.send(new SPlayerListItemPacket(SPlayerListItemPacket.Action.UPDATE_DISPLAY_NAME, getHandle()));
            }
        }
    }

    private net.kyori.adventure.text.Component playerListHeader; // Paper - Adventure
    private net.kyori.adventure.text.Component playerListFooter; // Paper - Adventure

    @Override
    public String getPlayerListHeader() {
        return (playerListHeader == null) ? null : io.papermc.paper.adventure.PaperAdventure.LEGACY_SECTION_UXRC.serialize(playerListHeader); // Paper - Adventure
    }

    @Override
    public String getPlayerListFooter() {
        return (playerListFooter == null) ? null : io.papermc.paper.adventure.PaperAdventure.LEGACY_SECTION_UXRC.serialize(playerListFooter); // Paper - Adventure
    }

    @Override
    public void setPlayerListHeader(String header) {
        this.playerListHeader = header == null ? null : io.papermc.paper.adventure.PaperAdventure.LEGACY_SECTION_UXRC.deserialize(header); // Paper - Adventure
        updatePlayerListHeaderFooter();
    }

    @Override
    public void setPlayerListFooter(String footer) {
        this.playerListFooter = footer == null ? null : io.papermc.paper.adventure.PaperAdventure.LEGACY_SECTION_UXRC.deserialize(footer); // Paper - Adventure
        updatePlayerListHeaderFooter();
    }

    @Override
    public void setPlayerListHeaderFooter(String header, String footer) {
        this.playerListHeader = header == null ? null : io.papermc.paper.adventure.PaperAdventure.LEGACY_SECTION_UXRC.deserialize(header); // Paper - Adventure
        this.playerListFooter = footer == null ? null : io.papermc.paper.adventure.PaperAdventure.LEGACY_SECTION_UXRC.deserialize(footer); // Paper - Adventure
        updatePlayerListHeaderFooter();
    }

    private void updatePlayerListHeaderFooter() {
        if (getHandle().connection == null) return;

        SPlayerListHeaderFooterPacket packet = new SPlayerListHeaderFooterPacket();
        packet.header = (this.playerListHeader == null) ? new StringTextComponent("") : io.papermc.paper.adventure.PaperAdventure.asVanilla(this.playerListHeader); // Paper - Adventure
        packet.footer = (this.playerListFooter == null) ? new StringTextComponent("") : io.papermc.paper.adventure.PaperAdventure.asVanilla(this.playerListFooter); // Paper - Adventure
        getHandle().connection.send(packet);
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
        if (getHandle().connection == null) return;

        getHandle().connection.disconnect(message == null ? "" : message);
    }

    // Paper start
    @Override
    public void kick(final net.kyori.adventure.text.Component message) {
        org.spigotmc.AsyncCatcher.catchOp("player kick");
        final ServerPlayNetHandler connection = this.getHandle().connection;
        if (connection != null) {
            connection.disconnect(io.papermc.paper.adventure.PaperAdventure.asVanilla(
                    message == null ? net.kyori.adventure.text.Component.empty() : message
            ));

        }

    }
    // Paper end

    @Override
    public void setCompassTarget(Location loc) {
        if (getHandle().connection == null) return;

        // Do not directly assign here, from the packethandler we'll assign it.
        getHandle().connection.send(new SWorldSpawnChangedPacket(new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), loc.getYaw()));
    }

    @Override
    public Location getCompassTarget() {
        return getHandle().compassTarget;
    }

    @Override
    public void chat(String msg) {
        if (getHandle().connection == null) return;

        getHandle().connection.chat(msg, false);
    }

    @Override
    public boolean performCommand(String command) {
        return server.dispatchCommand(this, command);
    }

    @Override
    public void playNote(Location loc, byte instrument, byte note) {
        if (getHandle().connection == null) return;

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
        getHandle().connection.send(new SPlaySoundEffectPacket(CraftSound.getSoundEffect("block.note_block." + instrumentName), net.minecraft.util.SoundCategory.RECORDS, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 3.0f, f));
    }

    @Override
    public void playNote(Location loc, Instrument instrument, Note note) {
        if (getHandle().connection == null) return;

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
        getHandle().connection.send(new SPlaySoundEffectPacket(CraftSound.getSoundEffect("block.note_block." + instrumentName), net.minecraft.util.SoundCategory.RECORDS, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 3.0f, f));
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
        if (loc == null || sound == null || category == null || getHandle().connection == null) return;

        SPlaySoundEffectPacket packet = new SPlaySoundEffectPacket(CraftSound.getSoundEffect(sound), net.minecraft.util.SoundCategory.valueOf(category.name()), loc.getX(), loc.getY(), loc.getZ(), volume, pitch);
        getHandle().connection.send(packet);
    }

    @Override
    public void playSound(Location loc, String sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        if (loc == null || sound == null || category == null || getHandle().connection == null) return;

        SPlaySoundPacket packet = new SPlaySoundPacket(new ResourceLocation(sound), net.minecraft.util.SoundCategory.valueOf(category.name()), new Vector3d(loc.getX(), loc.getY(), loc.getZ()), volume, pitch);
        getHandle().connection.send(packet);
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
        stopSound(sound, category);
    }

    @Override
    public void stopSound(String sound, org.bukkit.SoundCategory category) {
        if (getHandle().connection == null) return;

        getHandle().connection.send(new SStopSoundPacket(new ResourceLocation(sound), category == null ? net.minecraft.util.SoundCategory.MASTER : net.minecraft.util.SoundCategory.valueOf(category.name())));
    }

    @Override
    public void playEffect(Location loc, Effect effect, int data) {
        if (getHandle().connection == null) return;

        int packetData = effect.getId();
        SPlaySoundEventPacket packet = new SPlaySoundEventPacket(packetData, new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), data, false);
        getHandle().connection.send(packet);
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
        if (getHandle().connection == null) return;

        SChangeBlockPacket packet = new SChangeBlockPacket(new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), CraftMagicNumbers.getBlock(material, data));

        packet.blockState = CraftMagicNumbers.getBlock(material, data);
        getHandle().connection.send(packet);
    }

    @Override
    public void sendBlockChange(Location loc, BlockData block) {
        if (getHandle().connection == null) return;

        SChangeBlockPacket packet = new SChangeBlockPacket(new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), ((CraftBlockData) block).getState());

        packet.blockState = ((CraftBlockData) block).getState();
        getHandle().connection.send(packet);
    }

    @Override
    public void sendBlockDamage(Location loc, float progress) {
        Preconditions.checkArgument(loc != null, "loc must not be null");
        Preconditions.checkArgument(progress >= 0.0 && progress <= 1.0, "progress must be between 0.0 and 1.0 (inclusive)");

        if (getHandle().connection == null) return;

        int stage = (int) (9 * progress); // There are 0 - 9 damage states
        SAnimateBlockBreakPacket packet = new SAnimateBlockBreakPacket(getHandle().getId(), new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), stage);
        getHandle().connection.send(packet);
    }

    // Paper start
    @Override
    public void sendSignChange(Location loc, List<net.kyori.adventure.text.Component> lines) {
        this.sendSignChange(loc, lines, org.bukkit.DyeColor.BLACK);
    }

    @Override
    public void sendSignChange(Location loc, List<net.kyori.adventure.text.Component> lines, DyeColor dyeColor) {
        if (getHandle().connection == null) {
            return;
        }
        if (lines == null) {
            lines = new java.util.ArrayList<>(4);
        }
        Validate.notNull(loc, "Location cannot be null");
        Validate.notNull(dyeColor, "DyeColor cannot be null");
        if (lines.size() < 4) {
            throw new IllegalArgumentException("Must have at least 4 lines");
        }
        ITextComponent[] components = CraftSign.sanitizeLines(lines);
        this.sendSignChange0(components, loc, dyeColor);
    }

    private void sendSignChange0(ITextComponent[] components, Location loc, DyeColor dyeColor) {
        SignTileEntity sign = new SignTileEntity();
        sign.setPosition(new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        sign.setColor(net.minecraft.item.DyeColor.byId(dyeColor.getWoolData()));
        System.arraycopy(components, 0, sign.messages, 0, sign.messages.length);

        getHandle().connection.send(sign.getUpdatePacket());
    }
    // Paper end

    @Override
    public void sendSignChange(Location loc, String[] lines) {
        sendSignChange(loc, lines, DyeColor.BLACK);
    }

    @Override
    public void sendSignChange(Location loc, String[] lines, DyeColor dyeColor) {
        if (getHandle().connection == null) {
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

        ITextComponent[] components = CraftSign.sanitizeLines(lines);
        /*SignTileEntity sign = new SignTileEntity(); // Paper
        sign.setPosition(new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        sign.setColor(net.minecraft.item.DyeColor.byId(dyeColor.getWoolData()));
        System.arraycopy(components, 0, sign.messages, 0, sign.messages.length);

        getHandle().connection.send(sign.getUpdatePacket()); */ // Paper
        this.sendSignChange0(components, loc, dyeColor); // Paper
    }

    @Override
    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        if (getHandle().connection == null) return false;

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

        getHandle().connection.send(packet);

        return true;
        */

        throw new NotImplementedException("Chunk changes do not yet work"); // TODO: Chunk changes.
    }

    @Override
    public void sendMap(MapView map) {
        if (getHandle().connection == null) return;

        RenderData data = ((CraftMapView) map).render(this);
        Collection<MapDecoration> icons = new ArrayList<MapDecoration>();
        for (MapCursor cursor : data.cursors) {
            if (cursor.isVisible()) {
                icons.add(new MapDecoration(MapDecoration.Type.byIcon(cursor.getRawType()), cursor.getX(), cursor.getY(), cursor.getDirection(), CraftChatMessage.fromStringOrNull(cursor.getCaption())));
            }
        }

        SMapDataPacket packet = new SMapDataPacket(map.getId(), map.getScale().getValue(), true, map.isLocked(), icons, data.buffer, 0, 0, 128, 128);
        getHandle().connection.send(packet);
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

        if (entity.connection == null) {
            return false;
        }

        if (entity.isVehicle()) {
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
        if (getHandle().containerMenu != getHandle().inventoryMenu) {
            getHandle().closeContainer();
        }

        // Check if the fromWorld and toWorld are the same.
        /* if (fromWorld == toWorld) {
            entity.connection.teleport(to);
        } else {
            server.getHandle().moveToWorld(entity, toWorld, true, to, true);
        } */

        // Current implementation of 'moveToWorld' does not
        // work properly. Luckily, 'changeDimension' with
        // dummy teleporter does the same thing, but right.
        if (fromWorld != toWorld)
            entity.changeDimension(toWorld, new ITeleporter() {
            });
        entity.connection.teleport(to);
        return true;
    }

    @Override
    public void setSneaking(boolean sneak) {
        getHandle().setShiftKeyDown(sneak);
    }

    @Override
    public boolean isSneaking() {
        return getHandle().isShiftKeyDown();
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
        server.getHandle().playerIo.load(getHandle());
    }

    @Override
    public void saveData() {
        server.getHandle().playerIo.save(getHandle());
    }

    @Deprecated
    @Override
    public void updateInventory() {
        getHandle().refreshContainer(getHandle().containerMenu);
    }

    @Override
    public void setSleepingIgnored(boolean isSleeping) {
        getHandle().fauxSleeping = isSleeping;
        ((CraftWorld) getWorld()).getHandle().updateSleepingPlayerList();
    }

    @Override
    public boolean isSleepingIgnored() {
        return getHandle().fauxSleeping;
    }

    @Override
    public Location getBedSpawnLocation() {
        ServerWorld world = getHandle().server.getLevel(getHandle().getRespawnDimension());
        BlockPos bed = getHandle().getRespawnPosition();

        if (world != null && bed != null) {
            Optional<Vector3d> spawnLoc = PlayerEntity.findRespawnPositionAndUseSpawnBlock(world, bed, getHandle().getRespawnAngle(), getHandle().isRespawnForced(), true);
            if (spawnLoc.isPresent()) {
                Vector3d vec = spawnLoc.get();
                return new Location(world.getWorld(), vec.x, vec.y, vec.z);
            }
        }
        return null;
    }

    @Override
    public void setBedSpawnLocation(Location location) {
        setBedSpawnLocation(location, false);
    }

    @Override
    public void setBedSpawnLocation(Location location, boolean override) {
        if (location == null) {
            getHandle().setRespawnPosition(null, null, 0.0F, override, false);
        } else {
            getHandle().setRespawnPosition(((CraftWorld) location.getWorld()).getHandle().dimension(), new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()), location.getYaw(), override, false);
        }
    }

    @Override
    public Location getBedLocation() {
        Preconditions.checkState(isSleeping(), "Not sleeping");

        BlockPos bed = getHandle().getRespawnPosition();
        return new Location(getWorld(), bed.getX(), bed.getY(), bed.getZ());
    }

    @Override
    public boolean hasDiscoveredRecipe(NamespacedKey recipe) {
        Preconditions.checkArgument(recipe != null, "recipe cannot be null");
        return getHandle().getRecipeBook().contains(CraftNamespacedKey.toMinecraft(recipe));
    }

    @Override
    public Set<NamespacedKey> getDiscoveredRecipes() {
        ImmutableSet.Builder<NamespacedKey> bukkitRecipeKeys = ImmutableSet.builder();
        getHandle().getRecipeBook().known.forEach(key -> bukkitRecipeKeys.add(CraftNamespacedKey.fromMinecraft(key)));
        return bukkitRecipeKeys.build();
    }

    @Override
    public void incrementStatistic(Statistic statistic) {
        CraftStatistic.incrementStatistic(getHandle().getStats(), statistic);
    }

    @Override
    public void decrementStatistic(Statistic statistic) {
        CraftStatistic.decrementStatistic(getHandle().getStats(), statistic);
    }

    @Override
    public int getStatistic(Statistic statistic) {
        return CraftStatistic.getStatistic(getHandle().getStats(), statistic);
    }

    @Override
    public void incrementStatistic(Statistic statistic, int amount) {
        CraftStatistic.incrementStatistic(getHandle().getStats(), statistic, amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, int amount) {
        CraftStatistic.decrementStatistic(getHandle().getStats(), statistic, amount);
    }

    @Override
    public void setStatistic(Statistic statistic, int newValue) {
        CraftStatistic.setStatistic(getHandle().getStats(), statistic, newValue);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material) {
        CraftStatistic.incrementStatistic(getHandle().getStats(), statistic, material);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material) {
        CraftStatistic.decrementStatistic(getHandle().getStats(), statistic, material);
    }

    @Override
    public int getStatistic(Statistic statistic, Material material) {
        return CraftStatistic.getStatistic(getHandle().getStats(), statistic, material);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material, int amount) {
        CraftStatistic.incrementStatistic(getHandle().getStats(), statistic, material, amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material, int amount) {
        CraftStatistic.decrementStatistic(getHandle().getStats(), statistic, material, amount);
    }

    @Override
    public void setStatistic(Statistic statistic, Material material, int newValue) {
        CraftStatistic.setStatistic(getHandle().getStats(), statistic, material, newValue);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType) {
        CraftStatistic.incrementStatistic(getHandle().getStats(), statistic, entityType);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType) {
        CraftStatistic.decrementStatistic(getHandle().getStats(), statistic, entityType);
    }

    @Override
    public int getStatistic(Statistic statistic, EntityType entityType) {
        return CraftStatistic.getStatistic(getHandle().getStats(), statistic, entityType);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        CraftStatistic.incrementStatistic(getHandle().getStats(), statistic, entityType, amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        CraftStatistic.decrementStatistic(getHandle().getStats(), statistic, entityType, amount);
    }

    @Override
    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
        CraftStatistic.setStatistic(getHandle().getStats(), statistic, entityType, newValue);
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
    public void setPlayerWeather(WeatherType type) {
        getHandle().setPlayerWeather(type, true);
    }

    @Override
    public WeatherType getPlayerWeather() {
        return getHandle().getPlayerWeather();
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
        return server.getHandle().getWhiteList().isWhiteListed(getProfile());
    }

    @Override
    public void setWhitelisted(boolean value) {
        if (value) {
            server.getHandle().getWhiteList().add(new WhitelistEntry(getProfile()));
        } else {
            server.getHandle().getWhiteList().remove(getProfile());
        }
    }

    @Override
    public void setGameMode(GameMode mode) {
        if (getHandle().connection == null) return;

        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null");
        }

        getHandle().setGameMode(GameType.byId(mode.getValue()));
    }

    @Override
    public GameMode getGameMode() {
        return GameMode.getByValue(getHandle().gameMode.getGameModeForPlayer().getId());
    }

    @Override
    public void giveExp(int exp) {
        getHandle().giveExperiencePoints(exp);
    }

    @Override
    public void giveExpLevels(int levels) {
        getHandle().giveExperienceLevels(levels);
    }

    @Override
    public float getExp() {
        return getHandle().experienceProgress;
    }

    @Override
    public void setExp(float exp) {
        Preconditions.checkArgument(exp >= 0.0 && exp <= 1.0, "Experience progress must be between 0.0 and 1.0 (%s)", exp);
        getHandle().experienceProgress = exp;
        getHandle().lastSentExp = -1;
    }

    @Override
    public int getLevel() {
        return getHandle().experienceLevel;
    }

    @Override
    public void setLevel(int level) {
        Preconditions.checkArgument(level >= 0, "Experience level must not be negative (%s)", level);
        getHandle().experienceLevel = level;
        getHandle().lastSentExp = -1;
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

        if (getHandle().connection == null) {
            return;
        }

        SSetExperiencePacket packet = new SSetExperiencePacket(progress, getTotalExperience(), level);
        getHandle().connection.send(packet);
    }

    @Nullable
    private static WeakReference<Plugin> getPluginWeakReference(@Nullable Plugin plugin) {
        return (plugin == null) ? null : pluginWeakReferences.computeIfAbsent(plugin, WeakReference::new);
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
        if (getHandle().connection == null) return;
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
        ChunkManager tracker = ((ServerWorld) entity.level).getChunkSource().chunkMap;
        ServerPlayerEntity other = ((CraftPlayer) player).getHandle();
        tracker.removeTracker(tracker, getHandle(), other.getId());

        // Remove the hidden player from this player user list, if they're on it
        if (other.sentListPacket) {
            getHandle().connection.send(new SPlayerListItemPacket(SPlayerListItemPacket.Action.REMOVE_PLAYER, other));
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
        if (getHandle().connection == null) return;
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

        ChunkManager tracker = ((ServerWorld) entity.level).getChunkSource().chunkMap;
        ServerPlayerEntity other = ((CraftPlayer) player).getHandle();
        getHandle().connection.send(new SPlayerListItemPacket(SPlayerListItemPacket.Action.ADD_PLAYER, other));
        tracker.updateTrackingState(tracker, getHandle(), other.getId());
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

    @Override
    public long getLastPlayed() {
        return lastPlayed;
    }

    @Override
    public boolean hasPlayedBefore() {
        return hasPlayedBefore;
    }

    public void setFirstPlayed(long firstPlayed) {
        this.firstPlayed = firstPlayed;
    }

    public void readExtraData(CompoundNBT nbttagcompound) {
        hasPlayedBefore = true;
        if (nbttagcompound.contains("bukkit")) {
            CompoundNBT data = nbttagcompound.getCompound("bukkit");

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

    public void setExtraData(CompoundNBT nbttagcompound) {
        if (!nbttagcompound.contains("bukkit")) {
            nbttagcompound.put("bukkit", new CompoundNBT());
        }

        CompoundNBT data = nbttagcompound.getCompound("bukkit");
        ServerPlayerEntity handle = getHandle();
        data.putInt("newExp", handle.newExp);
        data.putInt("newTotalExp", handle.newTotalExp);
        data.putInt("newLevel", handle.newLevel);
        data.putInt("expToDrop", handle.expToDrop);
        data.putBoolean("keepLevel", handle.keepLevel);
        data.putLong("firstPlayed", getFirstPlayed());
        data.putLong("lastPlayed", System.currentTimeMillis());
        data.putString("lastKnownName", handle.getScoreboardName());
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
        if (getHandle().connection == null) return;

        if (channels.contains(channel)) {
            channel = StandardMessenger.validateAndCorrectChannel(channel);
            SCustomPayloadPlayPacket packet = new SCustomPayloadPlayPacket(new ResourceLocation(channel), new PacketBuffer(Unpooled.wrappedBuffer(message)));
            getHandle().connection.send(packet);
        }
    }

    @Override
    public void setTexturePack(String url) {
        setResourcePack(url);
    }

    @Override
    public void setResourcePack(String url) {
        Validate.notNull(url, "Resource pack URL cannot be null");

        getHandle().sendTexturePack(url, "null");
    }

    @Override
    public void setResourcePack(String url, byte[] hash) {
        Validate.notNull(url, "Resource pack URL cannot be null");
        Validate.notNull(hash, "Resource pack hash cannot be null");
        Validate.isTrue(hash.length == 20, "Resource pack hash should be 20 bytes long but was " + hash.length);

        getHandle().sendTexturePack(url, BaseEncoding.base16().lowerCase().encode(hash));
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
        if (getHandle().connection == null) return;
        Set<String> listening = server.getMessenger().getIncomingChannels();

        if (!listening.isEmpty()) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            for (String channel : listening) {
                try {
                    stream.write(channel.getBytes(StandardCharsets.UTF_8));
                    stream.write((byte) 0);
                } catch (IOException ex) {
                    Logger.getLogger(CraftPlayer.class.getName()).log(Level.SEVERE, "Could not send Plugin Channel REGISTER to " + getName(), ex);
                }
            }

            getHandle().connection.send(new SCustomPayloadPlayPacket(new ResourceLocation("register"), new PacketBuffer(Unpooled.wrappedBuffer(stream.toByteArray()))));
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
        Container container = getHandle().containerMenu;
        if (container.getBukkitView().getType() != prop.getType()) {
            return false;
        }
        getHandle().setContainerData(container, prop.getId(), value);
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
        getHandle().onUpdateAbilities();
    }

    @Override
    public boolean getAllowFlight() {
        return getHandle().abilities.mayfly;
    }

    @Override
    public void setAllowFlight(boolean value) {
        if (isFlying() && !value) {
            getHandle().abilities.flying = false;
        }

        getHandle().abilities.mayfly = value;
        getHandle().onUpdateAbilities();
    }

    @Override
    public int getNoDamageTicks() {
        if (getHandle().spawnInvulnerableTime > 0) {
            return Math.max(getHandle().spawnInvulnerableTime, getHandle().invulnerableTime);
        } else {
            return getHandle().invulnerableTime;
        }
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        super.setNoDamageTicks(ticks);
        getHandle().spawnInvulnerableTime = ticks; // SPIGOT-5921: Update both for players, like the getter above
    }

    @Override
    public void setFlySpeed(float value) {
        validateSpeed(value);
        ServerPlayerEntity player = getHandle();
        player.abilities.flyingSpeed = value / 2f;
        player.onUpdateAbilities();

    }

    @Override
    public void setWalkSpeed(float value) {
        validateSpeed(value);
        ServerPlayerEntity player = getHandle();
        player.abilities.walkingSpeed = value / 2f;
        player.onUpdateAbilities();
        getHandle().getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(player.abilities.walkingSpeed); // SPIGOT-5833: combination of the two in 1.16+
    }

    @Override
    public float getFlySpeed() {
        return getHandle().abilities.flyingSpeed * 2f;
    }

    @Override
    public float getWalkSpeed() {
        return getHandle().abilities.walkingSpeed * 2f;
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
        getHandle().resetSentInfo();
    }

    @Override
    public void resetMaxHealth() {
        super.resetMaxHealth();
        getHandle().resetSentInfo();
    }

    @Override
    public CraftScoreboard getScoreboard() {
        return this.server.getScoreboardManager().getPlayerBoard(this);
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) {
        Validate.notNull(scoreboard, "Scoreboard cannot be null");
        ServerPlayNetHandler connection = getHandle().connection;
        if (connection == null) {
            throw new IllegalStateException("Cannot set scoreboard yet");
        }
        if (connection.isDisconnected()) {
            // throw new IllegalStateException("Cannot set scoreboard for invalid CraftPlayer"); // Spigot - remove this as Mojang's semi asynchronous Netty implementation can lead to races
        }

        this.server.getScoreboardManager().setPlayerBoard(this, scoreboard);
    }

    @Override
    public void setHealthScale(double value) {
        Validate.isTrue((float) value > 0F, "Must be greater than 0");
        healthScale = value;
        scaledHealth = true;
        updateScaledHealth();
    }

    @Override
    public double getHealthScale() {
        return healthScale;
    }

    @Override
    public void setHealthScaled(boolean scale) {
        if (scaledHealth != (scaledHealth = scale)) {
            updateScaledHealth();
        }
    }

    @Override
    public boolean isHealthScaled() {
        return scaledHealth;
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
        AttributeModifierManager attributemapserver = getHandle().getAttributes();
        Collection<ModifiableAttributeInstance> set = attributemapserver.getSyncableAttributes(); // PAIL: Rename

        injectScaledMaxHealth(set, true);

        // SPIGOT-3813: Attributes before health
        if (getHandle().connection != null) {
            getHandle().connection.send(new SEntityPropertiesPacket(getHandle().getId(), set));
            if (sendHealth) {
                sendHealthUpdate();
            }
        }
        getHandle().getEntityData().set(LivingEntity.DATA_HEALTH_ID, getScaledHealth());

        getHandle().maxHealthCache = getMaxHealth();
    }

    public void sendHealthUpdate() {
        // Mohist - compat for Forge
        if (getHandle().connection != null) {
            getHandle().connection.send(new SUpdateHealthPacket(getScaledHealth(), getHandle().getFoodData().getFoodLevel(), getHandle().getFoodData().getSaturationLevel()));
        }
    }

    public void injectScaledMaxHealth(Collection<ModifiableAttributeInstance> collection, boolean force) {
        if (!scaledHealth && !force) {
            return;
        }
        for (ModifiableAttributeInstance genericInstance : collection) {
            if (genericInstance.getAttribute() == Attributes.MAX_HEALTH) {
                collection.remove(genericInstance);
                break;
            }
        }
        ModifiableAttributeInstance dummy = new ModifiableAttributeInstance(Attributes.MAX_HEALTH, (attribute) -> {
        });
        // Spigot start
        double healthMod = scaledHealth ? healthScale : getMaxHealth();
        if (healthMod >= Float.MAX_VALUE || healthMod <= 0) {
            healthMod = 20; // Reset health
            getServer().getLogger().warning(getName() + " tried to crash the server with a large health attribute");
        }
        dummy.setBaseValue(healthMod);
        // Spigot end
        collection.add(dummy);
    }

    @Override
    public org.bukkit.entity.Entity getSpectatorTarget() {
        Entity followed = getHandle().getCamera();
        return followed == getHandle() ? null : followed.getBukkitEntity();
    }

    @Override
    public void setSpectatorTarget(org.bukkit.entity.Entity entity) {
        Preconditions.checkArgument(getGameMode() == GameMode.SPECTATOR, "Player must be in spectator mode");
        getHandle().setCamera((entity == null) ? null : ((org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity) entity).getHandle());
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        sendTitle(title, subtitle, 10, 70, 20);
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        STitlePacket times = new STitlePacket(fadeIn, stay, fadeOut);
        getHandle().connection.send(times);

        if (title != null) {
            STitlePacket packetTitle = new STitlePacket(STitlePacket.Type.TITLE, CraftChatMessage.fromStringOrNull(title));
            getHandle().connection.send(packetTitle);
        }

        if (subtitle != null) {
            STitlePacket packetSubtitle = new STitlePacket(STitlePacket.Type.SUBTITLE, CraftChatMessage.fromStringOrNull(subtitle));
            getHandle().connection.send(packetSubtitle);
        }
    }

    @Override
    public void resetTitle() {
        STitlePacket packetReset = new STitlePacket(STitlePacket.Type.RESET, null);
        getHandle().connection.send(packetReset);
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
        SSpawnParticlePacket packetplayoutworldparticles = new SSpawnParticlePacket(CraftParticle.toNMS(particle, data), true, (float) x, (float) y, (float) z, (float) offsetX, (float) offsetY, (float) offsetZ, (float) extra, count);
        getHandle().connection.send(packetplayoutworldparticles);

    }

    @Override
    public org.bukkit.advancement.AdvancementProgress getAdvancementProgress(org.bukkit.advancement.Advancement advancement) {
        Preconditions.checkArgument(advancement != null, "advancement");

        CraftAdvancement craft = (CraftAdvancement) advancement;
        PlayerAdvancements data = getHandle().getAdvancements();
        AdvancementProgress progress = data.getOrStartProgress(craft.getHandle());

        return new CraftAdvancementProgress(craft, data, progress);
    }

    @Override
    public int getClientViewDistance() {
        return (getHandle().clientViewDistance == null) ? Bukkit.getViewDistance() : getHandle().clientViewDistance;
    }

    // Paper start
    @Override
    public java.util.Locale locale() {
        return getHandle().adventure$locale;
    }
    // Paper end

    @Override
    public int getPing() {
        return getHandle().latency;
    }

    @Override
    public String getLocale() {
        return getHandle().language;
    }

    // Paper start
    public void setAffectsSpawning(boolean affects) {
        this.getHandle().affectsSpawning = affects;
    }

    @Override
    public boolean getAffectsSpawning() {
        return this.getHandle().affectsSpawning;
    }
    // Paper end

    @Override
    public void updateCommands() {
        if (getHandle().connection == null) return;

        getHandle().server.getCommands().sendCommands(getHandle());
    }

    @Override
    public void openBook(ItemStack book) {
        Validate.isTrue(book != null, "book == null");
        Validate.isTrue(book.getType() == Material.WRITTEN_BOOK, "Book must be Material.WRITTEN_BOOK");

        ItemStack hand = getInventory().getItemInMainHand();
        getInventory().setItemInMainHand(book);
        getHandle().openItemGui(org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asNMSCopy(book), net.minecraft.util.Hand.MAIN_HAND);
        getInventory().setItemInMainHand(hand);
    }

    // Paper start
    @Override
    public net.kyori.adventure.text.Component displayName() {
        return this.getHandle().adventure$displayName;
    }

    @Override
    public void displayName(final net.kyori.adventure.text.Component displayName) {
        this.getHandle().adventure$displayName = displayName != null ? displayName : net.kyori.adventure.text.Component.text(this.getName());
        this.getHandle().displayName = null;
    }

    @Override
    public void sendMessage(final net.kyori.adventure.identity.Identity identity, final net.kyori.adventure.text.Component message, final net.kyori.adventure.audience.MessageType type) {
        final SChatPacket packet = new SChatPacket(null, type == net.kyori.adventure.audience.MessageType.CHAT ? ChatType.CHAT : ChatType.SYSTEM, identity.uuid());
        packet.adventure$message = message;
        this.getHandle().connection.send(packet);
    }

    @Override
    public void sendActionBar(final net.kyori.adventure.text.Component message) {
        final STitlePacket packet = new STitlePacket(STitlePacket.Type.ACTIONBAR, null);
        packet.adventure$text = message;
        this.getHandle().connection.send(packet);
    }

    @Override
    public void sendPlayerListHeader(final net.kyori.adventure.text.Component header) {
        this.playerListHeader = header;
        this.adventure$sendPlayerListHeaderAndFooter();
    }

    @Override
    public void sendPlayerListFooter(final net.kyori.adventure.text.Component footer) {
        this.playerListFooter = footer;
        this.adventure$sendPlayerListHeaderAndFooter();
    }

    @Override
    public void sendPlayerListHeaderAndFooter(final net.kyori.adventure.text.Component header, final net.kyori.adventure.text.Component footer) {
        this.playerListHeader = header;
        this.playerListFooter = footer;
        this.adventure$sendPlayerListHeaderAndFooter();
    }

    private void adventure$sendPlayerListHeaderAndFooter() {
        final ServerPlayNetHandler connection = this.getHandle().connection;
        if (connection == null) return;
        final SPlayerListHeaderFooterPacket packet = new SPlayerListHeaderFooterPacket();
        packet.adventure$header = (this.playerListHeader == null) ? net.kyori.adventure.text.Component.empty() : this.playerListHeader;
        packet.adventure$footer = (this.playerListFooter == null) ? net.kyori.adventure.text.Component.empty() : this.playerListFooter;
        connection.send(packet);
    }

    @Override
    public void showTitle(final net.kyori.adventure.title.Title title) {
        final ServerPlayNetHandler connection = this.getHandle().connection;
        final net.kyori.adventure.title.Title.Times times = title.times();
        if (times != null) {
            connection.send(new STitlePacket(ticks(times.fadeIn()), ticks(times.stay()), ticks(times.fadeOut())));
        }
        final STitlePacket sp = new STitlePacket(STitlePacket.Type.SUBTITLE, null);
        sp.adventure$text = title.subtitle();
        connection.send(sp);
        final STitlePacket tp = new STitlePacket(STitlePacket.Type.TITLE, null);
        tp.adventure$text = title.title();
        connection.send(tp);
    }

    private static int ticks(final java.time.Duration duration) {
        if (duration == null) {
            return -1;
        }
        return (int) (duration.toMillis() / 50L);
    }

    @Override
    public void clearTitle() {
        this.getHandle().connection.send(new STitlePacket(STitlePacket.Type.CLEAR, null));
    }

    // resetTitle implemented above

    @Override
    public void showBossBar(final net.kyori.adventure.bossbar.BossBar bar) {
        ((net.kyori.adventure.bossbar.HackyBossBarPlatformBridge) bar).paper$playerShow(this);
    }

    @Override
    public void hideBossBar(final net.kyori.adventure.bossbar.BossBar bar) {
        ((net.kyori.adventure.bossbar.HackyBossBarPlatformBridge) bar).paper$playerHide(this);
    }

    @Override
    public void playSound(final net.kyori.adventure.sound.Sound sound) {
        final Vector3d pos = this.getHandle().position();
        this.playSound(sound, pos.x, pos.y, pos.z);
    }

    @Override
    public void playSound(final net.kyori.adventure.sound.Sound sound, final double x, final double y, final double z) {
        final ResourceLocation name = io.papermc.paper.adventure.PaperAdventure.asVanilla(sound.name());
        final java.util.Optional<SoundEvent> event = Registry.SOUND_EVENT.getOptional(name);
        if (event.isPresent()) {
            this.getHandle().connection.send(new SPlaySoundEffectPacket(event.get(), io.papermc.paper.adventure.PaperAdventure.asVanilla(sound.source()), x, y, z, sound.volume(), sound.pitch()));
        } else {
            this.getHandle().connection.send(new SPlaySoundPacket(name, io.papermc.paper.adventure.PaperAdventure.asVanilla(sound.source()), new Vector3d(x, y, z), sound.volume(), sound.pitch()));
        }
    }

    @Override
    public void stopSound(final net.kyori.adventure.sound.SoundStop stop) {
        this.getHandle().connection.send(new SStopSoundPacket(
                io.papermc.paper.adventure.PaperAdventure.asVanillaNullable(stop.sound()),
                io.papermc.paper.adventure.PaperAdventure.asVanillaNullable(stop.source())
        ));
    }

    @Override
    public void openBook(final net.kyori.adventure.inventory.Book book) {
        final java.util.Locale locale = this.getHandle().adventure$locale;
        final net.minecraft.item.ItemStack item = io.papermc.paper.adventure.PaperAdventure.asItemStack(book, locale);
        final ServerPlayerEntity player = this.getHandle();
        final ServerPlayNetHandler connection = player.connection;
        final PlayerInventory inventory = player.inventory;
        final int slot = inventory.items.size() + inventory.selected;
        connection.send(new SSetSlotPacket(0, slot, item));
        connection.send(new SOpenBookWindowPacket(Hand.MAIN_HAND));
        connection.send(new SSetSlotPacket(0, slot, inventory.getSelected()));
    }
    // Paper end

    // Spigot start
    private final Player.Spigot spigot = new Player.Spigot() {

        @Override
        public InetSocketAddress getRawAddress() {
            return (InetSocketAddress) getHandle().connection.connection.getRawAddress();
        }

        @Override
        public boolean getCollidesWithEntities() {
            return CraftPlayer.this.isCollidable();
        }

        @Override
        public void setCollidesWithEntities(boolean collides) {
            CraftPlayer.this.setCollidable(collides);
        }

        @Override
        public void respawn() {
            if (getHealth() <= 0 && isOnline()) {
                server.getServer().getPlayerList().respawn(getHandle(), false);
            }
        }

        @Override
        public Set<Player> getHiddenPlayers() {
            Set<Player> ret = new HashSet<Player>();
            for (UUID u : hiddenPlayers.keySet()) {
                ret.add(getServer().getPlayer(u));
            }
            return java.util.Collections.unmodifiableSet(ret);
        }

        @Override
        public void sendMessage(BaseComponent component) {
            sendMessage(new BaseComponent[]{component});
        }

        @Override
        public void sendMessage(BaseComponent... components) {
            if (getHandle().connection == null) return;
            SChatPacket packet = new SChatPacket(null, ChatType.SYSTEM, Util.NIL_UUID);
            packet.components = components;
            getHandle().connection.send(packet);
        }

        @Override
        public void sendMessage(UUID sender, BaseComponent component) {
            this.sendMessage(net.md_5.bungee.api.ChatMessageType.CHAT, sender, component);
        }

        @Override
        public void sendMessage(UUID sender, BaseComponent... components) {
            this.sendMessage(net.md_5.bungee.api.ChatMessageType.CHAT, sender, components);
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, BaseComponent component) {
            sendMessage(position, new BaseComponent[]{component});
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, BaseComponent... components) {
            if (getHandle().connection == null) return;
            SChatPacket packet = new SChatPacket(null, ChatType.getForIndex((byte) position.ordinal()), Util.NIL_UUID);
            packet.components = components;
            getHandle().connection.send(packet);
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, UUID sender, BaseComponent component) {
            sendMessage(position, sender, new BaseComponent[]{component});
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, UUID sender, BaseComponent... components) {
            if (getHandle().connection == null) return;

            SChatPacket packet = new SChatPacket(null, ChatType.getForIndex((byte) position.ordinal()), sender == null ? Util.NIL_UUID : sender);
            packet.components = components;
            getHandle().connection.send(packet);
        }

        // Paper start
        @Override
        public int getPing() {
            return getHandle().latency;
        }
        // Paper end
    };

    @Override
    public Player.Spigot spigot() {
        return spigot;
    }
    // Spigot end
}
