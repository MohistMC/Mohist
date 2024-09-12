package com.mohistmc.plugins.world.utils;

import com.mohistmc.api.ServerAPI;
import com.mohistmc.api.WorldAPI;
import com.mohistmc.util.YamlUtils;
import java.io.File;
import java.util.Objects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class ConfigByWorlds {
    public static File f = new File("mohist-config", "worlds.yml");
    public static FileConfiguration config = YamlConfiguration.loadConfiguration(ConfigByWorlds.f);

    public static void addInfo(String w, String info) {
        World world = Bukkit.getWorld(w);
        if (ConfigByWorlds.f.exists() && world != null) {
            if (config.getString("worlds." + world.getName()) != null) {
                config.set("worlds." + world.getName() + ".info", info);
            }
            init();
        }
    }

    public static void addname(String w, String info) {
        World world = Bukkit.getWorld(w);
        if (ConfigByWorlds.f.exists() && world != null) {
            if (config.getString("worlds." + world.getName()) != null) {
                config.set("worlds." + world.getName() + ".name", info);
            }
            init();
        }
    }

    public static void setnandu(Player player, String nandu) {
        World world = player.getWorld();
        if (ConfigByWorlds.f.exists()) {
            if (config.getString("worlds." + world.getName()) != null) {
                config.set("worlds." + world.getName() + ".difficulty", nandu);
            }
            init();
        }
    }

    public static void addWorld(String w, boolean isMohist) {
        if (Bukkit.getWorld(w) != null) {
            World world = Bukkit.getWorld(w);
            String world_name = world.getName();
            if (ConfigByWorlds.f.exists()) {
                if (config.getString("worlds." + world_name + ".mohist") == null) {
                    config.set("worlds." + world_name + ".seed", world.getSeed());
                    config.set("worlds." + world_name + ".environment", world.getEnvironment().name());
                    config.set("worlds." + world_name + ".name", world_name);
                    config.set("worlds." + world_name + ".info", "-/-");
                    config.set("worlds." + world_name + ".difficulty", world.getDifficulty().name());
                    config.set("worlds." + world_name + ".mohist", isMohist);
                    config.set("worlds." + world_name + ".keepspawninmemory", true);
                }
                init();
            }
        }
    }

    public static void init() {
        YamlUtils.save(f, config);
    }

    public static void initMods(Level level, double pSize) {
        // Mohist - set worldborder size to worlds.ymls
        if (level != null && level instanceof ServerLevel serverLevel) {
            CraftWorld world = serverLevel.getWorld();
            config.set("worlds." + world.getName() + ".worldborder", pSize);
            if (world.isMods()) {
                config.set("worlds." + world.getName() + ".ismods", world.isMods());
                config.set("worlds." + world.getName() + ".modName", world.getModid());
            }
            init();
        }
    }

    public static void loadWorlds() {
        ConfigurationSection section = config.getConfigurationSection("worlds");
        if (section != null) {
            for (String w : section.getKeys(false)) {
                boolean canload = true;
                if (Objects.equals(w, "DIM1")) {
                    if (!Bukkit.getAllowNether()) {
                        config.set("worlds." + w, null);
                        init();
                        canload = false;
                    }
                } else if (Objects.equals(w, "DIM-1")) {
                    if (!Bukkit.getAllowEnd()) {
                        config.set("worlds." + w, null);
                        init();
                        canload = false;
                    }
                }
                String environment = "NORMAL";
                String difficulty = "EASY";
                boolean isMods = false;
                boolean isMohist = false;
                String modName = null;
                boolean keepspawninmemory = true;
                boolean isVoid = false;
                if (Bukkit.getWorld(w) == null) {
                    long seed = -1L;
                    if (config.get("worlds." + w + ".seed") != null) {
                        seed = config.getLong("worlds." + w + ".seed");
                    }
                    if (config.get("worlds." + w + ".environment") != null) {
                        environment = config.getString("worlds." + w + ".environment");
                    }
                    if (config.get("worlds." + w + ".difficulty") != null) {
                        difficulty = config.getString("worlds." + w + ".difficulty");
                    }
                    if (config.get("worlds." + w + ".ismods") != null) {
                        isMods = config.getBoolean("worlds." + w + ".ismods");
                    }
                    if (config.get("worlds." + w + ".modName") != null) {
                        modName = config.getString("worlds." + w + ".modName");
                    }
                    if (config.get("worlds." + w + ".mohist") != null) {
                        isMohist = config.getBoolean("worlds." + w + ".mohist");
                    }
                    if (config.get("worlds." + w + ".keepspawninmemory") != null) {
                        keepspawninmemory = config.getBoolean("worlds." + w + ".keepspawninmemory");
                    }
                    if (config.get("worlds." + w + ".void") != null) {
                        isVoid = config.getBoolean("worlds." + w + ".void");
                    }
                    // Worlds created by mods are no longer loaded when the mod is unloaded
                    if (isMods && !ServerAPI.hasMod(modName)) {
                        config.set("worlds." + w, null);
                        init();
                        canload = false;
                    }
                    if (!isMohist) {
                        canload = false;
                    }
                    if (canload) {
                        WorldCreator wc = new WorldCreator(w);
                        if (isVoid) wc.generator(new WorldAPI.VoidGenerator());
                        wc.seed(seed);
                        wc.environment(World.Environment.valueOf(environment));
                        wc.keepSpawnInMemory(keepspawninmemory);
                        wc.createWorld();
                    }
                }
                World world = Bukkit.getWorld(w);
                if (world != null) {
                    if (difficulty != null) {
                        world.setDifficulty(Difficulty.valueOf(difficulty));
                    }
                    if (config.get("worlds." + w + ".worldborder") != null) {
                        world.getWorldBorder().setSize(config.getDouble("worlds." + w + ".worldborder"));
                    }
                    // Fix mods world
                    ((CraftWorld)world).getHandle().keepSpawnInMemory = keepspawninmemory;
                }
            }
        }
    }

    public static void removeWorld(String w) {
        if (Bukkit.getWorld(w) != null) {
            World world = Bukkit.getWorld(w);
            if (f.exists() && world != null) {
                if (config.getString("worlds." + world.getName()) != null) {
                    config.set("worlds." + world.getName(), null);
                    init();
                }
            }
        }
    }

    public static void addSpawn(Location location) {
        World world = location.getWorld();
        if (f.exists() && world != null) {
            if (config.getString("worlds." + world.getName()) != null) {
                config.set("worlds." + world.getName() + ".spawn.x", location.getX());
                config.set("worlds." + world.getName() + ".spawn.y", location.getY());
                config.set("worlds." + world.getName() + ".spawn.z", location.getZ());
                config.set("worlds." + world.getName() + ".spawn.yaw", location.getYaw());
                config.set("worlds." + world.getName() + ".spawn.pitch", location.getPitch());
            }
            init();
        }
    }

    public static void getSpawn(String w, Player player) {
        World world = Bukkit.getWorld(w);
        if (f.exists() && world != null) {
            if (config.getString("worlds." + world.getName()) != null) {
                double x = config.getDouble("worlds." + world.getName() + ".spawn.x");
                double y = config.getDouble("worlds." + world.getName() + ".spawn.y");
                double z = config.getDouble("worlds." + world.getName() + ".spawn.z");
                double yaw = config.getDouble("worlds." + world.getName() + ".spawn.yaw");
                double pitch = config.getDouble("worlds." + world.getName() + ".spawn.pitch");
                player.teleport(new Location(world, x, y, z, (float) yaw, (float) pitch));
            } else {
                player.teleport(world.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.MOHIST);
            }
        }
    }

    public static void difficulty(String w, String difficulty) {
        config.set("worlds." + w + ".difficulty", difficulty);
        init();
    }

    @Deprecated
    public static void aVoid(String w, boolean isVoid) {
        config.set("worlds." + w + ".void", isVoid);
        init();
    }
}
