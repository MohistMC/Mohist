package com.destroystokyo.paper.profile;

import com.google.common.base.Preconditions;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a property on a {@link PlayerProfile}
 */
public class ProfileProperty {
    private final String name;
    private final String value;
    private final String signature;

    public ProfileProperty(@NotNull String name, @NotNull String value) {
        this(name, value, null);
    }

    public ProfileProperty(@NotNull String name, @NotNull String value, @Nullable String signature) {
        this.name = Preconditions.checkNotNull(name, "ProfileProperty name can not be null");
        this.value = Preconditions.checkNotNull(value, "ProfileProperty value can not be null");
        this.signature = signature;
    }

    /**
     * @return The property name, ie "textures"
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * @return The property value, likely to be base64 encoded
     */
    @NotNull
    public String getValue() {
        return value;
    }

    /**
     * @return A signature from Mojang for signed properties
     */
    @Nullable
    public String getSignature() {
        return signature;
    }

    /**
     * @return If this property has a signature or not
     */
    public boolean isSigned() {
        return this.signature != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileProperty that = (ProfileProperty) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(value, that.value) &&
                Objects.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
