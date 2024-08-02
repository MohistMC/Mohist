package ca.spottedleaf.concurrentutil.executor;

import ca.spottedleaf.concurrentutil.executor.standard.PrioritisedExecutor;
import ca.spottedleaf.concurrentutil.util.ConcurrentUtil;
import java.util.function.BooleanSupplier;

public interface BaseExecutor {

    /**
     * Returns whether every task scheduled to this queue has been removed and executed or cancelled. If no tasks have been queued,
     * returns {@code true}.
     *
     * @return {@code true} if all tasks that have been queued have finished executing or no tasks have been queued, {@code false} otherwise.
     */
    public default boolean haveAllTasksExecuted() {
        // order is important
        // if new tasks are scheduled between the reading of these variables, scheduled is guaranteed to be higher -
        // so our check fails, and we try again
        final long completed = this.getTotalTasksExecuted();
        final long scheduled = this.getTotalTasksScheduled();

        return completed == scheduled;
    }

    /**
     * Returns the number of tasks that have been scheduled or execute or are pending to be scheduled.
     */
    public long getTotalTasksScheduled();

    /**
     * Returns the number of tasks that have fully been executed.
     */
    public long getTotalTasksExecuted();


    /**
     * Waits until this queue has had all of its tasks executed (NOT removed). See {@link #haveAllTasksExecuted()}
     * <p>
     *     This call is most effective after a {@link #shutdown()} call, as the shutdown call guarantees no tasks can
     *     be executed and the waitUntilAllExecuted call makes sure the queue is empty. Effectively, using shutdown then using
     *     waitUntilAllExecuted ensures this queue is empty - and most importantly, will remain empty.
     * </p>
     * <p>
     *     This method is not guaranteed to be immediately responsive to queue state, so calls may take significantly more
     *     time than expected. Effectively, do not rely on this call being fast - even if there are few tasks scheduled.
     * </p>
     * <p>
     *     Note: Interruptions to the the current thread have no effect. Interrupt status is also not affected by this cal.
     * </p>
     *
     * @throws IllegalStateException If the current thread is not allowed to wait
     */
    public default void waitUntilAllExecuted() throws IllegalStateException {
        long failures = 1L; // start at 0.25ms

        while (!this.haveAllTasksExecuted()) {
            Thread.yield();
            failures = ConcurrentUtil.linearLongBackoff(failures, 250_000L, 5_000_000L); // 500us, 5ms
        }
    }

    /**
     * Executes the next available task.
     * <p>
     *     If there is a task with priority {@link PrioritisedExecutor.Priority#BLOCKING} available, then that such task is executed.
     * </p>
     * <p>
     *     If there is a task with priority {@link PrioritisedExecutor.Priority#IDLE} available then that task is only executed
     *     when there are no other tasks available with a higher priority.
     * </p>
     * <p>
     *     If there are no tasks that have priority {@link PrioritisedExecutor.Priority#BLOCKING} or {@link PrioritisedExecutor.Priority#IDLE}, then
     *     this function will be biased to execute tasks that have higher priorities.
     * </p>
     *
     * @return {@code true} if a task was executed, {@code false} otherwise
     * @throws IllegalStateException If the current thread is not allowed to execute a task
     */
    public boolean executeTask() throws IllegalStateException;

    /**
     * Executes all queued tasks.
     *
     * @return {@code true} if a task was executed, {@code false} otherwise
     * @throws IllegalStateException If the current thread is not allowed to execute a task
     */
    public default boolean executeAll() {
        if (!this.executeTask()) {
            return false;
        }

        while (this.executeTask());

        return true;
    }

    /**
     * Waits and executes tasks until the condition returns {@code true}.
     * <p>
     *     WARNING: This function is <i>not</i> suitable for waiting until a deadline!
     *     Use {@link #executeUntil(long)} or {@link #executeConditionally(BooleanSupplier, long)} instead.
     * </p>
     */
    public default void executeConditionally(final BooleanSupplier condition) {
        long failures = 0;
        while (!condition.getAsBoolean()) {
            if (this.executeTask()) {
                failures = failures >>> 2;
            } else {
                failures = ConcurrentUtil.linearLongBackoff(failures, 100_000L, 10_000_000L); // 100us, 10ms
            }
        }
    }

    /**
     * Waits and executes tasks until the condition returns {@code true} or {@code System.nanoTime() >= deadline}.
     */
    public default void executeConditionally(final BooleanSupplier condition, final long deadline) {
        long failures = 0;
        // double check deadline; we don't know how expensive the condition is
        while ((System.nanoTime() < deadline) && !condition.getAsBoolean() && (System.nanoTime() < deadline)) {
            if (this.executeTask()) {
                failures = failures >>> 2;
            } else {
                failures = ConcurrentUtil.linearLongBackoffDeadline(failures, 100_000L, 10_000_000L, deadline); // 100us, 10ms
            }
        }
    }

    /**
     * Waits and executes tasks until {@code System.nanoTime() >= deadline}.
     */
    public default void executeUntil(final long deadline) {
        long failures = 0;
        while (System.nanoTime() < deadline) {
            if (this.executeTask()) {
                failures = failures >>> 2;
            } else {
                failures = ConcurrentUtil.linearLongBackoffDeadline(failures, 100_000L, 10_000_000L, deadline); // 100us, 10ms
            }
        }
    }

    /**
     * Prevent further additions to this queue. Attempts to add after this call has completed (potentially during) will
     * result in {@link IllegalStateException} being thrown.
     * <p>
     *     This operation is atomic with respect to other shutdown calls
     * </p>
     * <p>
     *     After this call has completed, regardless of return value, this queue will be shutdown.
     * </p>
     *
     * @return {@code true} if the queue was shutdown, {@code false} if it has shut down already
     * @throws UnsupportedOperationException If this queue does not support shutdown
     */
    public default boolean shutdown() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns whether this queue has shut down. Effectively, whether new tasks will be rejected - this method
     * does not indicate whether all of the tasks scheduled have been executed.
     * @return Returns whether this queue has shut down.
     */
    public default boolean isShutdown() {
        return false;
    }

    public static interface BaseTask extends Cancellable {

        /**
         * Causes a lazily queued task to become queued or executed
         *
         * @throws IllegalStateException If the backing queue has shutdown
         * @return {@code true} If the task was queued, {@code false} if the task was already queued/cancelled/executed
         */
        public boolean queue();

        /**
         * Forces this task to be marked as completed.
         *
         * @return {@code true} if the task was cancelled, {@code false} if the task has already completed or is being completed.
         */
        @Override
        public boolean cancel();

        /**
         * Executes this task. This will also mark the task as completing.
         * <p>
         *     Exceptions thrown from the runnable will be rethrown.
         * </p>
         *
         * @return {@code true} if this task was executed, {@code false} if it was already marked as completed.
         */
        public boolean execute();
    }
}
