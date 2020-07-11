package org.bukkit.craftbukkit.v1_12_R1.util;

import java.util.*;

public class HashTreeSet<V> implements Set<V> {

    private final HashSet<V> hash = new HashSet<>();
    private final TreeSet<V> tree = new TreeSet<>();

    public HashTreeSet() {

    }

    @Override
    public int size() {
        return hash.size();
    }

    @Override
    public boolean isEmpty() {
        return hash.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return hash.contains(o);
    }

    @Override
    public Iterator<V> iterator() {
        return new Iterator<V>() {

            private final Iterator<V> it = tree.iterator();
            private V last;

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public V next() {
                return last = it.next();
            }

            @Override
            public void remove() {
                if (last == null) {
                    throw new IllegalStateException();
                }
                it.remove();
                hash.remove(last);
                last = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        return hash.toArray();
    }

    @Override
    public Object[] toArray(Object[] a) {
        return hash.toArray(a);
    }

    @Override
    public boolean add(V e) {
        hash.add(e);
        return tree.add(e);
    }

    @Override
    public boolean remove(Object o) {
        hash.remove(o);
        return tree.remove(o);
    }

    @Override
    public boolean containsAll(Collection c) {
        return hash.containsAll(c);
    }

    @Override
    public boolean addAll(Collection c) {
        tree.addAll(c);
        return hash.addAll(c);
    }

    @Override
    public boolean retainAll(Collection c) {
        tree.retainAll(c);
        return hash.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection c) {
        tree.removeAll(c);
        return hash.removeAll(c);
    }

    @Override
    public void clear() {
        hash.clear();
        tree.clear();
    }

    public V first() {
        return tree.first();
    }

}
