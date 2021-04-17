package com.destroystokyo.paper;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.v1_16_R3.scheduler.CraftTask;

/**
 * Reporting wrapper to catch exceptions not natively
 */
public class ServerSchedulerReportingWrapper implements Runnable {

    private final CraftTask internalTask;

    public ServerSchedulerReportingWrapper(CraftTask internalTask) {
        this.internalTask = Preconditions.checkNotNull(internalTask, "internalTask");
    }

    @Override
    public void run() {
        try {
            internalTask.run();
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable t) {
        }
    }

    public CraftTask getInternalTask() {
        return internalTask;
    }
}
