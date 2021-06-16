package com.destroystokyo.paper.profile;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    @NotNull
    String setName(@Nullable String name);

    /**
     * @return The players unique identifier, if set
     */
    @Nullable UUID getId();

    /**
     * Sets this profiles UUID
     *
     * @param uuid The new UUID
     * @return The previous UUID
     */
    @Nullable
    UUID setId(@Nullable UUID uuid);

    /**
     * @return A Mutable set of this players properties, such as textures.
     * Values specified here are subject to implementation details.
     */
    @NotNull Set<ProfileProperty> getProperties();

    /**
     * Check if the Profile has the specified property
     *
     * @param property Property name to check
     * @return If the property is set
     */
    boolean hasProperty(@Nullable String property);

    /**
     * Sets a property. If the property already exists, the previous one will be replaced
     *
     * @param property Property to set.
     */
    void setProperty(@NotNull ProfileProperty property);

    /**
     * Sets multiple properties. If any of the set properties already exist, it will be replaced
     *
     * @param properties The properties to set
     */
    void setProperties(@NotNull Collection<ProfileProperty> properties);

    /**
     * Removes a specific property from this profile
     *
     * @param property The property to remove
     * @return If a property was removed
     */
    boolean removeProperty(@Nullable String property);

    /**
     * Removes a specific property from this profile
     *
     * @param property The property to remove
     * @return If a property was removed
     */
    default boolean removeProperty(@NotNull ProfileProperty property) {
        return removeProperty(property.getName());
    }

    /**
     * Removes all properties in the collection
     *
     * @param properties The properties to remove
     * @return If any property was removed
     */
    default boolean removeProperties(@NotNull Collection<ProfileProperty> properties) {
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
     * Like {@link #complete(boolean)} but will try only from cache, and not make network calls
     * Does not account for textures.
     *
     * @param onlineMode Treat this as online mode or not
     * @return If the profile is now complete (has UUID and Name)
     */
    boolean completeFromCache(boolean onlineMode);

    /**
     * Like {@link #complete(boolean)} but will try only from cache, and not make network calls
     * Does not account for textures.
     *
     * @param lookupUUID If only name is supplied, should we do a UUID lookup
     * @param onlineMode Treat this as online mode or not
     * @return If the profile is now complete (has UUID and Name)
     */
    boolean completeFromCache(boolean lookupUUID, boolean onlineMode);

    /**
     * If this profile is not complete, then make the API call to complete it.
     * This is a blocking operation and should be done asynchronously.
     * <p>
     * This will also complete textures. If you do not want to load textures, use {{@link #complete(boolean)}}
     *
     * @return If the profile is now complete (has UUID and Name) (if you get rate limited, this operation may fail)
     */
    default boolean complete() {
        return complete(true);
    }

    /**
     * If this profile is not complete, then make the API call to complete it.
     * This is a blocking operation and should be done asynchronously.
     * <p>
     * Optionally will also fill textures.
     * <p>
     * Online mode will be automatically determined
     *
     * @param textures controls if we should fill the profile with texture properties
     * @return If the profile is now complete (has UUID and Name) (if you get rate limited, this operation may fail)
     */
    boolean complete(boolean textures);

    /**
     * If this profile is not complete, then make the API call to complete it.
     * This is a blocking operation and should be done asynchronously.
     * <p>
     * Optionally will also fill textures.
     *
     * @param textures   controls if we should fill the profile with texture properties
     * @param onlineMode Treat this server as online mode or not
     * @return If the profile is now complete (has UUID and Name) (if you get rate limited, this operation may fail)
     */
    boolean complete(boolean textures, boolean onlineMode);

    /**
     * Whether or not this Profile has textures associated to it
     *
     * @return If has a textures property
     */
    default boolean hasTextures() {
        return hasProperty("textures");
    }
}
