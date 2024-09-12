package com.mohistmc.paper.event.server;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ServerTickEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final int tickNumber;
    private final double tickDuration;
    private final long timeEnd;

    public ServerTickEndEvent(int tickNumber, double tickDuration, long timeRemaining) {
        this.tickNumber = tickNumber;
        this.tickDuration = tickDuration;
        this.timeEnd = System.nanoTime() + timeRemaining;
    }

    /**
     * @return What tick this was since start (first tick = 1)
     */
    public int getTickNumber() {
        return tickNumber;
    }

    /**
     * @return Time in milliseconds of how long this tick took
     */
    public double getTickDuration() {
        return tickDuration;
    }

    /**
     * Amount of nanoseconds remaining before the next tick should start.
     *
     * If this value is negative, then that means the server has exceeded the tick time limit and TPS has been lost.
     *
     * Method will continuously return the updated time remaining value. (return value is not static)
     *
     * @return Amount of nanoseconds remaining before the next tick should start
     */
    public long getTimeRemaining() {
        return this.timeEnd - System.nanoTime();
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
