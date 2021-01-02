package org.spigotmc;

import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;

public class WatchdogThread extends Thread
{

    private static WatchdogThread instance;
    private final long timeoutTime;
    private final boolean restart;
    private volatile long lastTick;
    private volatile boolean stopping;

    private WatchdogThread(long timeoutTime, boolean restart)
    {
        super( "Spigot Watchdog Thread" );
        this.timeoutTime = timeoutTime;
        this.restart = restart;
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
            if ( lastTick != 0 && monotonicMillis() > lastTick + timeoutTime )
            {
                Logger log = Bukkit.getServer().getLogger();
                log.log( Level.SEVERE, "------------------------------" );
                log.log( Level.SEVERE, com.mohistmc.util.i18n.i18n.get("watchdogthread.1" ));
                log.log( Level.SEVERE, com.mohistmc.util.i18n.i18n.get("watchdogthread.2" ));
                log.log( Level.SEVERE, "\t "+com.mohistmc.util.i18n.i18n.get("watchdogthread.3" ));
                log.log( Level.SEVERE, com.mohistmc.util.i18n.i18n.get("watchdogthread.4" ));
                log.log( Level.SEVERE, "\t "+com.mohistmc.util.i18n.i18n.get("watchdogthread.5" ));
                log.log( Level.SEVERE, com.mohistmc.util.i18n.i18n.get("watchdogthread.6" ));
                log.log( Level.SEVERE, com.mohistmc.util.i18n.i18n.get("watchdogthread.7" ));
                log.log( Level.SEVERE, com.mohistmc.util.i18n.i18n.get("watchdogthread.8" + Bukkit.getServer().getVersion() ));
                //
                if ( net.minecraft.world.World.lastPhysicsProblem != null )
                {
                    log.log( Level.SEVERE, "------------------------------" );
                    log.log( Level.SEVERE, com.mohistmc.util.i18n.i18n.get("watchdogthread.9" ));
                    log.log( Level.SEVERE, "near " + net.minecraft.world.World.lastPhysicsProblem );
                }
                //
                log.log( Level.SEVERE, "------------------------------" );
                log.log( Level.SEVERE, com.mohistmc.util.i18n.i18n.get("watchdogthread.10" ));
                dumpThread( ManagementFactory.getThreadMXBean().getThreadInfo( MinecraftServer.getServer().serverThread.getId(), Integer.MAX_VALUE ), log );
                log.log( Level.SEVERE, "------------------------------" );
                //
                log.log( Level.SEVERE, com.mohistmc.util.i18n.i18n.get("watchdogthread.11" ));
                ThreadInfo[] threads = ManagementFactory.getThreadMXBean().dumpAllThreads( true, true );
                for ( ThreadInfo thread : threads )
                {
                    dumpThread( thread, log );
                }
                log.log( Level.SEVERE, "------------------------------" );

                if ( restart && !MinecraftServer.getServer().hasStopped() )
                {
                    RestartCommand.restart();
                }
                break;
            }

            try
            {
                sleep( 10000 );
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
        log.log( Level.SEVERE, com.mohistmc.util.i18n.i18n.get("watchdogthread.12" ) + thread.getThreadName() );
        log.log( Level.SEVERE, "\tPID: " + thread.getThreadId()
                + " | " + com.mohistmc.util.i18n.i18n.get("watchdogthread.13" ) + thread.isSuspended()
                + " | " + com.mohistmc.util.i18n.i18n.get("watchdogthread.14" ) + thread.isInNative()
                + " | " + com.mohistmc.util.i18n.i18n.get("watchdogthread.15" ) + thread.getThreadState() );
        if ( thread.getLockedMonitors().length != 0 )
        {
            log.log( Level.SEVERE, "\t" + com.mohistmc.util.i18n.i18n.get("watchdogthread.16" ));
            for ( MonitorInfo monitor : thread.getLockedMonitors() )
            {
                log.log( Level.SEVERE, "\t\t" + com.mohistmc.util.i18n.i18n.get("watchdogthread.17" )+ monitor.getLockedStackFrame() );
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
