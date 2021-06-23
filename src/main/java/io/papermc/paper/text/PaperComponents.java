package io.papermc.paper.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Paper API-specific methods for working with {@link Component}s and related.
 */
public final class PaperComponents {
    private PaperComponents() {
        throw new RuntimeException("PaperComponents is not to be instantiated!");
    }

    /**
     * Return a component flattener that can use game data to resolve extra information about components.
     *
     * @return a component flattener
     */
    public static @NonNull ComponentFlattener flattener() {
        return Bukkit.getUnsafe().componentFlattener();
    }

    /**
     * Get a serializer for {@link Component}s that will convert components to
     * a plain-text string.
     *
     * <p>Implementations may provide a serializer capable of processing any
     * information that requires access to implementation details.</p>
     *
     * @return a serializer to plain text
     */
    public static @NonNull PlainComponentSerializer plainSerializer() {
        return Bukkit.getUnsafe().plainComponentSerializer();
    }

    /**
     * Get a serializer for {@link Component}s that will convert to and from the
     * standard JSON serialization format using Gson.
     *
     * <p>Implementations may provide a serializer capable of processing any
     * information that requires implementation details, such as legacy
     * (pre-1.16) hover events.</p>
     *
     * @return a json component serializer
     */
    public static @NonNull GsonComponentSerializer gsonSerializer() {
        return Bukkit.getUnsafe().gsonComponentSerializer();
    }

    /**
     * Get a serializer for {@link Component}s that will convert to and from the
     * standard JSON serialization format using Gson, downsampling any RGB colors
     * to their nearest {@link NamedTextColor} counterpart.
     *
     * <p>Implementations may provide a serializer capable of processing any
     * information that requires implementation details, such as legacy
     * (pre-1.16) hover events.</p>
     *
     * @return a json component serializer
     */
    public static @NonNull GsonComponentSerializer colorDownsamplingGsonSerializer() {
        return Bukkit.getUnsafe().colorDownsamplingGsonComponentSerializer();
    }

    /**
     * Get a serializer for {@link Component}s that will convert to and from the
     * legacy component format used by Bukkit. This serializer uses the
     * {@link LegacyComponentSerializer.Builder#useUnusualXRepeatedCharacterHexFormat()}
     * option to match upstream behavior.
     *
     * <p>This legacy serializer uses the standard section symbol to mark
     * formatting characters.</p>
     *
     * <p>Implementations may provide a serializer capable of processing any
     * information that requires access to implementation details.</p>
     *
     * @return a section serializer
     */
    public static @NonNull LegacyComponentSerializer legacySectionSerializer() {
        return Bukkit.getUnsafe().legacyComponentSerializer();
    }
}
