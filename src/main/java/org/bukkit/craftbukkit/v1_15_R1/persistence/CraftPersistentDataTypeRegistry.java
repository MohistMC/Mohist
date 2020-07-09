package org.bukkit.craftbukkit.v1_15_R1.persistence;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import com.google.common.primitives.Primitives;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.nbt.LongArrayNBT;
import net.minecraft.nbt.ShortNBT;
import net.minecraft.nbt.StringNBT;
import org.apache.commons.lang3.Validate;
import org.bukkit.persistence.PersistentDataContainer;

/**
 * This class represents a registry that contains the used adapters for.
 */
public final class CraftPersistentDataTypeRegistry {

    private final Function<Class, TagAdapter> CREATE_ADAPTER = this::createAdapter;

    private class TagAdapter<T, Z extends INBT> {

        private final Function<T, Z> builder;
        private final Function<Z, T> extractor;

        private final Class<T> primitiveType;
        private final Class<Z> nbtBaseType;

        public TagAdapter(Class<T> primitiveType, Class<Z> nbtBaseType, Function<T, Z> builder, Function<Z, T> extractor) {
            this.primitiveType = primitiveType;
            this.nbtBaseType = nbtBaseType;
            this.builder = builder;
            this.extractor = extractor;
        }

        /**
         * This method will extract the value stored in the tag, according to
         * the expected primitive type.
         *
         * @param base the base to extract from
         *
         * @return the value stored inside of the tag
         *
         * @throws ClassCastException if the passed base is not an instanced of
         * the defined base type and therefore is not applicable to the
         * extractor function
         */
        T extract(INBT base) {
            Validate.isInstanceOf(nbtBaseType, base, "The provided INBT was of the type %s. Expected type %s", base.getClass().getSimpleName(), nbtBaseType.getSimpleName());
            return this.extractor.apply(nbtBaseType.cast(base));
        }

        /**
         * Builds a tag instance wrapping around the provided value object.
         *
         * @param value the value to store inside the created tag
         *
         * @return the new tag instance
         *
         * @throws ClassCastException if the passed value object is not of the
         * defined primitive type and therefore is not applicable to the builder
         * function
         */
        Z build(Object value) {
            Validate.isInstanceOf(primitiveType, value, "The provided value was of the type %s. Expected type %s", value.getClass().getSimpleName(), primitiveType.getSimpleName());
            return this.builder.apply(primitiveType.cast(value));
        }

        /**
         * Returns if the tag instance matches the adapters one.
         *
         * @param base the base to check
         *
         * @return if the tag was an instance of the set type
         */
        boolean isInstance(INBT base) {
            return this.nbtBaseType.isInstance(base);
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
        if (!Primitives.isWrapperType(type)) {
            type = Primitives.wrap(type); //Make sure we will always "switch" over the wrapper types
        }

        /*
            Primitives
         */
        if (Objects.equals(Byte.class, type)) {
            return createAdapter(Byte.class, ByteNBT.class, ByteNBT::valueOf, ByteNBT::getByte);
        }
        if (Objects.equals(Short.class, type)) {
            return createAdapter(Short.class, ShortNBT.class, ShortNBT::valueOf, ShortNBT::getShort);
        }
        if (Objects.equals(Integer.class, type)) {
            return createAdapter(Integer.class, IntNBT.class, IntNBT::valueOf, IntNBT::getInt);
        }
        if (Objects.equals(Long.class, type)) {
            return createAdapter(Long.class, LongNBT.class, LongNBT::valueOf, LongNBT::getLong);
        }
        if (Objects.equals(Float.class, type)) {
            return createAdapter(Float.class, FloatNBT.class, FloatNBT::valueOf, FloatNBT::getFloat);
        }
        if (Objects.equals(Double.class, type)) {
            return createAdapter(Double.class, DoubleNBT.class, DoubleNBT::valueOf, DoubleNBT::getDouble);
        }

        /*
            String
         */
        if (Objects.equals(String.class, type)) {
            return createAdapter(String.class, StringNBT.class, StringNBT::valueOf, StringNBT::getString);
        }

        /*
            Primitive Arrays
         */
        if (Objects.equals(byte[].class, type)) {
            return createAdapter(byte[].class, ByteArrayNBT.class, array -> new ByteArrayNBT(Arrays.copyOf(array, array.length)), n -> Arrays.copyOf(n.getByteArray(), n.size()));
        }
        if (Objects.equals(int[].class, type)) {
            return createAdapter(int[].class, IntArrayNBT.class, array -> new IntArrayNBT(Arrays.copyOf(array, array.length)), n -> Arrays.copyOf(n.getIntArray(), n.size()));
        }
        if (Objects.equals(long[].class, type)) {
            return createAdapter(long[].class, LongArrayNBT.class, array -> new LongArrayNBT(Arrays.copyOf(array, array.length)), n -> Arrays.copyOf(n.getAsLongArray(), n.size()));
        }

        /*
            Note that this will map the interface PersistentMetadataContainer directly to the CraftBukkit implementation
            Passing any other instance of this form to the tag type registry will throw a ClassCastException as defined in TagAdapter#build
         */
        if (Objects.equals(PersistentDataContainer.class, type)) {
            return createAdapter(CraftPersistentDataContainer.class, CompoundNBT.class, CraftPersistentDataContainer::toTagCompound, tag -> {
                CraftPersistentDataContainer container = new CraftPersistentDataContainer(this);
                for (String key : tag.keySet()) {
                    container.put(key, tag.get(key));
                }
                return container;
            });
        }

        throw new IllegalArgumentException("Could not find a valid TagAdapter implementation for the requested type " + type.getSimpleName());
    }

    private <T, Z extends INBT> TagAdapter<T, Z> createAdapter(Class<T> primitiveType, Class<Z> nbtBaseType, Function<T, Z> builder, Function<Z, T> extractor) {
        return new TagAdapter<>(primitiveType, nbtBaseType, builder, extractor);
    }

    /**
     * Wraps the passed value into a tag instance.
     *
     * @param type the type of the passed value
     * @param value the value to be stored in the tag
     * @param <T> the generic type of the value
     *
     * @return the created tag instance
     *
     * @throws IllegalArgumentException if no suitable tag type adapter for this
     * type was found
     */
    public <T> INBT wrap(Class<T> type, T value) {
        return this.adapters.computeIfAbsent(type, CREATE_ADAPTER).build(value);
    }

    /**
     * Returns if the tag instance matches the provided primitive type.
     *
     * @param type the type of the primitive value
     * @param base the base instance to check
     * @param <T> the generic type of the type
     *
     * @return if the base stores values of the primitive type passed
     *
     * @throws IllegalArgumentException if no suitable tag type adapter for this
     * type was found
     */
    public <T> boolean isInstanceOf(Class<T> type, INBT base) {
        return this.adapters.computeIfAbsent(type, CREATE_ADAPTER).isInstance(base);
    }

    /**
     * Extracts the value out of the provided tag.
     *
     * @param type the type of the value to extract
     * @param tag the tag to extract the value from
     * @param <T> the generic type of the value stored inside the tag
     *
     * @return the extracted value
     *
     * @throws IllegalArgumentException if the passed base is not an instanced
     * of the defined base type and therefore is not applicable to the extractor
     * function
     * @throws IllegalArgumentException if the found object is not of type
     * passed
     * @throws IllegalArgumentException if no suitable tag type adapter for this
     * type was found
     */
    public <T> T extract(Class<T> type, INBT tag) throws ClassCastException, IllegalArgumentException {
        TagAdapter adapter = this.adapters.computeIfAbsent(type, CREATE_ADAPTER);
        Validate.isTrue(adapter.isInstance(tag), "`The found tag instance cannot store %s as it is a %s", type.getSimpleName(), tag.getClass().getSimpleName());

        Object foundValue = adapter.extract(tag);
        Validate.isInstanceOf(type, foundValue, "The found object is of the type %s. Expected type %s", foundValue.getClass().getSimpleName(), type.getSimpleName());
        return type.cast(foundValue);
    }
}
