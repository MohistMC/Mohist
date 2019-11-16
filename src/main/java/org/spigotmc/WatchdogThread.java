package org.spigotmc;

import java.io.File;
import java.io.FileWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.cauldron.CauldronHooks;
import net.minecraftforge.cauldron.configuration.CauldronConfig;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;


public class WatchdogThread extends Thread
{

    private static WatchdogThread instance;
    private final long timeoutTime;
    private final long warningTime;
    private final boolean restart;
    private volatile long lastTick;
    private volatile boolean stopping;
    private volatile long lastWarning;

    private WatchdogThread(long timeoutTime, boolean restart)
    {
        super( "Spigot Watchdog Thread" );
        this.timeoutTime = timeoutTime;
        this.warningTime = Math.max(timeoutTime/3, 5000);
        this.restart = restart;
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
        instance.lastTick = System.currentTimeMillis();
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
        while (!stopping)
        {
            // Trigger watchdog logic
            long currentTime = System.currentTimeMillis();
            if ((lastTick != 0 && currentTime > lastTick + timeoutTime))
            {
                Logger log = Bukkit.getServer().getLogger();
                log.log(Level.SEVERE, "The server has stopped responding!");
                log.log(Level.SEVERE, "Please report this to https://github.com/CyberdyneCC/Thermos/issues");
                log.log(Level.SEVERE, "Be sure to include ALL relevant console errors and Minecraft crash reports");
                log.log(Level.SEVERE, "Cauldron version: " + Bukkit.getServer().getVersion());
                
                // Cauldron start - add more logging info
                log.log(Level.SEVERE, "The server is going slow. Last server tick was " + (currentTime - lastTick) + "ms ago");
                double tps = Math.min(20, Math.round(net.minecraft.server.MinecraftServer.currentTps * 10) / 10.0);
                log.log(Level.SEVERE, "Last Tick: " + lastTick + " Current Time: " + currentTime + " Warning: " + warningTime + " Timeout: " + timeoutTime);
                log.log(Level.SEVERE, "[TPS]: " + tps + " Server Tick #" + net.minecraft.server.MinecraftServer.getServer().getTickCounter());
                log.log(Level.SEVERE, "Last recorded TPS: " + tps);

                // Dump world info
                log.log(Level.SEVERE, "------------------------------");
                log.log(Level.SEVERE, "Loaded dimensions:");
                for (net.minecraft.world.WorldServer world : MinecraftServer.getServer().worlds)
                {
                    log.log(Level.SEVERE, "  Dimension:" + world.provider.dimensionId);
                    log.log(Level.SEVERE,
                            "  Loaded Chunks: " + world.theChunkProviderServer.loadedChunkHashMap_KC.rawThermos().size() + " Active Chunks: " + world.activeChunkSet.size()
                                    + " Entities: " + world.loadedEntityList.size() + " Tile Entities: " + world.loadedTileEntityList.size());
                    log.log(Level.SEVERE, "  Entities Last Tick: " + world.entitiesTicked);
                    log.log(Level.SEVERE, "  Tiles Last Tick: " + world.tilesTicked);
                }

                log.log(Level.SEVERE, "------------------------------");

                if (MinecraftServer.getServer().cauldronConfig.dumpChunksOnDeadlock.getValue())
                {
                    // Dump detailed world info to a watchdog report log
                    File file = new File(new File(new File("."), "crash-reports"), "watchdog-chunks-"
                            + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");
                    log.log(Level.SEVERE, "------------------------------");
                    log.log(Level.SEVERE, "Writing watchdog detailed info to: " + file);
                    CauldronHooks.writeChunks(file, false);
                    log.log(Level.SEVERE, "Writing complete");
                    log.log(Level.SEVERE, "------------------------------");
                }
                if (MinecraftServer.getServer().cauldronConfig.dumpHeapOnDeadlock.getValue())
                {
                    // Dump detailed world info to a watchdog report log
                    File file = new File(new File(new File("."), "crash-reports"), "watchdog-heap-"
                            + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.bin");
                    log.log(Level.SEVERE, "------------------------------");
                    log.log(Level.SEVERE, "Writing heap dump to: " + file);
                    CauldronHooks.dumpHeap(file, true);
                    log.log(Level.SEVERE, "Writing complete");
                    log.log(Level.SEVERE, "------------------------------");
                }
                // Cauldron end
                
                log.log(Level.SEVERE, "------------------------------");
                log.log(Level.SEVERE, "Server thread dump (Look for plugins here before reporting to Cauldron!):");
                dumpThread(ManagementFactory.getThreadMXBean().getThreadInfo(MinecraftServer.getServer().primaryThread.getId(), Integer.MAX_VALUE), log);
                log.log(Level.SEVERE, "------------------------------");
                //
                log.log(Level.SEVERE, "Entire Thread Dump:");
                ThreadInfo[] threads = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);
                for (ThreadInfo thread : threads)
                {
                    dumpThread(thread, log);
                }
                log.log(Level.SEVERE, "------------------------------");

                if (restart)
                {
                    RestartCommand.restart();
                }
                break;
            }
            // Cauldron + start - add warning info 
            else if (lastTick != 0 && System.currentTimeMillis() > lastTick + warningTime)
            {
                Logger log = Bukkit.getServer().getLogger();
                lastWarning = System.currentTimeMillis();
                // Print what the last server TPS was...
                log.log(Level.WARNING, "The server is going slow. Last server tick was " + ((System.currentTimeMillis() - lastTick)) + "ms ago");
                double tps = Math.min(20, Math.round(net.minecraft.server.MinecraftServer.currentTps * 10) / 10.0);
                log.log(Level.WARNING, "Last Tick: " + lastTick + " Current Time: " + currentTime + " Warning: " + warningTime + " Timeout: " + timeoutTime);
                log.log(Level.WARNING, "[TPS]: " + tps + " Server Tick #" + net.minecraft.server.MinecraftServer.getServer().getTickCounter());
                for (net.minecraft.world.WorldServer world : MinecraftServer.getServer().worlds)
                {
                    log.log(Level.WARNING, "  Dimension:" + world.provider.dimensionId);
                    log.log(Level.WARNING, "  Loaded Chunks: " + world.theChunkProviderServer.loadedChunkHashMap_KC.rawThermos().size() +
                            " Active Chunks: " + world.activeChunkSet.size() +
                            " Entities: " + world.loadedEntityList.size() +
                            " Tile Entities: " + world.loadedTileEntityList.size());
                    log.log(Level.WARNING, "  Entities Last Tick: " + world.entitiesTicked);
                    log.log(Level.WARNING, "  Tiles Last Tick: " + world.tilesTicked);
                }
                if (MinecraftServer.getServer().cauldronConfig.dumpThreadsOnWarn.getValue())
                {
                    log.log(Level.WARNING, "Server thread dump (Look for mods or plugins here before reporting to Cauldron!):");
                    dumpThread(ManagementFactory.getThreadMXBean().getThreadInfo(MinecraftServer.getServer().primaryThread.getId(), Integer.MAX_VALUE), log,
                            Level.WARNING);
                }
            }
            // Cauldron end

            try
            {
                sleep(10000);
            }
            catch (InterruptedException ex)
            {
                interrupt();
            }
        }
    }
    private static void dumpThread(ThreadInfo thread, Logger log)
    {
        dumpThread(thread, log, Level.SEVERE);
    }

    private static void dumpThread(ThreadInfo thread, Logger log, Level level)
    {
        if (thread == null) return;
        if ( thread.getThreadState() != State.WAITING )
        {
            log.log( level, "------------------------------" );
            //
            log.log( level, "Current Thread: " + thread.getThreadName() );
            log.log( level, "\tPID: " + thread.getThreadId()
                    + " | Suspended: " + thread.isSuspended()
                    + " | Native: " + thread.isInNative()
                    + " | State: " + thread.getThreadState() 
                    + " | Blocked Time: " + thread.getBlockedTime()     // Cauldron add info about blocked time
                    + " | Blocked Count: " + thread.getBlockedCount()); // Cauldron add info about blocked count
            
            if ( thread.getLockedMonitors().length != 0 )
            {
                log.log( level, "\tThread is waiting on monitor(s):" );
                for ( MonitorInfo monitor : thread.getLockedMonitors() )
                {
                    log.log( level, "\t\tLocked on:" + monitor.getLockedStackFrame() );
                }
            }
            if ( thread.getLockOwnerId() != -1 ) log.log( level, "\tLock Owner Id: " + thread.getLockOwnerId()); // Cauldron + add info about lock owner thread id
            log.log( level, "\tStack:" );
            //
            StackTraceElement[] stack = thread.getStackTrace();
            for ( int line = 0; line < stack.length; line++ )
            {
                log.log( level, "\t\t" + stack[line].toString() );
            }
        }
    }
}
