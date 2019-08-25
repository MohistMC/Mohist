package red.mohist;

import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.bukkit.Bukkit;
import red.mohist.i18n.Message;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang.StringUtils.join;
import static org.spigotmc.TicksPerSecondCommand.format;

public class WatchMohist implements Runnable {

    private static ScheduledThreadPoolExecutor timer = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("WatchMohist").daemon(true).build());
    private static long Time = 0L;
    private static long WarnTime = 0L;

    public static void update() {
        WatchMohist.Time = System.currentTimeMillis();
    }

    public static void start() {
        timer.scheduleAtFixedRate(new WatchMohist(), 48000L, 600L, TimeUnit.MILLISECONDS);
    }

    public static void stop() {
        timer.shutdown();
    }

    @Override
    public void run() {
        long curTime = System.currentTimeMillis();
        if (WatchMohist.Time > 0L && curTime - WatchMohist.Time > 2400L && curTime - WatchMohist.WarnTime > 48000L && String.valueOf(curTime - WatchMohist.Time).contains("-")) {
            WatchMohist.WarnTime = curTime;
            Mohist.LOGGER.warn(Message.getString("watchmohist.1"));

            double[] tps = Bukkit.getTPS();
            String[] tpsAvg = new String[tps.length];
            for (int i = 0; i < tps.length; i++) {
                tpsAvg[i] = format(tps[i]);
            }

            Mohist.LOGGER.warn(Message.getFormatString("watchmohist.2", new Object[]{String.valueOf(curTime - WatchMohist.Time), join(tpsAvg, ", ")}));
            Mohist.LOGGER.warn(Message.getString("watchmohist.3"));
            for (StackTraceElement stack : MinecraftServer.getServerInst().primaryThread.getStackTrace()) {
                Mohist.LOGGER.warn(Message.getString("watchmohist.4") + stack);
            }
            Mohist.LOGGER.warn(Message.getString("watchmohist.1"));
        }
    }
}