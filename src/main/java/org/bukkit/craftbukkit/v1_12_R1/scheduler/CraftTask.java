package org.bukkit.craftbukkit.v1_12_R1.scheduler;

import co.aikar.timings.MinecraftTimings;
import co.aikar.timings.Timing;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class CraftTask implements BukkitTask, Runnable { // Spigot

    public static final int ERROR = 0;
    public static final int NO_REPEATING = -1;
    public static final int CANCEL = -2;
    public static final int PROCESS_FOR_FUTURE = -3;
    public static final int DONE_FOR_FUTURE = -4;
    public final Runnable task; // Paper
    private final Plugin plugin;
    private final int id;
    public Timing timings; // Paper
    private volatile CraftTask next = null;
    /**
     * -1 means no repeating <br>
     * -2 means cancel <br>
     * -3 means processing for Future <br>
     * -4 means done for Future <br>
     * Never 0 <br>
     * >0 means number of ticks to wait between each execution
     */
    private volatile long period;
    private long nextRun;

    CraftTask() {
        this(null, null, CraftTask.NO_REPEATING, CraftTask.NO_REPEATING);
    }

    CraftTask(final Runnable task) {
        this(null, task, CraftTask.NO_REPEATING, CraftTask.NO_REPEATING);
    }

    CraftTask(final Plugin plugin, final Runnable task, final int id, final long period) { // Paper
        this.plugin = plugin;
        this.task = task;
        this.id = id;
        this.period = period;
        timings = task != null ? MinecraftTimings.getPluginTaskTimings(this, period) : null; // Paper
    }

    public final int getTaskId() {
        return id;
    }

    public final Plugin getOwner() {
        return plugin;
    }

    public boolean isSync() {
        return true;
    }

    public void run() {
        if (timings != null && isSync()) timings.startTiming(); // Paper
        task.run();
        if (timings != null && isSync()) timings.stopTiming(); // Paper
    }

    long getPeriod() {
        return period;
    }

    void setPeriod(long period) {
        this.period = period;
    }

    long getNextRun() {
        return nextRun;
    }

    void setNextRun(long nextRun) {
        this.nextRun = nextRun;
    }

    CraftTask getNext() {
        return next;
    }

    void setNext(CraftTask next) {
        this.next = next;
    }

    Class<? extends Runnable> getTaskClass() {
        return task.getClass();
    }

    @Override
    public boolean isCancelled() {
        return (period == CraftTask.CANCEL);
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(id);
    }

    /**
     * This method properly sets the status to cancelled, synchronizing when required.
     *
     * @return false if it is a craft future task that has already begun execution, true otherwise
     */
    boolean cancel0() {
        setPeriod(CraftTask.CANCEL);
        return true;
    }
}