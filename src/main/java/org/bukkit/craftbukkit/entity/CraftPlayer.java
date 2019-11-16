package org.bukkit.craftbukkit.entity;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.MapMaker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.mojang.authlib.GameProfile;

import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.world.WorldServer;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.*;
import org.bukkit.Statistic.Type;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.bukkit.craftbukkit.block.CraftSign;
import org.bukkit.craftbukkit.conversations.ConversationTracker;
import org.bukkit.craftbukkit.CraftEffect;
import org.bukkit.craftbukkit.CraftOfflinePlayer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.craftbukkit.CraftStatistic;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.map.CraftMapView;
import org.bukkit.craftbukkit.map.RenderData;
import org.bukkit.craftbukkit.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.scoreboard.Scoreboard;

@DelegateDeserialization(CraftOfflinePlayer.class)
public class CraftPlayer extends CraftHumanEntity implements Player {
    private long firstPlayed = 0;
    private long lastPlayed = 0;
    private boolean hasPlayedBefore = false;
    private final ConversationTracker conversationTracker = new ConversationTracker();
    private final Set<String> channels = new HashSet<String>();
    private final Set<UUID> hiddenPlayers = new HashSet<UUID>();
    private int hash = 0;
    private double health = 20;
    private boolean scaledHealth = false;
    private double healthScale = 20;

    public CraftPlayer(CraftServer server, net.minecraft.entity.player.EntityPlayerMP entity) {
        super(server, entity);

        firstPlayed = System.currentTimeMillis();
    }

    public GameProfile getProfile() {
        return getHandle().getGameProfile();
    }

    @Override
    public boolean isOp() {
        return server.getHandle().func_152596_g(getProfile());
    }

    @Override
    public void setOp(boolean value) {
        if (value == isOp()) return;

        if (value) {
            server.getHandle().func_152605_a(getProfile());
        } else {
            server.getHandle().func_152610_b(getProfile());
        }

        perm.recalculatePermissions();
    }

    public boolean isOnline() {
        if (this.getHandle() instanceof net.minecraftforge.common.util.FakePlayer)
	{
	    return true;
	}
        for (Object obj : server.getHandle().playerEntityList) {
            net.minecraft.entity.player.EntityPlayerMP player = (net.minecraft.entity.player.EntityPlayerMP) obj;
            if (player != null && (this.getHandle() == player || player.getBukkitEntity() == this || this.getHandle().getGameProfile().getId().equals(player.getGameProfile().getId())))
            {
                return true;
            }
        }
        return false;
    }

    public InetSocketAddress getAddress() {
        if (getHandle().playerNetServerHandler == null) return null;

        SocketAddress addr = getHandle().playerNetServerHandler.netManager.getSocketAddress();
        if (addr instanceof InetSocketAddress) {
            return (InetSocketAddress) addr;
        } else {
            return null;
        }
    }

    @Override
    public double getEyeHeight() {
        return getEyeHeight(false);
    }

    @Override
    public double getEyeHeight(boolean ignoreSneaking) {
        if (ignoreSneaking) {
            return 1.62D;
        } else {
            if (isSneaking()) {
                return 1.54D;
            } else {
                return 1.62D;
            }
        }
    }

    @Override
    public void sendRawMessage(String message) {
        if (getHandle().playerNetServerHandler == null) return;

        for (net.minecraft.util.IChatComponent component : CraftChatMessage.fromString(message)) {
            getHandle().playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S02PacketChat(component));
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
        return getHandle().listName;
    }

    @Override
    public void setPlayerListName(String name) {
        String oldName = getHandle().listName;

        if (name == null) {
            name = getName();
        }

        if (oldName.equals(name)) {
            return;
        }

        if (name.length() > 16) {
            throw new IllegalArgumentException("Player list names can only be a maximum of 16 characters long");
        }

        // Collisions will make for invisible people
        for (int i = 0; i < server.getHandle().playerEntityList.size(); ++i) {
            if (((net.minecraft.entity.player.EntityPlayerMP) server.getHandle().playerEntityList.get(i)).listName.equals(name)) {
                throw new IllegalArgumentException(name + " is already assigned as a player list name for someone");
            }
        }

        getHandle().listName = name;

        // Change the name on the client side
        net.minecraft.network.play.server.S38PacketPlayerListItem oldpacket = new net.minecraft.network.play.server.S38PacketPlayerListItem(oldName, false, 9999);
        net.minecraft.network.play.server.S38PacketPlayerListItem packet = new net.minecraft.network.play.server.S38PacketPlayerListItem(name, true, getHandle().ping);
        for (int i = 0; i < server.getHandle().playerEntityList.size(); ++i) {
            net.minecraft.entity.player.EntityPlayerMP entityplayer = (net.minecraft.entity.player.EntityPlayerMP) server.getHandle().playerEntityList.get(i);
            if (entityplayer.playerNetServerHandler == null) continue;

            if (entityplayer.getBukkitEntity().canSee(this)) {
                entityplayer.playerNetServerHandler.sendPacket(oldpacket);
                entityplayer.playerNetServerHandler.sendPacket(packet);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OfflinePlayer)) {
            return false;
        }
        OfflinePlayer other = (OfflinePlayer) obj;
        if ((this.getName() == null) || (other.getName() == null)) {
            return false;
        }

        boolean nameEquals = this.getName().equalsIgnoreCase(other.getName());
        boolean idEquals = true;

        if (other instanceof CraftPlayer) {
            idEquals = this.getEntityId() == ((CraftPlayer) other).getEntityId();
        }

        return nameEquals && idEquals;
    }

    @Override
    public void kickPlayer(String message) {
        if (Thread.currentThread() != net.minecraft.server.MinecraftServer.getServer().primaryThread) throw new IllegalStateException("Asynchronous player kick!"); // Spigot
        if (getHandle().playerNetServerHandler == null) return;

        getHandle().playerNetServerHandler.kickPlayerFromServer(message == null ? "" : message);
    }

    @Override
    public void setCompassTarget(Location loc) {
        if (getHandle().playerNetServerHandler == null) return;

        // Do not directly assign here, from the packethandler we'll assign it.
        getHandle().playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S05PacketSpawnPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    }

    @Override
    public Location getCompassTarget() {
        return getHandle().compassTarget;
    }

    @Override
    public void chat(String msg) {
        if (getHandle().playerNetServerHandler == null) return;

        getHandle().playerNetServerHandler.chat(msg, false);
    }

    @Override
    public boolean performCommand(String command) {
        return server.dispatchCommand(this, command);
    }

    @Override
    public void playNote(Location loc, byte instrument, byte note) {
        if (getHandle().playerNetServerHandler == null) return;

        String instrumentName = null;
        switch (instrument) {
        case 0:
            instrumentName = "harp";
            break;
        case 1:
            instrumentName = "bd";
            break;
        case 2:
            instrumentName = "snare";
            break;
        case 3:
            instrumentName = "hat";
            break;
        case 4:
            instrumentName = "bassattack";
            break;
        }
        getHandle().playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S29PacketSoundEffect("note."+instrumentName, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 3.0f, note));
    }

    @Override
    public void playNote(Location loc, Instrument instrument, Note note) {
        if (getHandle().playerNetServerHandler == null) return;

        String instrumentName = null;
        switch (instrument.ordinal()) {
            case 0:
                instrumentName = "harp";
                break;
            case 1:
                instrumentName = "bd";
                break;
            case 2:
                instrumentName = "snare";
                break;
            case 3:
                instrumentName = "hat";
                break;
            case 4:
                instrumentName = "bassattack";
                break;
        }
        getHandle().playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S29PacketSoundEffect("note."+instrumentName, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 3.0f, note.getId()));
    }

    @Override
    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        if (sound == null) {
            return;
        }
        playSound(loc, CraftSound.getSound(sound), volume, pitch);
    }

    @Override
    public void playSound(Location loc, String sound, float volume, float pitch) {
        if (loc == null || sound == null || getHandle().playerNetServerHandler == null) return;

        double x = loc.getBlockX() + 0.5;
        double y = loc.getBlockY() + 0.5;
        double z = loc.getBlockZ() + 0.5;

        net.minecraft.network.play.server.S29PacketSoundEffect packet = new net.minecraft.network.play.server.S29PacketSoundEffect(sound, x, y, z, volume, pitch);
        getHandle().playerNetServerHandler.sendPacket(packet);
    }

    @Override
    public void playEffect(Location loc, Effect effect, int data) {
        if (getHandle().playerNetServerHandler == null) return;

        int packetData = effect.getId();
        net.minecraft.network.play.server.S28PacketEffect packet = new net.minecraft.network.play.server.S28PacketEffect(packetData, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), data, false);
        getHandle().playerNetServerHandler.sendPacket(packet);
    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data) {
        if (data != null) {
            Validate.isTrue(data.getClass().equals(effect.getData()), "Wrong kind of data for this effect!");
        } else {
            Validate.isTrue(effect.getData() == null, "Wrong kind of data for this effect!");
        }

        int datavalue = data == null ? 0 : CraftEffect.getDataValue(effect, data);
        playEffect(loc, effect, datavalue);
    }

    @Override
    public void sendBlockChange(Location loc, Material material, byte data) {
        sendBlockChange(loc, material.getId(), data);
    }

    @Override
    public void sendBlockChange(Location loc, int material, byte data) {
        if (getHandle().playerNetServerHandler == null) return;

        S23PacketBlockChange packet = new S23PacketBlockChange(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), ((CraftWorld) loc.getWorld()).getHandle());

        packet.field_148883_d = CraftMagicNumbers.getBlock(material);
        packet.field_148884_e = data;
        getHandle().playerNetServerHandler.sendPacket(packet);
    }

    @Override
    public void sendSignChange(Location loc, String[] lines) {
        if (getHandle().playerNetServerHandler == null) {
            return;
        }

        if (lines == null) {
            lines = new String[4];
        }

        Validate.notNull(loc, "Location can not be null");
        if (lines.length < 4) {
            throw new IllegalArgumentException("Must have at least 4 lines");
        }

        // Limit to 15 chars per line and set null lines to blank
        String[] astring = CraftSign.sanitizeLines(lines);

        getHandle().playerNetServerHandler.sendPacket(new S33PacketUpdateSign(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), astring));
    }

    @Override
    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        if (getHandle().playerNetServerHandler == null) return false;

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
        if (getHandle().playerNetServerHandler == null) return;

        RenderData data = ((CraftMapView) map).render(this);
        for (int x = 0; x < 128; ++x) {
            byte[] bytes = new byte[131];
            bytes[1] = (byte) x;
            for (int y = 0; y < 128; ++y) {
                bytes[y + 3] = data.buffer[y * 128 + x];
            }
            net.minecraft.network.play.server.S34PacketMaps packet = new net.minecraft.network.play.server.S34PacketMaps(map.getId(), bytes);
            getHandle().playerNetServerHandler.sendPacket(packet);
        }
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        net.minecraft.entity.player.EntityPlayerMP entity = getHandle();

        if (getHealth() == 0 || entity.isDead || entity instanceof net.minecraftforge.common.util.FakePlayer) {
            return false;
        }

        if (entity.playerNetServerHandler == null || entity.playerNetServerHandler.isDisconnected()) {
            return false;
        }

        // Spigot Start
        // if (entity.vehicle != null || entity.passenger != null) {
        // return false;
        // }
        // Spigot End

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
        
        // Spigot Start
        eject();
        leaveVehicle();
        // Spigot End

        // Update the From Location
        from = event.getFrom();
        // Grab the new To Location dependent on whether the event was cancelled.
        to = event.getTo();
        // Grab the To and From World Handles.
        net.minecraft.world.WorldServer fromWorld = ((CraftWorld) from.getWorld()).getHandle();
        net.minecraft.world.WorldServer toWorld = ((CraftWorld) to.getWorld()).getHandle();
        // Close any foreign inventory
        if (getHandle().openContainer != getHandle().inventoryContainer) {
            getHandle().closeScreen();
        }

        // Check if the fromWorld and toWorld are the same.
        if (fromWorld == toWorld) {
            entity.playerNetServerHandler.teleport(to);
        } else {
        	//Thermos....transfer them correctly?!
            this.getHandle().mountEntity(null);
        	thermos.thermite.ThermiteTeleportationHandler.transferPlayerToDimension(this.getHandle(), toWorld.dimension, this.getHandle().mcServer.getConfigurationManager(), to.getWorld().getEnvironment()); 
        	 //this.getHandle().playerNetServerHandler.teleport(to);
        	 this.getHandle().playerNetServerHandler.teleport(to);
        	 //this.getHandle().playerNetServerHandler.setPlayerLocation(to.getX(), to.getY(), to.getZ(), this.getHandle().rotationYaw, this.getHandle().rotationPitch);
        	//server.getHandle().respawnPlayer(entity, toWorld.dimension, false, to, false); // Cauldron
        }
        return true;
    }

    @Override
    public void setSneaking(boolean sneak) {
        getHandle().setSneaking(sneak);
    }

    @Override
    public boolean isSneaking() {
        return getHandle().isSneaking();
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
        server.getHandle().playerNBTManagerObj.readPlayerData(getHandle());
    }

    @Override
    public void saveData() {
        server.getHandle().playerNBTManagerObj.writePlayerData(getHandle());
    }

    @Deprecated
    @Override
    public void updateInventory() {
        getHandle().sendContainerToPlayer(getHandle().openContainer);
    }

    @Override
    public void setSleepingIgnored(boolean isSleeping) {
        getHandle().fauxSleeping = isSleeping;
        ((CraftWorld) getWorld()).getHandle().checkSleepStatus();
    }

    @Override
    public boolean isSleepingIgnored() {
        return getHandle().fauxSleeping;
    }

    @Override
    public void awardAchievement(Achievement achievement) {
        Validate.notNull(achievement, "Achievement cannot be null");
        if (achievement.hasParent() && !hasAchievement(achievement.getParent())) {
            awardAchievement(achievement.getParent());
        }
        getHandle().func_147099_x().func_150873_a(getHandle(), CraftStatistic.getNMSAchievement(achievement), 1);
        getHandle().func_147099_x().func_150884_b(getHandle());
    }

    @Override
    public void removeAchievement(Achievement achievement) {
        Validate.notNull(achievement, "Achievement cannot be null");
        for (Achievement achieve : Achievement.values()) {
            if (achieve.getParent() == achievement && hasAchievement(achieve)) {
                removeAchievement(achieve);
            }
        }
        getHandle().func_147099_x().func_150873_a(getHandle(), CraftStatistic.getNMSAchievement(achievement), 0);
    }

    @Override
    public boolean hasAchievement(Achievement achievement) {
        Validate.notNull(achievement, "Achievement cannot be null");
        return getHandle().func_147099_x().hasAchievementUnlocked(CraftStatistic.getNMSAchievement(achievement));
    }

    @Override
    public void incrementStatistic(Statistic statistic) {
        incrementStatistic(statistic, 1);
    }

    @Override
    public void decrementStatistic(Statistic statistic) {
        decrementStatistic(statistic, 1);
    }

    @Override
    public int getStatistic(Statistic statistic) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.isTrue(statistic.getType() == Type.UNTYPED, "Must supply additional paramater for this statistic");
        return getHandle().func_147099_x().writeStat(CraftStatistic.getNMSStatistic(statistic));
    }

    @Override
    public void incrementStatistic(Statistic statistic, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(statistic, getStatistic(statistic) + amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(statistic, getStatistic(statistic) - amount);
    }

    @Override
    public void setStatistic(Statistic statistic, int newValue) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.isTrue(statistic.getType() == Type.UNTYPED, "Must supply additional paramater for this statistic");
        Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
        net.minecraft.stats.StatBase nmsStatistic = CraftStatistic.getNMSStatistic(statistic);
        getHandle().func_147099_x().func_150873_a(getHandle(), nmsStatistic, newValue);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material) {
        incrementStatistic(statistic, material, 1);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material) {
        decrementStatistic(statistic, material, 1);
    }

    @Override
    public int getStatistic(Statistic statistic, Material material) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.notNull(material, "Material cannot be null");
        Validate.isTrue(statistic.getType() == Type.BLOCK || statistic.getType() == Type.ITEM, "This statistic does not take a Material parameter");
        net.minecraft.stats.StatBase nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);
        Validate.notNull(nmsStatistic, "The supplied Material does not have a corresponding statistic");
        return getHandle().func_147099_x().writeStat(nmsStatistic);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(statistic, material, getStatistic(statistic, material) + amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(statistic, material, getStatistic(statistic, material) - amount);
    }

    @Override
    public void setStatistic(Statistic statistic, Material material, int newValue) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.notNull(material, "Material cannot be null");
        Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
        Validate.isTrue(statistic.getType() == Type.BLOCK || statistic.getType() == Type.ITEM, "This statistic does not take a Material parameter");
        net.minecraft.stats.StatBase nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);
        Validate.notNull(nmsStatistic, "The supplied Material does not have a corresponding statistic");
        getHandle().func_147099_x().func_150873_a(getHandle(), nmsStatistic, newValue);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType) {
        incrementStatistic(statistic, entityType, 1);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType) {
        decrementStatistic(statistic, entityType, 1);
    }

    @Override
    public int getStatistic(Statistic statistic, EntityType entityType) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.notNull(entityType, "EntityType cannot be null");
        Validate.isTrue(statistic.getType() == Type.ENTITY, "This statistic does not take an EntityType parameter");
        net.minecraft.stats.StatBase nmsStatistic = CraftStatistic.getEntityStatistic(statistic, entityType);
        Validate.notNull(nmsStatistic, "The supplied EntityType does not have a corresponding statistic");
        return getHandle().func_147099_x().writeStat(nmsStatistic);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(statistic, entityType, getStatistic(statistic, entityType) + amount);
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        Validate.isTrue(amount > 0, "Amount must be greater than 0");
        setStatistic(statistic, entityType, getStatistic(statistic, entityType) - amount);
    }

    @Override
    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
        Validate.notNull(statistic, "Statistic cannot be null");
        Validate.notNull(entityType, "EntityType cannot be null");
        Validate.isTrue(newValue >= 0, "Value must be greater than or equal to 0");
        Validate.isTrue(statistic.getType() == Type.ENTITY, "This statistic does not take an EntityType parameter");
        net.minecraft.stats.StatBase nmsStatistic = CraftStatistic.getEntityStatistic(statistic, entityType);
        Validate.notNull(nmsStatistic, "The supplied EntityType does not have a corresponding statistic");
        getHandle().func_147099_x().func_150873_a(getHandle(), nmsStatistic, newValue);
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
    public void setBanned(boolean value) {
        if (value) {
            server.getBanList(BanList.Type.NAME).addBan(getName(), null, null, null);
        } else {
            server.getBanList(BanList.Type.NAME).pardon(getName());
        }
    }

    @Override
    public boolean isWhitelisted() {
        return server.getHandle().func_152607_e(getProfile());
    }

    @Override
    public void setWhitelisted(boolean value) {
        if (value) {
            server.getHandle().func_152601_d(getProfile());
        } else {
            server.getHandle().func_152597_c(getProfile());
        }
    }

    @Override
    public void setGameMode(GameMode mode) {
        if (getHandle().playerNetServerHandler == null) return;

        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null");
        }

        if (mode != getGameMode()) {
            PlayerGameModeChangeEvent event = new PlayerGameModeChangeEvent(this, mode);
            server.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }

            getHandle().theItemInWorldManager.setGameType(net.minecraft.world.WorldSettings.GameType.getByID(mode.getValue()));
            getHandle().fallDistance = 0;
            getHandle().playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S2BPacketChangeGameState(3, mode.getValue()));
        }
    }

    @Override
    public GameMode getGameMode() {
        return GameMode.getByValue(getHandle().theItemInWorldManager.getGameType().getID());
    }

    public void giveExp(int exp) {
        getHandle().addExperience(exp);
    }

    public void giveExpLevels(int levels) {
        getHandle().addExperienceLevel(levels);
    }

    public float getExp() {
        return getHandle().experience;
    }

    public void setExp(float exp) {
        getHandle().experience = exp;
        getHandle().lastExperience = -1;
    }

    public int getLevel() {
        return getHandle().experienceLevel;
    }

    public void setLevel(int level) {
        getHandle().experienceLevel = level;
        getHandle().lastExperience = -1;
    }

    public int getTotalExperience() {
        return getHandle().experienceTotal;
    }

    public void setTotalExperience(int exp) {
        getHandle().experienceTotal = exp;
    }

    public float getExhaustion() {
        return getHandle().getFoodStats().foodExhaustionLevel;
    }

    public void setExhaustion(float value) {
        getHandle().getFoodStats().foodExhaustionLevel = value;
    }

    public float getSaturation() {
        return getHandle().getFoodStats().foodSaturationLevel;
    }

    public void setSaturation(float value) {
        getHandle().getFoodStats().foodSaturationLevel = value;
    }

    public int getFoodLevel() {
        return getHandle().getFoodStats().foodLevel;
    }

    public void setFoodLevel(int value) {
        getHandle().getFoodStats().foodLevel = value;
    }

    public Location getBedSpawnLocation() {
        World world = getServer().getWorld(getHandle().spawnWorld);
        net.minecraft.util.ChunkCoordinates bed = getHandle().getBedLocation();

        if (world != null && bed != null) {
            bed = net.minecraft.entity.player.EntityPlayer.verifyRespawnCoordinates(((CraftWorld) world).getHandle(), bed, getHandle().isSpawnForced());
            if (bed != null) {
                return new Location(world, bed.posX, bed.posY, bed.posZ);
            }
        }
        return null;
    }

    public void setBedSpawnLocation(Location location) {
        setBedSpawnLocation(location, false);
    }

    public void setBedSpawnLocation(Location location, boolean override) {
        if (location == null) {
            getHandle().setSpawnChunk(null, override);
        } else {
            getHandle().setSpawnChunk(new net.minecraft.util.ChunkCoordinates(location.getBlockX(), location.getBlockY(), location.getBlockZ()), override);
            getHandle().spawnWorld = location.getWorld().getName();
        }
    }

    public void hidePlayer(Player player) {
        Validate.notNull(player, "hidden player cannot be null");
        if (getHandle().playerNetServerHandler == null) return;
        if (equals(player)) return;
        if (hiddenPlayers.contains(player.getUniqueId())) return;
        hiddenPlayers.add(player.getUniqueId());

        //remove this player from the hidden player's EntityTrackerEntry
        net.minecraft.entity.EntityTracker tracker = ((net.minecraft.world.WorldServer) entity.worldObj).theEntityTracker;
        net.minecraft.entity.player.EntityPlayerMP other = ((CraftPlayer) player).getHandle();
        net.minecraft.entity.EntityTrackerEntry entry = (net.minecraft.entity.EntityTrackerEntry) tracker.trackedEntityIDs.lookup(other.getEntityId());
        if (entry != null) {
            entry.removePlayerFromTracker(getHandle());
        }

        //remove the hidden player from this player user list
        getHandle().playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S38PacketPlayerListItem(player.getPlayerListName(), false, 9999));
    }

    public void showPlayer(Player player) {
        Validate.notNull(player, "shown player cannot be null");
        if (getHandle().playerNetServerHandler == null) return;
        if (equals(player)) return;
        if (!hiddenPlayers.contains(player.getUniqueId())) return;
        hiddenPlayers.remove(player.getUniqueId());

        EntityTracker tracker = ((WorldServer) entity.worldObj).theEntityTracker;
        EntityPlayerMP other = ((CraftPlayer) player).getHandle();
        EntityTrackerEntry entry = (EntityTrackerEntry) tracker.trackedEntityIDs.lookup(other.getEntityId());
        if (entry != null) {
            entry.removePlayerFromTracker(getHandle());
        }

        getHandle().playerNetServerHandler.sendPacket(new S38PacketPlayerListItem(player.getPlayerListName(), false, 9999));
    }

    public void removeDisconnectingPlayer(Player player) {
        hiddenPlayers.remove(player.getUniqueId());
    }

    public boolean canSee(Player player) {
        return !hiddenPlayers.contains(player.getUniqueId());
    }

    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        result.put("name", getName());

        return result;
    }

    public Player getPlayer() {
        return this;
    }

    @Override
    public net.minecraft.entity.player.EntityPlayerMP getHandle() {
        return (net.minecraft.entity.player.EntityPlayerMP) entity;
    }

    public void setHandle(final net.minecraft.entity.player.EntityPlayerMP entity) {
        super.setHandle(entity);
    }

    @Override
    public String toString() {
        return "CraftPlayer{" + "name=" + getName() + '}';
    }

    @Override
    public int hashCode() {
        if (hash == 0 || hash == 485) {
            hash = 97 * 5 + (this.getName() != null ? this.getName().toLowerCase().hashCode() : 0);
        }
        return hash;
    }

    public long getFirstPlayed() {
        return firstPlayed;
    }

    public long getLastPlayed() {
        return lastPlayed;
    }

    public boolean hasPlayedBefore() {
        return hasPlayedBefore;
    }

    public void setFirstPlayed(long firstPlayed) {
        this.firstPlayed = firstPlayed;
    }

    public void readExtraData(net.minecraft.nbt.NBTTagCompound nbttagcompound) {
        hasPlayedBefore = true;
        if (nbttagcompound.hasKey("bukkit")) {
            net.minecraft.nbt.NBTTagCompound data = nbttagcompound.getCompoundTag("bukkit");

            if (data.hasKey("firstPlayed")) {
                firstPlayed = data.getLong("firstPlayed");
                lastPlayed = data.getLong("lastPlayed");
            }

            if (data.hasKey("newExp")) {
                net.minecraft.entity.player.EntityPlayerMP handle = getHandle();
                handle.newExp = data.getInteger("newExp");
                handle.newTotalExp = data.getInteger("newTotalExp");
                handle.newLevel = data.getInteger("newLevel");
                handle.expToDrop = data.getInteger("expToDrop");
                handle.keepLevel = data.getBoolean("keepLevel");
            }
        }
    }

    public void setExtraData(net.minecraft.nbt.NBTTagCompound nbttagcompound) {
        if (!nbttagcompound.hasKey("bukkit")) {
            nbttagcompound.setTag("bukkit", new net.minecraft.nbt.NBTTagCompound());
        }

        net.minecraft.nbt.NBTTagCompound data = nbttagcompound.getCompoundTag("bukkit");
        net.minecraft.entity.player.EntityPlayerMP handle = getHandle();
        data.setInteger("newExp", handle.newExp);
        data.setInteger("newTotalExp", handle.newTotalExp);
        data.setInteger("newLevel", handle.newLevel);
        data.setInteger("expToDrop", handle.expToDrop);
        data.setBoolean("keepLevel", handle.keepLevel);
        data.setLong("firstPlayed", getFirstPlayed());
        data.setLong("lastPlayed", System.currentTimeMillis());
        data.setString("lastKnownName", handle.getCommandSenderName());
    }

    public boolean beginConversation(Conversation conversation) {
        return conversationTracker.beginConversation(conversation);
    }

    public void abandonConversation(Conversation conversation) {
        conversationTracker.abandonConversation(conversation, new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
    }

    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        conversationTracker.abandonConversation(conversation, details);
    }

    public void acceptConversationInput(String input) {
        conversationTracker.acceptConversationInput(input);
    }

    public boolean isConversing() {
        return conversationTracker.isConversing();
    }

    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(server.getMessenger(), source, channel, message);
        if (getHandle().playerNetServerHandler == null) return;

        if (channels.contains(channel)) {
            net.minecraft.network.play.server.S3FPacketCustomPayload packet = new net.minecraft.network.play.server.S3FPacketCustomPayload(channel, message);
            getHandle().playerNetServerHandler.sendPacket(packet);
        }
    }

    public void setTexturePack(String url) {
        setResourcePack(url);
    }

    @Override
    public void setResourcePack(String url) {
        Validate.notNull(url, "Resource pack URL cannot be null");

        getHandle().requestTexturePackLoad(url); // should be setResourcePack
    }

    public void addChannel(String channel) {
        if (channels.add(channel)) {
            server.getPluginManager().callEvent(new PlayerRegisterChannelEvent(this, channel));
        }
    }

    public void removeChannel(String channel) {
        if (channels.remove(channel)) {
            server.getPluginManager().callEvent(new PlayerUnregisterChannelEvent(this, channel));
        }
    }

    public Set<String> getListeningPluginChannels() {
        return ImmutableSet.copyOf(channels);
    }

    public void sendSupportedChannels() {
        if (getHandle().playerNetServerHandler == null) return;
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

            getHandle().playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S3FPacketCustomPayload("REGISTER", stream.toByteArray()));
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
        net.minecraft.inventory.Container container = getHandle().openContainer;
        if (container.getBukkitView().getType() != prop.getType()) {
            return false;
        }
        getHandle().sendProgressBarUpdate(container, prop.getId(), value);
        return true;
    }

    public void disconnect(String reason) {
        conversationTracker.abandonAllConversations();
        perm.clearPermissions();
    }

    public boolean isFlying() {
        return getHandle().capabilities.isFlying;
    }

    public void setFlying(boolean value) {
        if (!getAllowFlight() && value) {
            throw new IllegalArgumentException("Cannot make player fly if getAllowFlight() is false");
        }

        getHandle().capabilities.isFlying = value;
        getHandle().sendPlayerAbilities();
    }

    public boolean getAllowFlight() {
        return getHandle().capabilities.allowFlying;
    }

    public void setAllowFlight(boolean value) {
        if (isFlying() && !value) {
            getHandle().capabilities.isFlying = false;
        }

        getHandle().capabilities.allowFlying = value;
        getHandle().sendPlayerAbilities();
    }

    @Override
    public int getNoDamageTicks() {
        if (getHandle().field_147101_bU > 0) {
            return Math.max(getHandle().field_147101_bU, getHandle().hurtResistantTime);
        } else {
            return getHandle().hurtResistantTime;
        }
    }

    public void setFlySpeed(float value) {
        validateSpeed(value);
        net.minecraft.entity.player.EntityPlayerMP player = getHandle();
        player.capabilities.flySpeed = Math.max( value, 0.0001f ) / 2f; // Spigot
        player.sendPlayerAbilities();

    }

    public void setWalkSpeed(float value) {
        validateSpeed(value);
        net.minecraft.entity.player.EntityPlayerMP player = getHandle();
        player.capabilities.walkSpeed = Math.max( value, 0.0001f ) / 2f; // Spigot
        player.sendPlayerAbilities();
    }

    public float getFlySpeed() {
        return getHandle().capabilities.flySpeed * 2f;
    }

    public float getWalkSpeed() {
        return getHandle().capabilities.walkSpeed * 2f;
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
        getHandle().setPlayerHealthUpdated();
    }

    @Override
    public void resetMaxHealth() {
        super.resetMaxHealth();
        getHandle().setPlayerHealthUpdated();
    }

    public CraftScoreboard getScoreboard() {
        return this.server.getScoreboardManager().getPlayerBoard(this);
    }

    public void setScoreboard(Scoreboard scoreboard) {
        Validate.notNull(scoreboard, "Scoreboard cannot be null");
        net.minecraft.network.NetHandlerPlayServer playerConnection = getHandle().playerNetServerHandler;
        if (playerConnection == null) {
            throw new IllegalStateException("Cannot set scoreboard yet");
        }
        if (playerConnection.isDisconnected()) {
            // throw new IllegalStateException("Cannot set scoreboard for invalid CraftPlayer"); // Spigot - remove this as Mojang's semi asynchronous Netty implementation can lead to races
        }

        this.server.getScoreboardManager().setPlayerBoard(this, scoreboard);
    }

    public void setHealthScale(double value) {
        Validate.isTrue((float) value > 0F, "Must be greater than 0");
        healthScale = value;
        scaledHealth = true;
        updateScaledHealth();
    }

    public double getHealthScale() {
        return healthScale;
    }

    public void setHealthScaled(boolean scale) {
        if (scaledHealth != (scaledHealth = scale)) {
            updateScaledHealth();
        }
    }

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
        net.minecraft.entity.ai.attributes.ServersideAttributeMap attributemapserver = (net.minecraft.entity.ai.attributes.ServersideAttributeMap) getHandle().getAttributeMap();
        Set set = attributemapserver.getAttributeInstanceSet();

        injectScaledMaxHealth(set, true);

        getHandle().getDataWatcher().updateObject(6, (float) getScaledHealth());
        getHandle().playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S06PacketUpdateHealth(getScaledHealth(), getHandle().getFoodStats().getFoodLevel(), getHandle().getFoodStats().getSaturationLevel()));
        getHandle().playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S20PacketEntityProperties(getHandle().getEntityId(), set));

        set.clear();
        getHandle().maxHealthCache = getMaxHealth();
    }

    public void injectScaledMaxHealth(Collection collection, boolean force) {
        if (!scaledHealth && !force) {
            return;
        }
        for (Object genericInstance : collection) {
            net.minecraft.entity.ai.attributes.IAttribute attribute = ((net.minecraft.entity.ai.attributes.IAttributeInstance) genericInstance).getAttribute();
            if (attribute.getAttributeUnlocalizedName().equals("generic.maxHealth")) {
                collection.remove(genericInstance);
                break;
            }
            continue;
        }
        collection.add(new net.minecraft.entity.ai.attributes.ModifiableAttributeInstance(getHandle().getAttributeMap(), (new net.minecraft.entity.ai.attributes.RangedAttribute("generic.maxHealth", scaledHealth ? healthScale : getMaxHealth(), 0.0D, Float.MAX_VALUE)).setDescription("Max Health").setShouldWatch(true)));
    }

    // Spigot start
    private final Player.Spigot spigot = new Player.Spigot()
    {
        @Override
        public InetSocketAddress getRawAddress()
        {
            return (InetSocketAddress) getHandle().playerNetServerHandler.netManager.getRawAddress();
        }

        @Override
        public boolean getCollidesWithEntities()
        {
            return getHandle().collidesWithEntities;
        }

        @Override
        public void setCollidesWithEntities(boolean collides)
        {
            getHandle().collidesWithEntities = collides;
            getHandle().preventEntitySpawning = collides; // First boolean of Entity
        }

        @Override
        public void respawn()
        {
            if ( getHealth() <= 0 && isOnline() )
            {
                server.getServer().getConfigurationManager().respawnPlayer( getHandle(), 0 );
            }
        }

        @Override
        public String getLocale()
        {
           return getHandle().translator;
        }

        @Override
        public void sendMessage(BaseComponent component) {
            this.sendMessage(new BaseComponent[]{component});
        }

        @Override
        public /* varargs */ void sendMessage(BaseComponent ... components) {
            if (CraftPlayer.this.getHandle().playerNetServerHandler == null) {
                return;
            }
            S02PacketChat packet = new S02PacketChat();
            packet.components = components;
            CraftPlayer.this.getHandle().playerNetServerHandler.sendPacket(packet);
        }

    };

    public Player.Spigot spigot()
    {
        return spigot;
    }
    // Spigot end
}
