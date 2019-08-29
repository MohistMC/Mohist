package com.destroystokyo.paper.profile;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.spigotmc.SpigotConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class CraftPlayerProfile implements PlayerProfile {

    private final PropertySet properties = new PropertySet();
    private GameProfile profile;

    public CraftPlayerProfile(CraftPlayer player) {
        this.profile = player.getHandle().getGameProfile();
    }

    public CraftPlayerProfile(UUID id, String name) {
        this.profile = new GameProfile(id, name);
    }

    public CraftPlayerProfile(GameProfile profile) {
        this.profile = profile;
    }

    private static void copyProfileProperties(GameProfile source, GameProfile target) {
        PropertyMap sourceProperties = source.getProperties();
        if (sourceProperties.isEmpty()) {
            return;
        }
        PropertyMap properties = target.getProperties();
        properties.clear();

        for (Property property : sourceProperties.values()) {
            properties.put(property.getName(), property);
        }
    }

    private static ProfileProperty toBukkit(Property property) {
        return new ProfileProperty(property.getName(), property.getValue(), property.getSignature());
    }

    public static PlayerProfile asBukkitCopy(GameProfile gameProfile) {
        CraftPlayerProfile profile = new CraftPlayerProfile(gameProfile.getId(), gameProfile.getName());
        copyProfileProperties(gameProfile, profile.profile);
        return profile;
    }

    public static PlayerProfile asBukkitMirror(GameProfile profile) {
        return new CraftPlayerProfile(profile);
    }

    public static Property asAuthlib(ProfileProperty property) {
        return new Property(property.getName(), property.getValue(), property.getSignature());
    }

    public static GameProfile asAuthlibCopy(PlayerProfile profile) {
        CraftPlayerProfile craft = ((CraftPlayerProfile) profile);
        return asAuthlib(craft.clone());
    }

    public static GameProfile asAuthlib(PlayerProfile profile) {
        CraftPlayerProfile craft = ((CraftPlayerProfile) profile);
        return craft.getGameProfile();
    }

    @Override
    public boolean hasProperty(String property) {
        return profile.getProperties().containsKey(property);
    }

    @Override
    public void setProperty(ProfileProperty property) {
        String name = property.getName();
        PropertyMap properties = profile.getProperties();
        properties.removeAll(name);
        properties.put(name, new Property(name, property.getValue(), property.getSignature()));
    }

    public GameProfile getGameProfile() {
        return profile;
    }

    @Nullable
    @Override
    public UUID getId() {
        return profile.getId();
    }

    @Override
    public UUID setId(@Nullable UUID uuid) {
        GameProfile prev = this.profile;
        this.profile = new GameProfile(uuid, prev.getName());
        copyProfileProperties(prev, this.profile);
        return prev.getId();
    }

    @Nullable
    @Override
    public String getName() {
        return profile.getName();
    }

    @Override
    public String setName(@Nullable String name) {
        GameProfile prev = this.profile;
        this.profile = new GameProfile(prev.getId(), name);
        copyProfileProperties(prev, this.profile);
        return prev.getName();
    }

    @Nonnull
    @Override
    public Set<ProfileProperty> getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Collection<ProfileProperty> properties) {
        properties.forEach(this::setProperty);
    }

    @Override
    public void clearProperties() {
        profile.getProperties().clear();
    }

    @Override
    public boolean removeProperty(String property) {
        return !profile.getProperties().removeAll(property).isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CraftPlayerProfile that = (CraftPlayerProfile) o;
        return Objects.equals(profile, that.profile);
    }

    @Override
    public int hashCode() {
        return profile.hashCode();
    }

    @Override
    public String toString() {
        return profile.toString();
    }

    @Override
    public CraftPlayerProfile clone() {
        CraftPlayerProfile clone = new CraftPlayerProfile(this.getId(), this.getName());
        clone.setProperties(getProperties());
        return clone;
    }

    @Override
    public boolean isComplete() {
        return profile.isComplete();
    }

    @Override
    public boolean completeFromCache() {
        return completeFromCache(false);
    }

    public boolean completeFromCache(boolean lookupName) {
        if (profile.isComplete()) {
            return true;
        }
        MinecraftServer server = MinecraftServer.getServerInst();
        String name = profile.getName();
        PlayerProfileCache userCache = server.getPlayerProfileCache();
        if (profile.getId() == null) {
            final GameProfile profile;
            boolean isOnlineMode = server.isServerInOnlineMode() || (SpigotConfig.bungee);
            if (isOnlineMode) {
                profile = lookupName ? userCache.getGameProfileForUsername(name) : userCache.getProfileIfCached(name);
            } else {
                // Make an OfflinePlayer using an offline mode UUID since the name has no profile
                profile = new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8)), name);
            }
            if (profile != null) {
                this.profile = profile;
            }
        }

        if (profile.getName() == null) {
            // If we need textures, skip this check, as we will get it below anyways.
            GameProfile profile = userCache.getProfileByUUID(this.profile.getId());
            if (profile != null) {
                this.profile = profile;
            }
        }
        return this.profile.isComplete();
    }

    public boolean complete(boolean textures) {
        MinecraftServer server = MinecraftServer.getServerInst();

        boolean isOnlineMode = server.isServerInOnlineMode() || (SpigotConfig.bungee);
        boolean isCompleteFromCache = this.completeFromCache(true);
        if (isOnlineMode && (!isCompleteFromCache || textures && !hasTextures())) {
            GameProfile result = server.getMinecraftSessionService().fillProfileProperties(profile, true);
            if (result != null) {
                this.profile = result;
            }
        }
        return profile.isComplete() && (!isOnlineMode || !textures || hasTextures());
    }

    private class PropertySet extends AbstractSet<ProfileProperty> {

        @Override
        @Nonnull
        public Iterator<ProfileProperty> iterator() {
            return new ProfilePropertyIterator(profile.getProperties().values().iterator());
        }

        @Override
        public int size() {
            return profile.getProperties().size();
        }

        @Override
        public boolean add(ProfileProperty property) {
            setProperty(property);
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends ProfileProperty> c) {
            //noinspection unchecked
            setProperties((Collection<ProfileProperty>) c);
            return true;
        }

        @Override
        public boolean contains(Object o) {
            return o instanceof ProfileProperty && profile.getProperties().containsKey(((ProfileProperty) o).getName());
        }

        private class ProfilePropertyIterator implements Iterator<ProfileProperty> {
            private final Iterator<Property> iterator;

            ProfilePropertyIterator(Iterator<Property> iterator) {
                this.iterator = iterator;
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public ProfileProperty next() {
                return toBukkit(iterator.next());
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        }
    }
}
