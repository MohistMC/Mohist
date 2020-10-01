package com.mohistmc;

import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;
import org.spigotmc.TicksPerSecondCommand;
import com.mohistmc.common.async.MohistThreadBox;
import com.mohistmc.configuration.MohistConfig;
import com.mohistmc.util.i18n.Message;


public class WatchMohist implements Runnable {

    private static long Time = 0L;
    private static long WarnTime = 0L;

    @Override
    public void run() {
        long curTime = System.currentTimeMillis();
        if (Time > 0L && curTime - Time > 2000L && curTime - WarnTime > 30000L) {
            WarnTime = curTime;
            MohistMC.LOGGER.warn(Message.getString("watchmohist.1"));

            double[] tps = Bukkit.getTPS();
            String[] tpsAvg = new String[tps.length];
            for (int i = 0; i < tps.length; i++) {
                tpsAvg[i] = TicksPerSecondCommand.format(tps[i]);
            }

            MohistMC.LOGGER.warn(Message.getFormatString("watchmohist.2", new Object[]{String.valueOf(curTime - Time), StringUtils.join(tpsAvg, ", ")}));
            MohistMC.LOGGER.warn(Message.getString("watchmohist.3"));
            for (StackTraceElement stack : MinecraftServer.getServerInst().primaryThread.getStackTrace()) {
                MohistMC.LOGGER.warn(Message.getString("watchmohist.4") + stack);
            }
            MohistMC.LOGGER.warn(Message.getString("watchmohist.1"));
        }
    }

    public static void start() {
        if (isEnable()) {
            MohistThreadBox.WatchMohist.scheduleAtFixedRate(new WatchMohist(), 30000L, 500L, TimeUnit.MILLISECONDS);
        }
    }

    public static void update() {
        if (isEnable()) {
            Time = System.currentTimeMillis();
        }
    }

    public static void stop() {
        if (isEnable()) {
            MohistThreadBox.WatchMohist.shutdown();
        }
    }

    public static boolean isEnable(){
        return MohistConfig.instance.getBoolean("mohist.watchdog_mohist");
    }
}