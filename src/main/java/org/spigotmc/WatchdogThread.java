package org.spigotmc;

import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mohistmc.util.i18n.i18n;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;

public class WatchdogThread extends Thread
{

    private static WatchdogThread instance;
    private final long timeoutTime;
    private final boolean restart;
    private final long earlyWarningEvery; // Paper - Timeout time for just printing a dump but not restarting
    private final long earlyWarningDelay; // Paper
    public static volatile boolean hasStarted; // Paper
    private long lastEarlyWarning; // Paper - Keep track of short dump times to avoid spamming console with short dumps
    private volatile long lastTick;
    private volatile boolean stopping;

    private WatchdogThread(long timeoutTime, boolean restart)
    {
        super( "Spigot Watchdog Thread" );
        this.timeoutTime = timeoutTime;
        this.restart = restart;
        earlyWarningEvery = Math.min(5000, timeoutTime); // Paper
        earlyWarningDelay = Math.min(10000, timeoutTime); // Paper
    }

    private static long monotonicMillis()
    {
        return System.nanoTime() / 1000000L;
    }

    public static void doStart(int timeoutTime, boolean restart)
    {
        if ( instance == null )
        {
            instance = new WatchdogThread( timeoutTime * 1000L, restart );
            instance.start();
        }
    }

    public static void tick()
    {
        instance.lastTick = monotonicMillis();
    }

    public static void doStop()
    {
        if ( instance != null )
        {
            instance.stopping = true;
        }
    }

    @Override
    public void run()
    {
        while ( !stopping )
        {
            //
// Paper start
            Logger log = Bukkit.getServer().getLogger();
            long currentTime = monotonicMillis();
            if ( lastTick != 0 && timeoutTime > 0 && currentTime > lastTick + earlyWarningEvery && !Boolean.getBoolean("disable.watchdog") )
            {
                boolean isLongTimeout = currentTime > lastTick + timeoutTime;
                // Don't spam early warning dumps
                if ( !isLongTimeout && (earlyWarningEvery <= 0 || !hasStarted || currentTime < lastEarlyWarning + earlyWarningEvery || currentTime < lastTick + earlyWarningDelay)) continue;
                if ( !isLongTimeout && MinecraftServer.getServer().hasStopped()) continue; // Don't spam early watchdog warnings during shutdown, we'll come back to this...
                lastEarlyWarning = currentTime;
                if (isLongTimeout) {
                    // Paper end
                log.log( Level.SEVERE, "------------------------------" );
                log.log( Level.SEVERE, i18n.get("watchdogthread.1" ));
                log.log( Level.SEVERE, i18n.get("watchdogthread.2" ));
                log.log( Level.SEVERE, "\t "+ i18n.get("watchdogthread.3" ));
                log.log( Level.SEVERE, i18n.get("watchdogthread.4" ));
                log.log( Level.SEVERE, "\t "+ i18n.get("watchdogthread.5" ));
                log.log( Level.SEVERE, i18n.get("watchdogthread.6" ));
                log.log( Level.SEVERE, i18n.get("watchdogthread.7" ));
                log.log( Level.SEVERE, i18n.get("watchdogthread.8") + " " + Bukkit.getServer().getVersion());
                //
                if ( net.minecraft.world.World.lastPhysicsProblem != null )
                {
                    log.log( Level.SEVERE, "------------------------------" );
                    log.log( Level.SEVERE, i18n.get("watchdogthread.9" ));
                    log.log( Level.SEVERE, "near " + net.minecraft.world.World.lastPhysicsProblem );
                }
                } else
                {
                    log.log(Level.SEVERE, "--- DO NOT REPORT THIS TO PAPER - THIS IS NOT A BUG OR A CRASH  - " + Bukkit.getServer().getVersion() + " ---");
                    log.log(Level.SEVERE, "The server has not responded for " + (currentTime - lastTick) / 1000 + " seconds! Creating thread dump");
                }
                // Paper end - Different message for short timeout
                log.log( Level.SEVERE, "------------------------------" );
                log.log( Level.SEVERE, i18n.get("watchdogthread.10" ));
                dumpThread( ManagementFactory.getThreadMXBean().getThreadInfo( MinecraftServer.getServer().serverThread.getId(), Integer.MAX_VALUE ), log );
                log.log( Level.SEVERE, "------------------------------" );
                //
                // Paper start - Only print full dump on long timeouts
                if ( isLongTimeout )
                {
                log.log( Level.SEVERE, i18n.get("watchdogthread.11" ));
                ThreadInfo[] threads = ManagementFactory.getThreadMXBean().dumpAllThreads( true, true );
                for ( ThreadInfo thread : threads )
                {
                    dumpThread( thread, log );
                }
                } else {
                    log.log(Level.SEVERE, "--- DO NOT REPORT THIS TO PAPER - THIS IS NOT A BUG OR A CRASH ---");
                }
                log.log( Level.SEVERE, "------------------------------" );

                if ( isLongTimeout ) {
                    if (restart && !MinecraftServer.getServer().hasStopped()) {
                        RestartCommand.restart();
                    }
                    break;
                } // Paper end
            }

            try
            {
                sleep( 1000 ); // Paper - Reduce check time to every second instead of every ten seconds, more consistent and allows for short timeout
            } catch ( InterruptedException ex )
            {
                interrupt();
            }
        }
    }

    private static void dumpThread(ThreadInfo thread, Logger log)
    {
        log.log( Level.SEVERE, "------------------------------" );
        //
        log.log( Level.SEVERE, i18n.get("watchdogthread.12" ) + thread.getThreadName() );
        log.log( Level.SEVERE, "\tPID: " + thread.getThreadId()
                + " | " + i18n.get("watchdogthread.13" ) + thread.isSuspended()
                + " | " + i18n.get("watchdogthread.14" ) + thread.isInNative()
                + " | " + i18n.get("watchdogthread.15" ) + thread.getThreadState() );
        if ( thread.getLockedMonitors().length != 0 )
        {
            log.log( Level.SEVERE, "\t" + i18n.get("watchdogthread.16" ));
            for ( MonitorInfo monitor : thread.getLockedMonitors() )
            {
                log.log( Level.SEVERE, "\t\t" + i18n.get("watchdogthread.17" )+ monitor.getLockedStackFrame() );
            }
        }
        log.log( Level.SEVERE, "\tStack:" );
        //
        for ( StackTraceElement stack : thread.getStackTrace() )
        {
            log.log( Level.SEVERE, "\t\t" + stack );
        }
    }
}
