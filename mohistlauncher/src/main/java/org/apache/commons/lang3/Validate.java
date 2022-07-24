//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.apache.commons.lang3;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class Validate {
    private static final String DEFAULT_NOT_NAN_EX_MESSAGE = "The validated value is not a number";
    private static final String DEFAULT_FINITE_EX_MESSAGE = "The value is invalid: %f";
    private static final String DEFAULT_EXCLUSIVE_BETWEEN_EX_MESSAGE = "The value %s is not in the specified exclusive range of %s to %s";
    private static final String DEFAULT_INCLUSIVE_BETWEEN_EX_MESSAGE = "The value %s is not in the specified inclusive range of %s to %s";
    private static final String DEFAULT_MATCHES_PATTERN_EX = "The string %s does not match the pattern %s";
    private static final String DEFAULT_IS_NULL_EX_MESSAGE = "The validated object is null";
    private static final String DEFAULT_IS_TRUE_EX_MESSAGE = "The validated expression is false";
    private static final String DEFAULT_NO_NULL_ELEMENTS_ARRAY_EX_MESSAGE = "The validated array contains null element at index: %d";
    private static final String DEFAULT_NO_NULL_ELEMENTS_COLLECTION_EX_MESSAGE = "The validated collection contains null element at index: %d";
    private static final String DEFAULT_NOT_BLANK_EX_MESSAGE = "The validated character sequence is blank";
    private static final String DEFAULT_NOT_EMPTY_ARRAY_EX_MESSAGE = "The validated array is empty";
    private static final String DEFAULT_NOT_EMPTY_CHAR_SEQUENCE_EX_MESSAGE = "The validated character sequence is empty";
    private static final String DEFAULT_NOT_EMPTY_COLLECTION_EX_MESSAGE = "The validated collection is empty";
    private static final String DEFAULT_NOT_EMPTY_MAP_EX_MESSAGE = "The validated map is empty";
    private static final String DEFAULT_VALID_INDEX_ARRAY_EX_MESSAGE = "The validated array index is invalid: %d";
    private static final String DEFAULT_VALID_INDEX_CHAR_SEQUENCE_EX_MESSAGE = "The validated character sequence index is invalid: %d";
    private static final String DEFAULT_VALID_INDEX_COLLECTION_EX_MESSAGE = "The validated collection index is invalid: %d";
    private static final String DEFAULT_VALID_STATE_EX_MESSAGE = "The validated state is false";
    private static final String DEFAULT_IS_ASSIGNABLE_EX_MESSAGE = "Cannot assign a %s to a %s";
    private static final String DEFAULT_IS_INSTANCE_OF_EX_MESSAGE = "Expected type: %s, actual: %s";

    public Validate() {
    }

    public static void isTrue(boolean expression, String message, long value) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, value));
        }
    }

    public static void isTrue(boolean expression, String message, double value) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, value));
        }
    }

    public static void isTrue(boolean expression, String message, Object... values) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    public static void isTrue(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException("The validated expression is false");
        }
    }

    public static <T> T notNull(T object) {
        return notNull(object, "The validated object is null");
    }

    public static <T> T notNull(Object object,  String message) {
        return notNull(object, message);
    }

    public static <T> T notNull(T object, String message, Object... values) {
        return Objects.requireNonNull(object, () -> {
            return String.format(message, values);
        });
    }

    public static <T> T[] notEmpty(T[] array, String message, Object... values) {
        Objects.requireNonNull(array, () -> {
            return String.format(message, values);
        });
        if (array.length == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        } else {
            return array;
        }
    }

    public static <T> T[] notEmpty(T[] array) {
        return notEmpty(array, "The validated array is empty");
    }

    public static <T extends Collection<?>> T notEmpty(T collection, String message, Object... values) {
        Objects.requireNonNull(collection, () -> {
            return String.format(message, values);
        });
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        } else {
            return collection;
        }
    }

    public static <T extends Collection<?>> T notEmpty(T collection) {
        return notEmpty(collection, "The validated collection is empty");
    }

    public static <T extends Map<?, ?>> T notEmpty(T map, String message, Object... values) {
        Objects.requireNonNull(map, () -> {
            return String.format(message, values);
        });
        if (map.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        } else {
            return map;
        }
    }

    public static <T extends Map<?, ?>> T notEmpty(T map) {
        return notEmpty(map, "The validated map is empty");
    }

    public static <T extends CharSequence> T notEmpty(T chars, String message, Object... values) {
        Objects.requireNonNull(chars, () -> {
            return String.format(message, values);
        });
        if (chars.length() == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        } else {
            return chars;
        }
    }

    public static <T extends CharSequence> T notEmpty(T chars) {
        return notEmpty(chars, "The validated character sequence is empty");
    }

    public static <T extends CharSequence> T notBlank(T chars, String message, Object... values) {
        Objects.requireNonNull(chars, () -> {
            return String.format(message, values);
        });
        if (StringUtils.isBlank(chars)) {
            throw new IllegalArgumentException(String.format(message, values));
        } else {
            return chars;
        }
    }

    public static <T extends CharSequence> T notBlank(T chars) {
        return notBlank(chars, "The validated character sequence is blank");
    }

    public static <T> T[] noNullElements(T[] array, String message, Object... values) {
        notNull(array);

        for(int i = 0; i < array.length; ++i) {
            if (array[i] == null) {
                Object[] values2 = ArrayUtils.add(values, i);
                throw new IllegalArgumentException(String.format(message, values2));
            }
        }

        return array;
    }

    public static <T> T[] noNullElements(T[] array) {
        return noNullElements(array, "The validated array contains null element at index: %d");
    }

    public static <T extends Iterable<?>> T noNullElements(T iterable, String message, Object... values) {
        notNull(iterable);
        int i = 0;

        for(Iterator<?> it = iterable.iterator(); it.hasNext(); ++i) {
            if (it.next() == null) {
                Object[] values2 = ArrayUtils.addAll(values, new Object[]{i});
                throw new IllegalArgumentException(String.format(message, values2));
            }
        }

        return iterable;
    }

    public static <T extends Iterable<?>> T noNullElements(T iterable) {
        return noNullElements(iterable, "The validated collection contains null element at index: %d");
    }

    public static <T> T[] validIndex(T[] array, int index, String message, Object... values) {
        notNull(array);
        if (index >= 0 && index < array.length) {
            return array;
        } else {
            throw new IndexOutOfBoundsException(String.format(message, values));
        }
    }

    public static <T> T[] validIndex(T[] array, int index) {
        return validIndex(array, index, "The validated array index is invalid: %d", index);
    }

    public static <T extends Collection<?>> T validIndex(T collection, int index, String message, Object... values) {
        notNull(collection);
        if (index >= 0 && index < collection.size()) {
            return collection;
        } else {
            throw new IndexOutOfBoundsException(String.format(message, values));
        }
    }

    public static <T extends Collection<?>> T validIndex(T collection, int index) {
        return validIndex(collection, index, "The validated collection index is invalid: %d", index);
    }

    public static <T extends CharSequence> T validIndex(T chars, int index, String message, Object... values) {
        notNull(chars);
        if (index >= 0 && index < chars.length()) {
            return chars;
        } else {
            throw new IndexOutOfBoundsException(String.format(message, values));
        }
    }

    public static <T extends CharSequence> T validIndex(T chars, int index) {
        return validIndex(chars, index, "The validated character sequence index is invalid: %d", index);
    }

    public static void validState(boolean expression) {
        if (!expression) {
            throw new IllegalStateException("The validated state is false");
        }
    }

    public static void validState(boolean expression, String message, Object... values) {
        if (!expression) {
            throw new IllegalStateException(String.format(message, values));
        }
    }

    public static void matchesPattern(CharSequence input, String pattern) {
        if (!Pattern.matches(pattern, input)) {
            throw new IllegalArgumentException(String.format("The string %s does not match the pattern %s", input, pattern));
        }
    }

    public static void matchesPattern(CharSequence input, String pattern, String message, Object... values) {
        if (!Pattern.matches(pattern, input)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    public static void notNaN(double value) {
        notNaN(value, "The validated value is not a number");
    }

    public static void notNaN(double value, String message, Object... values) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    public static void finite(double value) {
        finite(value, "The value is invalid: %f", value);
    }

    public static void finite(double value, String message, Object... values) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    public static <T> void inclusiveBetween(T start, T end, Comparable<T> value) {
        if (value.compareTo(start) < 0 || value.compareTo(end) > 0) {
            throw new IllegalArgumentException(String.format("The value %s is not in the specified inclusive range of %s to %s", value, start, end));
        }
    }

    public static <T> void inclusiveBetween(T start, T end, Comparable<T> value, String message, Object... values) {
        if (value.compareTo(start) < 0 || value.compareTo(end) > 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    public static void inclusiveBetween(long start, long end, long value) {
        if (value < start || value > end) {
            throw new IllegalArgumentException(String.format("The value %s is not in the specified inclusive range of %s to %s", value, start, end));
        }
    }

    public static void inclusiveBetween(long start, long end, long value, String message) {
        if (value < start || value > end) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void inclusiveBetween(double start, double end, double value) {
        if (value < start || value > end) {
            throw new IllegalArgumentException(String.format("The value %s is not in the specified inclusive range of %s to %s", value, start, end));
        }
    }

    public static void inclusiveBetween(double start, double end, double value, String message) {
        if (value < start || value > end) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T> void exclusiveBetween(T start, T end, Comparable<T> value) {
        if (value.compareTo(start) <= 0 || value.compareTo(end) >= 0) {
            throw new IllegalArgumentException(String.format("The value %s is not in the specified exclusive range of %s to %s", value, start, end));
        }
    }

    public static <T> void exclusiveBetween(T start, T end, Comparable<T> value, String message, Object... values) {
        if (value.compareTo(start) <= 0 || value.compareTo(end) >= 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    public static void exclusiveBetween(long start, long end, long value) {
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(String.format("The value %s is not in the specified exclusive range of %s to %s", value, start, end));
        }
    }

    public static void exclusiveBetween(long start, long end, long value, String message) {
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void exclusiveBetween(double start, double end, double value) {
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(String.format("The value %s is not in the specified exclusive range of %s to %s", value, start, end));
        }
    }

    public static void exclusiveBetween(double start, double end, double value, String message) {
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isInstanceOf(Class<?> type, Object obj) {
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(String.format("Expected type: %s, actual: %s", type.getName(), obj == null ? "null" : obj.getClass().getName()));
        }
    }

    public static void isInstanceOf(Class<?> type, Object obj, String message, Object... values) {
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    public static void isAssignableFrom(Class<?> superType, Class<?> type) {
        if (!superType.isAssignableFrom(type)) {
            throw new IllegalArgumentException(String.format("Cannot assign a %s to a %s", type == null ? "null" : type.getName(), superType.getName()));
        }
    }

    public static void isAssignableFrom(Class<?> superType, Class<?> type, String message, Object... values) {
        if (!superType.isAssignableFrom(type)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }
}
