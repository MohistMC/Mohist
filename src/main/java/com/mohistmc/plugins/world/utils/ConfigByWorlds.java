package com.mohistmc.plugins.world.utils;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.File;
import java.io.IOException;

public class ConfigByWorlds {
    public static File f = new File("mohist-config", "worlds.yml");
    public static FileConfiguration config = YamlConfiguration.loadConfiguration(ConfigByWorlds.f);

    public static void addInfo(String w, String info) {
        World world = Bukkit.getWorld(w);
        if (ConfigByWorlds.f.exists()) {
            try {
                config.load(ConfigByWorlds.f);
                if (config.getString("worlds." + world.getName()) != null) {
                    config.set("worlds." + world.getName() + ".info", info);
                }
                config.save(ConfigByWorlds.f);
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        }
    }

    public static void addname(String w, String info) {
        World world = Bukkit.getWorld(w);
        if (ConfigByWorlds.f.exists()) {
            try {
                config.load(ConfigByWorlds.f);
                if (config.getString("worlds." + world.getName()) != null) {
                    config.set("worlds." + world.getName() + ".name", info);
                }
                config.save(ConfigByWorlds.f);
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        }
    }

    public static void setnandu(Player player, String nandu) {
        World world = player.getWorld();
        if (ConfigByWorlds.f.exists()) {
            try {
                config.load(ConfigByWorlds.f);
                if (config.getString("worlds." + world.getName()) != null) {
                    config.set("worlds." + world.getName() + ".difficulty", nandu);
                }
                config.save(ConfigByWorlds.f);
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        }
    }

    public static void addWorld(String w) {
        if (Bukkit.getWorld(w) != null) {
            World world = Bukkit.getWorld(w);
            if (ConfigByWorlds.f.exists()) {
                try {
                    config.load(ConfigByWorlds.f);
                    if (config.getString("worlds." + world.getName()) == null) {
                        config.set("worlds." + world.getName() + ".seed", world.getSeed());
                        config.set("worlds." + world.getName() + ".environment", world.getEnvironment().name());
                        config.set("worlds." + world.getName() + ".name", world.getName());
                        config.set("worlds." + world.getName() + ".info", "-/-");
                        config.set("worlds." + world.getName() + ".difficulty", world.getDifficulty().name());
                    }
                    config.save(ConfigByWorlds.f);
                } catch (Exception e) {
                    e.fillInStackTrace();
                }
            }
        }
    }

    public static void createFile() {
        try {
            if (!ConfigByWorlds.f.exists()) {
                try {
                    config.save(f);
                } catch (IOException var4) {
                    var4.fillInStackTrace();
                }
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public static void loadWorlds() {
        try {
            config.load(ConfigByWorlds.f);
            if (config.getConfigurationSection("worlds.") != null) {
                for (String w : config.getConfigurationSection("worlds.").getKeys(false)) {
                    World world = Bukkit.getWorld(w);
                    String environment = "NORMAL";
                    String difficulty = "EASY";
                    if (world == null && ConfigByWorlds.f.exists()) {
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
                        WorldCreator wc = new WorldCreator(w);
                        wc.seed(seed);
                        wc.environment(World.Environment.valueOf(environment));

                        wc.createWorld();
                    }
                    if (world != null) {
                        if (config.get("worlds." + w + ".difficulty") != null) {
                            difficulty = config.getString("worlds." + w + ".difficulty");
                            world.setDifficulty(Difficulty.valueOf(difficulty));
                        }
                        if (config.get("worlds." + w + ".worldborder") != null) {
                            world.getWorldBorder().setSize(config.getDouble("worlds." + w + ".worldborder"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public static void removeWorld(String w) {
        if (Bukkit.getWorld(w) != null) {
            World world = Bukkit.getWorld(w);
            if (ConfigByWorlds.f.exists()) {
                try {
                    config.load(ConfigByWorlds.f);
                    if (config.getString("worlds." + world.getName()) != null) {
                        config.set("worlds." + world.getName(), null);
                        config.save(ConfigByWorlds.f);
                    }
                } catch (Exception e) {
                    e.fillInStackTrace();
                }
            }
        }
    }

    public static void addSpawn(Location location) {
        World world = location.getWorld();
        if (ConfigByWorlds.f.exists()) {
            try {
                config.load(ConfigByWorlds.f);
                if (config.getString("worlds." + world.getName()) != null) {
                    config.set("worlds." + world.getName() + ".spawn.x", location.getX());
                    config.set("worlds." + world.getName() + ".spawn.y", location.getY());
                    config.set("worlds." + world.getName() + ".spawn.z", location.getZ());
                    config.set("worlds." + world.getName() + ".spawn.yaw", location.getYaw());
                    config.set("worlds." + world.getName() + ".spawn.pitch", location.getPitch());
                }
                config.save(ConfigByWorlds.f);
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        }
    }

    public static void getSpawn(String w, Player player) {
        World world = Bukkit.getWorld(w);
        if (f.exists()) {
            try {
                config.load(f);
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
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        }
    }

    public static void addFlag(World world, String key, Object v) {
        config.set("worlds." + world.getName() + "." + key, v);
        try {
            config.save(ConfigByWorlds.f);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }
}
