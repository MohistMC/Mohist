package org.spigotmc;

import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import red.mohist.Mohist;
import red.mohist.i18n.Message;

import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;

public class WatchdogThread extends Thread {

    private static WatchdogThread instance;
    private final long timeoutTime;
    private final boolean restart;
    private volatile long lastTick;
    private volatile boolean stopping;

    private WatchdogThread(long timeoutTime, boolean restart) {
        super("Spigot Watchdog Thread");
        this.timeoutTime = timeoutTime;
        this.restart = restart;
    }

    public static void doStart(int timeoutTime, boolean restart) {
        if (instance == null) {
            instance = new WatchdogThread(timeoutTime * 1000L, restart);
            instance.start();
        }
    }

    public static void tick() {
        instance.lastTick = System.currentTimeMillis();
    }

    public static void doStop() {
        if (instance != null) {
            instance.stopping = true;
        }
    }

    private static void dumpThread(ThreadInfo thread, Logger log) {
        log.error("------------------------------");
        //
        log.error(Message.getString(Message.Watchdog_9) + thread.getThreadName());
        log.error("\t" + Message.getString(Message.Watchdog_10) + thread.getThreadId()
                + " | " + Message.getString(Message.Watchdog_11) + thread.isSuspended()
                + " | " + Message.getString(Message.Watchdog_12) + thread.isInNative()
                + " | " + Message.getString(Message.Watchdog_13) + thread.getThreadState());
        if (thread.getLockedMonitors().length != 0) {
            log.error("\t" + Message.getString(Message.Watchdog_16));
            for (MonitorInfo monitor : thread.getLockedMonitors()) {
                log.error("\t\t" + Message.getString(Message.Watchdog_14) + monitor.getLockedStackFrame());
            }
        }
        log.error("\t" + Message.getString(Message.Watchdog_15));
        //
        for (StackTraceElement stack : thread.getStackTrace()) {
            log.error("\t\t" + stack);
        }
    }

    @Override
    public void run() {
        while (!stopping) {
            //
            if (lastTick != 0 && System.currentTimeMillis() > lastTick + timeoutTime && !Boolean.getBoolean("disable.watchdog")) {
                Logger log = Mohist.LOGGER;
                log.error(Message.getString(Message.Watchdog_1));
                log.error(Message.getString(Message.Watchdog_2));
                log.error(Message.getString(Message.Watchdog_3));
                log.error(Message.getString(Message.Watchdog_4) + Mohist.getVersion());
                //
                if (net.minecraft.world.World.haveWeSilencedAPhysicsCrash) {
                    log.error("------------------------------");
                    log.error(Message.getString(Message.Watchdog_5));
                    log.error(Message.getString(Message.Watchdog_6) + net.minecraft.world.World.blockLocation);
                }
                log.error("------------------------------");
                log.error(Message.getString(Message.Watchdog_7));
                dumpThread(ManagementFactory.getThreadMXBean().getThreadInfo(MinecraftServer.getServerInst().primaryThread.getId(), Integer.MAX_VALUE), log);
                log.error("------------------------------");
                //
                log.error(Message.getString(Message.Watchdog_8));
                ThreadInfo[] threads = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);
                for (ThreadInfo thread : threads) {
                    dumpThread(thread, log);
                }
                log.error("------------------------------");

                if (restart) {
                    MinecraftServer.getServerInst().primaryThread.stop();
                }
                break;
            }

            try {
                sleep(10000);
            } catch (InterruptedException ex) {
                interrupt();
            }
        }
    }
}
