package red.mohist.model;

import java.util.function.Supplier;

/**
 *
 * @author pyz
 * @date 2019/7/5 11:17 PM
 */
public class LazyField<T> {
    private final Supplier<T> supplier;
    private final Object lock = new Object();
    private T value;
    private volatile boolean init = false;

    private LazyField(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> LazyField<T> from(Supplier<T> supplier) {
        return new LazyField<>(supplier);
    }

    public T get() {
        if (init) {
            return value;
        }
        synchronized (lock) {
            if (init) {
                return value;
            }
            value = supplier.get();
            init = true;
        }
        return this.value;
    }

    public LazyField<T> set(T value) {
        synchronized (lock) {
            this.value = value;
            init = true;
        }
        return this;
    }
}
