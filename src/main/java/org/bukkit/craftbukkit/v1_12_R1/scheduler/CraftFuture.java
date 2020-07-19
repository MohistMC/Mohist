package org.bukkit.craftbukkit.v1_12_R1.scheduler;

import org.bukkit.plugin.Plugin;

import java.util.concurrent.*;

class CraftFuture<T> extends CraftTask implements Future<T> {

    private final Callable<T> callable;
    private T value;
    private Exception exception = null;

    CraftFuture(final Callable<T> callable, final Plugin plugin, final int id) {
        super(plugin, null, id, CraftTask.NO_REPEATING);
        this.callable = callable;
    }

    public synchronized boolean cancel(final boolean mayInterruptIfRunning) {
        if (getPeriod() != CraftTask.NO_REPEATING) {
            return false;
        }
        setPeriod(CraftTask.CANCEL);
        return true;
    }

    public boolean isDone() {
        final long period = this.getPeriod();
        return period != CraftTask.NO_REPEATING && period != CraftTask.PROCESS_FOR_FUTURE;
    }

    public T get() throws CancellationException, InterruptedException, ExecutionException {
        try {
            return get(0, TimeUnit.MILLISECONDS);
        } catch (final TimeoutException e) {
            throw new Error(e);
        }
    }

    public synchronized T get(long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        timeout = unit.toMillis(timeout);
        long period = this.getPeriod();
        long timestamp = timeout > 0 ? System.currentTimeMillis() : 0L;
        while (true) {
            if (period == CraftTask.NO_REPEATING || period == CraftTask.PROCESS_FOR_FUTURE) {
                this.wait(timeout);
                period = this.getPeriod();
                if (period == CraftTask.NO_REPEATING || period == CraftTask.PROCESS_FOR_FUTURE) {
                    if (timeout == 0L) {
                        continue;
                    }
                    timeout += timestamp - (timestamp = System.currentTimeMillis());
                    if (timeout > 0) {
                        continue;
                    }
                    throw new TimeoutException();
                }
            }
            if (period == CraftTask.CANCEL) {
                throw new CancellationException();
            }
            if (period == CraftTask.DONE_FOR_FUTURE) {
                if (exception == null) {
                    return value;
                }
                throw new ExecutionException(exception);
            }
            throw new IllegalStateException("Expected " + CraftTask.NO_REPEATING + " to " + CraftTask.DONE_FOR_FUTURE + ", got " + period);
        }
    }

    @Override
    public void run() {
        synchronized (this) {
            if (getPeriod() == CraftTask.CANCEL) {
                return;
            }
            setPeriod(CraftTask.PROCESS_FOR_FUTURE);
        }
        try {
            value = callable.call();
        } catch (final Exception e) {
            exception = e;
        } finally {
            synchronized (this) {
                setPeriod(CraftTask.DONE_FOR_FUTURE);
                this.notifyAll();
            }
        }
    }

    synchronized boolean cancel0() {
        if (getPeriod() != CraftTask.NO_REPEATING) {
            return false;
        }
        setPeriod(CraftTask.CANCEL);
        notifyAll();
        return true;
    }
}
