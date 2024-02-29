package com.mohistmc.plugins;

import com.mohistmc.MohistConfig;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.thread.NamedThreadFactory;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/25 23:56:03
 */
public class EntityClear {

    public static final ScheduledExecutorService ENTITYCLEAR = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("EntityClear"));

    public static void start() {
        ENTITYCLEAR.scheduleAtFixedRate(() -> {
            if (MinecraftServer.getServer().hasStopped()) {
                return;
            }
            if (MohistConfig.clear_item) run();
        }, 1000 * 60, 1000L * MohistConfig.clear_item__time, TimeUnit.MILLISECONDS);
    }

    public static void run() {
        AtomicInteger size = new AtomicInteger(0);
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Item item) {
                    if (!MohistConfig.clear_item__whitelist.contains(item.getItemStack().getType().name())) {
                        entity.remove();
                        size.addAndGet(1);
                    }
                }
            }
        }
        if (!MohistConfig.clear_item__msg.isEmpty()) Bukkit.broadcastMessage(MohistConfig.clear_item__msg.replace("&", "§").replace("%size%", String.valueOf(size.getAndSet(0))));
    }
}
