/*
 * This file is licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 Daniel Ennis <http://aikar.co>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package co.aikar.timings;

import co.aikar.util.LoadingIntMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.Bukkit;
import red.mohist.util.i18n.Message;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

class TimingHandler implements Timing {

    private static final AtomicInteger idPool = new AtomicInteger(1);
    static Deque<TimingHandler> TIMING_STACK = new ArrayDeque<>();
    final int id = idPool.getAndIncrement();

    final TimingIdentifier identifier;
    final TimingData record;
    private final boolean verbose;
    private final Int2ObjectOpenHashMap<TimingData> children = new LoadingIntMap<>(TimingData::new);
    private final TimingHandler groupHandler;

    private long start = 0;
    private int timingDepth = 0;
    private boolean added;
    private boolean timed;
    private boolean enabled;

    TimingHandler(TimingIdentifier id) {
        this.identifier = id;
        this.verbose = id.name.startsWith("##");
        this.record = new TimingData(this.id);
        this.groupHandler = id.groupHandler;

        TimingIdentifier.getGroup(id.group).handlers.add(this);
        checkEnabled();
    }

    final void checkEnabled() {
        enabled = Timings.timingsEnabled && (!verbose || Timings.verboseEnabled);
    }

    void processTick(boolean violated) {
        if (timingDepth != 0 || record.getCurTickCount() == 0) {
            timingDepth = 0;
            start = 0;
            return;
        }

        record.processTick(violated);
        for (TimingData handler : children.values()) {
            handler.processTick(violated);
        }
    }

    @Override
    public Timing startTimingIfSync() {
        startTiming();
        return this;
    }

    @Override
    public void stopTimingIfSync() {
        stopTiming();
    }

    public Timing startTiming() {
        if (enabled && Bukkit.isPrimaryThread() && ++timingDepth == 1) {
            start = System.nanoTime();
            TIMING_STACK.addLast(this);
        }
        return this;
    }

    public void stopTiming() {
        if (enabled && timingDepth > 0 && Bukkit.isPrimaryThread() && --timingDepth == 0 && start != 0) {
            TimingHandler last;
            while ((last = TIMING_STACK.removeLast()) != this) {
                last.timingDepth = 0;
                String reportTo;
                if ("minecraft".equals(last.identifier.group)) {
                    reportTo = Message.getString("timings.handler.1");
                } else {
                    reportTo = Message.getFormatString("timings.handler.2", new Object[]{last.identifier.group});
                }
                Logger.getGlobal().log(Level.SEVERE, Message.getFormatString("timings.handler.3", new Object[]{reportTo, last.identifier}), new Throwable());
            }
            addDiff(System.nanoTime() - start, TIMING_STACK.peekLast());

            start = 0;
        }
    }

    @Override
    public final void abort() {

    }

    void addDiff(long diff, TimingHandler parent) {
        if (parent != null) {
            parent.children.get(id).add(diff);
        }

        record.add(diff);
        if (!added) {
            added = true;
            timed = true;
            TimingsManager.HANDLERS.add(this);
        }
        if (groupHandler != null) {
            groupHandler.addDiff(diff, parent);
            groupHandler.children.get(id).add(diff);
        }
    }

    /**
     * Reset this timer, setting all values to zero.
     */
    void reset(boolean full) {
        record.reset();
        if (full) {
            timed = false;
        }
        start = 0;
        timingDepth = 0;
        added = false;
        children.clear();
        checkEnabled();
    }

    @Override
    public TimingHandler getTimingHandler() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        return (this == o);
    }

    @Override
    public int hashCode() {
        return id;
    }

    /**
     * This is simply for the Closeable interface so it can be used with try-with-resources ()
     */
    @Override
    public void close() {
        stopTimingIfSync();
    }

    public boolean isSpecial() {
        return this == TimingsManager.FULL_SERVER_TICK || this == TimingsManager.TIMINGS_TICK;
    }

    boolean isTimed() {
        return timed;
    }

    public boolean isEnabled() {
        return enabled;
    }

    TimingData[] cloneChildren() {
        final TimingData[] clonedChildren = new TimingData[children.size()];
        int i = 0;
        for (TimingData child : children.values()) {
            clonedChildren[i++] = child.clone();
        }
        return clonedChildren;
    }
}
