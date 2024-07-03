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
import org.bukkit.entity.Monster;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/25 23:56:03
 */
public class EntityClear {

    public static final ScheduledExecutorService ENTITYCLEAR_ITEM = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("EntityClear - Item"));
    public static final ScheduledExecutorService ENTITYCLEAR_MONSTER = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("EntityClear - Item"));

    public static void start() {
        if (MohistConfig.clear_item) {
            ENTITYCLEAR_ITEM.scheduleAtFixedRate(() -> {
                if (MinecraftServer.getServer().hasStopped()) {
                    return;
                }
                run_item();
            }, 1000 * 60, 1000L * MohistConfig.clear_item_time, TimeUnit.MILLISECONDS);
        }
        if (MohistConfig.clear_monster) {
            ENTITYCLEAR_MONSTER.scheduleAtFixedRate(() -> {
                if (MinecraftServer.getServer().hasStopped()) {
                    return;
                }
                run_monster();
            }, 1000 * 60, 1000L * MohistConfig.clear_monster_time, TimeUnit.MILLISECONDS);
        }
    }

    public static void stop() {
        ENTITYCLEAR_ITEM.shutdown();
        ENTITYCLEAR_MONSTER.shutdown();
    }

    public static void run_item() {
        AtomicInteger size_item = new AtomicInteger(0);
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Item item) {
                    if (!MohistConfig.clear_item_whitelist.contains(item.getItemStack().getType().name())) {
                        entity.remove();
                        size_item.addAndGet(1);
                    }
                }
            }
        }
        if (!MohistConfig.clear_item_msg.isEmpty()){
            Bukkit.broadcastMessage(MohistConfig.clear_item_msg.replace("&", "§").replace("%size%", String.valueOf(size_item.getAndSet(0))));
        }
    }

    public static void run_monster() {
        AtomicInteger size_monster = new AtomicInteger(0);
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Monster monster) {
                    if (!MohistConfig.clear_monster_whitelist.contains(monster.getType().name()) && monster.getCustomName() == null) {
                        entity.remove();
                        size_monster.addAndGet(1);
                    }
                }
            }
        }
        if (!MohistConfig.clear_monster_msg.isEmpty()){
            Bukkit.broadcastMessage(MohistConfig.clear_monster_msg.replace("&", "§").replace("%size%", String.valueOf(size_monster.getAndSet(0))));
        }
    }
}
