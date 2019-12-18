package com.destroystokyo.paper.profile;

import com.mojang.authlib.GameProfile;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a players profile for the game, such as UUID, Name, and textures.
 */
public interface PlayerProfile {

    /**
     * @return The players name, if set
     */
    @Nullable
    String getName();

    /**
     * Sets this profiles Name
     *
     * @param name The new Name
     * @return The previous Name
     */
    String setName(@Nullable String name);

    /**
     * @return The players unique identifier, if set
     */
    @Nullable
    UUID getId();

    /**
     * Sets this profiles UUID
     *
     * @param uuid The new UUID
     * @return The previous UUID
     */
    UUID setId(@Nullable UUID uuid);

    /**
     * @return A Mutable set of this players properties, such as textures.
     * Values specified here are subject to implementation details.
     */
    @Nonnull
    Set<ProfileProperty> getProperties();

    /**
     * Sets multiple properties. If any of the set properties already exist, it will be replaced
     * @param properties The properties to set
     */
    void setProperties(Collection<ProfileProperty> properties);

    /**
     * Check if the Profile has the specified property
     * @param property Property name to check
     * @return If the property is set
     */
    boolean hasProperty(String property);

    /**
     * Sets a property. If the property already exists, the previous one will be replaced
     * @param property Property to set.
     */
    void setProperty(ProfileProperty property);

    /**
     * Removes a specific property from this profile
     * @param property The property to remove
     * @return If a property was removed
     */
    boolean removeProperty(String property);

    /**
     * Removes a specific property from this profile
     * @param property The property to remove
     * @return If a property was removed
     */
    default boolean removeProperty(@Nonnull ProfileProperty property) {
        return removeProperty(property.getName());
    }

    /**
     * Removes all properties in the collection
     * @param properties The properties to remove
     * @return If any property was removed
     */
    default boolean removeProperties(Collection<ProfileProperty> properties) {
        boolean removed = false;
        for (ProfileProperty property : properties) {
            if (removeProperty(property)) {
                removed = true;
            }
        }
        return removed;
    }

    /**
     * Clears all properties on this profile
     */
    void clearProperties();

    /**
     * @return If the profile is now complete (has UUID and Name)
     */
    boolean isComplete();

    /**
     * Like {@link #complete(boolean)} but will try only from cache, and not make network calls
     * Does not account for textures.
     *
     * @return If the profile is now complete (has UUID and Name)
     */
    boolean completeFromCache();

    /**
     * If this profile is not complete, then make the API call to complete it.
     * This is a blocking operation and should be done asynchronously.
     *
     * This will also complete textures. If you do not want to load textures, use {{@link #complete(boolean)}}
     * @return If the profile is now complete (has UUID and Name) (if you get rate limited, this operation may fail)
     */
    default boolean complete() {
        return complete(true);
    }

    /**
     * If this profile is not complete, then make the API call to complete it.
     * This is a blocking operation and should be done asynchronously.
     *
     * Optionally will also fill textures.
     * @param textures controls if we should fill the profile with texture properties
     * @return If the profile is now complete (has UUID and Name) (if you get rate limited, this operation may fail)
     */
    boolean complete(boolean textures);

    /**
     * Whether or not this Profile has textures associated to it
     * @return If has a textures property
     */
    default boolean hasTextures() {
        return hasProperty("textures");
    }

    /**
     * @deprecated Will be removed in 1.13
     * @return the GameProfile for this PlayerProfile
     */
    @Deprecated
    GameProfile getGameProfile();
}
