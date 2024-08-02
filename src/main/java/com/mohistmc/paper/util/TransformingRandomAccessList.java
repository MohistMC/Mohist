package com.mohistmc.paper.util;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.function.Function;
import java.util.function.Predicate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;


import static com.google.common.base.Preconditions.checkNotNull;

public final class TransformingRandomAccessList<F, T> extends AbstractList<T> implements RandomAccess {
    final List<F> fromList;
    final Function<? super F, ? extends T> toFunction;
    final Function<? super T, ? extends F> fromFunction;

    /**
     * Create a new {@link TransformingRandomAccessList}.
     *
     * @param fromList     backing list
     * @param toFunction   function mapping backing list element type to transformed list element type
     * @param fromFunction function mapping transformed list element type to backing list element type
     */
    public TransformingRandomAccessList(
            final @NonNull List<F> fromList,
            final @NonNull Function<? super F, ? extends T> toFunction,
            final @NonNull Function<? super T, ? extends F> fromFunction
    ) {
        this.fromList = checkNotNull(fromList);
        this.toFunction = checkNotNull(toFunction);
        this.fromFunction = checkNotNull(fromFunction);
    }

    @Override
    public void clear() {
        this.fromList.clear();
    }

    @Override
    public T get(int index) {
        return this.toFunction.apply(this.fromList.get(index));
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return this.listIterator();
    }

    @Override
    public @NotNull ListIterator<T> listIterator(int index) {
        return new TransformedListIterator<F, T>(this.fromList.listIterator(index)) {
            @Override
            T transform(F from) {
                return TransformingRandomAccessList.this.toFunction.apply(from);
            }

            @Override
            F transformBack(T from) {
                return TransformingRandomAccessList.this.fromFunction.apply(from);
            }
        };
    }

    @Override
    public boolean isEmpty() {
        return this.fromList.isEmpty();
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        checkNotNull(filter);
        return this.fromList.removeIf(element -> filter.test(this.toFunction.apply(element)));
    }

    @Override
    public T remove(int index) {
        return this.toFunction.apply(this.fromList.remove(index));
    }

    @Override
    public int size() {
        return this.fromList.size();
    }

    @Override
    public T set(int i, T t) {
        return this.toFunction.apply(this.fromList.set(i, this.fromFunction.apply(t)));
    }

    @Override
    public void add(int i, T t) {
        this.fromList.add(i, this.fromFunction.apply(t));
    }

    static abstract class TransformedListIterator<F, T> implements ListIterator<T>, Iterator<T> {
        final Iterator<F> backingIterator;

        TransformedListIterator(ListIterator<F> backingIterator) {
            this.backingIterator = checkNotNull((Iterator<F>) backingIterator);
        }

        static <A> ListIterator<A> cast(Iterator<A> iterator) {
            return (ListIterator<A>) iterator;
        }

        private ListIterator<F> backingIterator() {
            return cast(this.backingIterator);
        }

        @Override
        public final boolean hasPrevious() {
            return this.backingIterator().hasPrevious();
        }

        @Override
        public final T previous() {
            return this.transform(this.backingIterator().previous());
        }

        @Override
        public final int nextIndex() {
            return this.backingIterator().nextIndex();
        }

        @Override
        public final int previousIndex() {
            return this.backingIterator().previousIndex();
        }

        @Override
        public void set(T element) {
            this.backingIterator().set(this.transformBack(element));
        }

        @Override
        public void add(T element) {
            this.backingIterator().add(this.transformBack(element));
        }

        abstract T transform(F from);

        abstract F transformBack(T to);

        @Override
        public final boolean hasNext() {
            return this.backingIterator.hasNext();
        }

        @Override
        public final T next() {
            return this.transform(this.backingIterator.next());
        }

        @Override
        public final void remove() {
            this.backingIterator.remove();
        }
    }
}
