package com.mohistmc;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.thread.NamedThreadFactory;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.spigotmc.TicksPerSecondCommand;

public class WatchMohist implements Runnable {

    public static ScheduledThreadPoolExecutor WatchMohist = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("WatchMohist"));

    private static long Time = 0L;
    private static long WarnTime = 0L;

    public static void start() {
        if (isEnable()) {
            WatchMohist.scheduleAtFixedRate(new WatchMohist(), 60000L, 500L, TimeUnit.MILLISECONDS);
        }
    }

    public static void update() {
        if (isEnable()) {
            Time = System.currentTimeMillis();
        }
    }

    public static void stop() {
        if (isEnable()) {
            WatchMohist.shutdown();
        }
    }

    public static boolean isEnable() {
        return MohistConfig.watchdog_mohist;
    }

    @Override
    public void run() {
        long curTime = System.currentTimeMillis();
        if (Time > 0L && curTime - Time > 2000L && curTime - WarnTime > 60000L) {
            WarnTime = curTime;
            MohistMC.LOGGER.warn(MohistMC.i18n.as("watchmohist.1"));

            double[] tps = Bukkit.getTPS();
            String[] tpsAvg = new String[tps.length];
            for (int i = 0; i < tps.length; i++) {
                tpsAvg[i] = TicksPerSecondCommand.format(tps[i]);
            }

            MohistMC.LOGGER.warn(MohistMC.i18n.as("watchmohist.2", String.valueOf(curTime - Time), StringUtils.join(tpsAvg, ", ")));
            MohistMC.LOGGER.warn(MohistMC.i18n.as("watchmohist.3"));
            MohistMC.LOGGER.warn(MohistMC.i18n.as("watchmohist.4"));
            for (StackTraceElement stack : MinecraftServer.getServer().serverThread.getStackTrace()) {
                MohistMC.LOGGER.warn("{}{}", MohistMC.i18n.as("watchmohist.5"), stack);
            }
            MohistMC.LOGGER.warn(MohistMC.i18n.as("watchmohist.1"));
        }
    }
}