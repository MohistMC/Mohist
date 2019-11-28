/*
  Based on CompactHashSet Copyright 2011 Ontopia Project

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package org.bukkit.craftbukkit.util;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class LongHashSet implements Set<Long> {
    private final static int INITIAL_SIZE = 3;
    private final static double LOAD_FACTOR = 0.75;

    private final static long FREE = 0;
    private final static long REMOVED = Long.MIN_VALUE;

    private int freeEntries;
    private int elements;
    private long[] values;
    private int modCount;
    private org.spigotmc.FlatMap<Boolean> flat = new org.spigotmc.FlatMap<Boolean>(); // Spigot

    public LongHashSet() {
        this(INITIAL_SIZE);
    }

    public LongHashSet(int size) {
        values = new long[(size==0 ? 1 : size)];
        elements = 0;
        freeEntries = values.length;
        modCount = 0;
    }

    @Override
    public Iterator<Long> iterator() {
        return new Itr();
    }

    @Override
    public int size() {
        return elements;
    }

    @Override
    public boolean isEmpty() {
        return elements == 0;
    }

    public boolean contains(int msw, int lsw) {
        // Spigot start
        if ( elements == 0 )
        {
            return false;
        }
        if ( flat.contains( msw, lsw ) )
        {
            return true;
        }
        // Spigot end
        return contains(LongHash.toLong(msw, lsw));
    }

    public boolean contains(long value) {
        // Spigot start
        if ( elements == 0 )
        {
            return false;
        }
        if ( flat.contains( value ) )
        {
            return true;
        }
        // Spigot end
        int hash = hash(value);
        int index = (hash & 0x7FFFFFFF) % values.length;
        int offset = 1;

        // search for the object (continue while !null and !this object)
        while(values[index] != FREE && !(hash(values[index]) == hash && values[index] == value)) {
            index = ((index + offset) & 0x7FFFFFFF) % values.length;
            offset = offset * 2 + 1;

            if (offset == -1) {
                offset = 2;
            }
        }

        return values[index] != FREE;
    }

    public boolean add(int msw, int lsw) {
        return add(LongHash.toLong(msw, lsw));
    }

    public boolean add(long value) {
        flat.put( value, Boolean.TRUE ); // Spigot
        int hash = hash(value);
        int index = (hash & 0x7FFFFFFF) % values.length;
        int offset = 1;
        int deletedix = -1;

        // search for the object (continue while !null and !this object)
        while(values[index] != FREE && !(hash(values[index]) == hash && values[index] == value)) {
            // if there's a deleted object here we can put this object here,
            // provided it's not in here somewhere else already
            if (values[index] == REMOVED) {
                deletedix = index;
            }

            index = ((index + offset) & 0x7FFFFFFF) % values.length;
            offset = offset * 2 + 1;

            if (offset == -1) {
                offset = 2;
            }
        }

        if (values[index] == FREE) {
            if (deletedix != -1) { // reusing a deleted cell
                index = deletedix;
            } else {
                freeEntries--;
            }

            modCount++;
            elements++;
            values[index] = value;

            if (1 - (freeEntries / (double) values.length) > LOAD_FACTOR) {
                rehash();
            }

            return true;
        } else {
            return false;
        }
    }

    public void remove(int msw, int lsw) {
        // Spigot start
        flat.remove(msw, lsw);
        remove0(LongHash.toLong(msw, lsw));
    }

    public boolean remove(long value) {
        flat.remove(value);
        return remove0(value);
    }

    private boolean remove0(long value) {
        // Spigot end
        int hash = hash(value);
        int index = (hash & 0x7FFFFFFF) % values.length;
        int offset = 1;

        // search for the object (continue while !null and !this object)
        while(values[index] != FREE && !(hash(values[index]) == hash && values[index] == value)) {
            index = ((index + offset) & 0x7FFFFFFF) % values.length;
            offset = offset * 2 + 1;

            if (offset == -1) {
                offset = 2;
            }
        }

        if (values[index] != FREE) {
            values[index] = REMOVED;
            modCount++;
            elements--;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void clear() {
        elements = 0;
        for (int ix = 0; ix < values.length; ix++) {
            values[ix] = FREE;
        }

        freeEntries = values.length;
        modCount++;
        flat = new org.spigotmc.FlatMap<Boolean>();
    }

    public long[] toPrimitiveArray() {
        long[] result = new long[elements];
        long[] values = Java15Compat.Arrays_copyOf(this.values, this.values.length);
        int pos = 0;

        for (long value : values) {
            if (value != FREE && value != REMOVED) {
                result[pos++] = value;
            }
        }

        return result;
    }

    @Override
    public Long[] toArray() {
        Long[] result = new Long[elements];
        long[] values = Java15Compat.Arrays_copyOf(this.values, this.values.length);
        int pos = 0;

        for (long value : values) {
            if (value != FREE && value != REMOVED) {
                result[pos++] = value;
            }
        }

        return result;
    }
    
    @Override
    public <T> T[] toArray(T[] arg0) {
        throw new UnsupportedOperationException();
    }

    public long popFirst() {
        for (long value : values) {
            if (value != FREE && value != REMOVED) {
                remove(value);
                return value;
            }
        }

        return 0;
    }

    public long[] popAll() {
        long[] ret = toPrimitiveArray();
        clear();
        return ret;
    }

    // This method copied from Murmur3, written by Austin Appleby released under Public Domain
    private int hash(long value) {
        value ^= value >>> 33;
        value *= 0xff51afd7ed558ccdL;
        value ^= value >>> 33;
        value *= 0xc4ceb9fe1a85ec53L;
        value ^= value >>> 33;
        return (int) value;
    }

    private void rehash() {
        int gargagecells = values.length - (elements + freeEntries);
        if (gargagecells / (double) values.length > 0.05) {
            rehash(values.length);
        } else {
            rehash(values.length * 2 + 1);
        }
    }

    private void rehash(int newCapacity) {
        long[] newValues = new long[newCapacity];

        for (long value : values) {
            if (value == FREE || value == REMOVED) {
                continue;
            }

            int hash = hash(value);
            int index = (hash & 0x7FFFFFFF) % newCapacity;
            int offset = 1;

            // search for the object
            while (newValues[index] != FREE) {
                index = ((index + offset) & 0x7FFFFFFF) % newCapacity;
                offset = offset * 2 + 1;

                if (offset == -1) {
                    offset = 2;
                }
            }

            newValues[index] = value;
        }

        values = newValues;
        freeEntries = values.length - elements;
    }

    private class Itr implements Iterator<Long> {
        private int index;
        private int lastReturned = -1;
        private int expectedModCount;

        public Itr() {
            for (index = 0; index < values.length && (values[index] == FREE || values[index] == REMOVED); index++) {
                // This is just to drive the index forward to the first valid entry
            }
            expectedModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            return index != values.length;
        }

        @Override
        public Long next() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            int length = values.length;
            if (index >= length) {
                lastReturned = -2;
                throw new NoSuchElementException();
            }

            lastReturned = index;
            for (index += 1; index < length && (values[index] == FREE || values[index] == REMOVED); index++) {
                // This is just to drive the index forward to the next valid entry
            }

            if (values[lastReturned] == FREE) {
                return FREE;
            } else {
                return values[lastReturned];
            }
        }

        @Override
        public void remove() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            if (lastReturned == -1 || lastReturned == -2) {
                throw new IllegalStateException();
            }

            if (values[lastReturned] != FREE && values[lastReturned] != REMOVED) {
                values[lastReturned] = REMOVED;
                elements--;
                modCount++;
                expectedModCount = modCount;
            }
        }
    }

    @Override
    public boolean add(Long value) {
        return add(value.longValue());
    }

    @Override
    public boolean addAll(Collection<? extends Long> collection) {
        boolean result = false;
        for (Long value : collection) result |= add(value.longValue());
        return result;
    }

    @Override
    public boolean contains(Object o) {
        return o instanceof Long ? contains(((Long) o).longValue()) : false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object value : collection) if (!contains(value)) return false;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return o instanceof Long ? remove(((Long) o).longValue()) : false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean result = false;
        for (Object value : collection) result |= remove(value);
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean result = false;
        Iterator<Long> iterator = iterator();
        while(iterator.hasNext()) {
            Long l = iterator.next();
            if (!collection.contains(l)) {
                iterator.remove();
                result = true;
            }
        }
        return result;
    }
}
