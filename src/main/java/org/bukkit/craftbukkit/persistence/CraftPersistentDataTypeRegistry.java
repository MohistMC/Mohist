package org.bukkit.craftbukkit.persistence;

import com.google.common.primitives.Primitives;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import org.apache.commons.lang3.Validate;
import org.bukkit.persistence.PersistentDataContainer;

/**
 * This class represents a registry that contains the used adapters for.
 */
public final class CraftPersistentDataTypeRegistry {

    private final Function<Class, TagAdapter> CREATE_ADAPTER = this::createAdapter;

    private class TagAdapter<T, Z extends Tag> {

        private final Function<T, Z> builder;
        private final Function<Z, T> extractor;

        private final Class<T> primitiveType;
        private final Class<Z> TagType;

        public TagAdapter(Class<T> primitiveType, Class<Z> TagType, Function<T, Z> builder, Function<Z, T> extractor) {
            this.primitiveType = primitiveType;
            this.TagType = TagType;
            this.builder = builder;
            this.extractor = extractor;
        }

        T extract(Tag base) {
            Validate.isInstanceOf(TagType, base, "The provided Tag was of the type %s. Expected type %s", base.getClass().getSimpleName(), TagType.getSimpleName());
            return this.extractor.apply(TagType.cast(base));
        }

        Z build(Object value) {
            Validate.isInstanceOf(primitiveType, value, "The provided value was of the type %s. Expected type %s", value.getClass().getSimpleName(), primitiveType.getSimpleName());
            return this.builder.apply(primitiveType.cast(value));
        }

        boolean isInstance(Tag base) {
            return this.TagType.isInstance(base);
        }
    }

    private final Map<Class, TagAdapter> adapters = new HashMap<>();

    /**
     * Creates a suitable adapter instance for the primitive class type
     *
     * @param type the type to create an adapter for
     * @param <T> the generic type of that class
     *
     * @return the created adapter instance
     *
     * @throws IllegalArgumentException if no suitable tag type adapter for this
     * type was found
     */
    private <T> TagAdapter createAdapter(Class<T> type) {
        if (!Primitives.isWrapperType(type))
            type = Primitives.wrap(type); // Make sure we will always "switch" over the wrapper types

        // Primitives
        if (Objects.equals(Byte.class, type))
            return createAdapter(Byte.class, ByteTag.class, ByteTag::of, ByteTag::getByte);

        if (Objects.equals(Short.class, type))
            return createAdapter(Short.class, ShortTag.class, ShortTag::of, ShortTag::getShort);

        if (Objects.equals(Integer.class, type))
            return createAdapter(Integer.class, IntTag.class, IntTag::of, IntTag::getInt);

        if (Objects.equals(Long.class, type))
            return createAdapter(Long.class, LongTag.class, LongTag::of, LongTag::getLong);

        if (Objects.equals(Float.class, type))
            return createAdapter(Float.class, FloatTag.class, FloatTag::of, FloatTag::getFloat);

        if (Objects.equals(Double.class, type))
            return createAdapter(Double.class, DoubleTag.class, DoubleTag::of, DoubleTag::getDouble);

        // String
        if (Objects.equals(String.class, type))
            return createAdapter(String.class, StringTag.class, StringTag::of, StringTag::asString);

        // Primitive Arrays
        if (Objects.equals(byte[].class, type))
            return createAdapter(byte[].class, ByteArrayTag.class, array -> new ByteArrayTag(Arrays.copyOf(array, array.length)), n -> Arrays.copyOf(n.getByteArray(), n.size()));

        if (Objects.equals(int[].class, type))
            return createAdapter(int[].class, IntArrayTag.class, array -> new IntArrayTag(Arrays.copyOf(array, array.length)), n -> Arrays.copyOf(n.getIntArray(), n.size()));

        if (Objects.equals(long[].class, type))
            return createAdapter(long[].class, LongArrayTag.class, array -> new LongArrayTag(Arrays.copyOf(array, array.length)), n -> Arrays.copyOf(n.getLongArray(), n.size()));

        // Note that this will map the interface PersistentMetadataContainer directly to the CraftBukkit implementation
        // Passing any other instance of this form to the tag type registry will throw a ClassCastException as defined in TagAdapter#build
        if (Objects.equals(PersistentDataContainer.class, type)) {
            return createAdapter(CraftPersistentDataContainer.class, CompoundTag.class, CraftPersistentDataContainer::toTagCompound, tag -> {
                CraftPersistentDataContainer container = new CraftPersistentDataContainer(this);
                for (String key : tag.getKeys())
                    container.put(key, tag.get(key));
                return container;
            });
        }

        throw new IllegalArgumentException("Could not find a valid TagAdapter implementation for the requested type " + type.getSimpleName());
    }

    private <T, Z extends Tag> TagAdapter<T, Z> createAdapter(Class<T> primitiveType, Class<Z> TagType, Function<T, Z> builder, Function<Z, T> extractor) {
        return new TagAdapter<>(primitiveType, TagType, builder, extractor);
    }

    public <T> Tag wrap(Class<T> type, T value) {
        return this.adapters.computeIfAbsent(type, CREATE_ADAPTER).build(value);
    }

    public <T> boolean isInstanceOf(Class<T> type, Tag base) {
        return this.adapters.computeIfAbsent(type, CREATE_ADAPTER).isInstance(base);
    }

    public <T> T extract(Class<T> type, Tag tag) throws ClassCastException, IllegalArgumentException {
        TagAdapter adapter = this.adapters.computeIfAbsent(type, CREATE_ADAPTER);
        Validate.isTrue(adapter.isInstance(tag), "`The found tag instance cannot store %s as it is a %s", type.getSimpleName(), tag.getClass().getSimpleName());

        Object foundValue = adapter.extract(tag);
        Validate.isInstanceOf(type, foundValue, "The found object is of the type %s. Expected type %s", foundValue.getClass().getSimpleName(), type.getSimpleName());
        return type.cast(foundValue);
    }

}