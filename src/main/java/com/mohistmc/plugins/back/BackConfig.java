package com.mohistmc.plugins.back;

import com.mohistmc.MohistConfig;
import com.mohistmc.plugins.config.MohistPluginConfig;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class BackConfig extends MohistPluginConfig {

    public static BackConfig INSTANCE;

    public BackConfig(File file) {
        super(file);
    }

    public static void init() {
        INSTANCE = new BackConfig(new File("mohist-config", "back.yml"));
    }

    public void saveLocation(Player player, Location location, BackType backType) {
        if (!MohistConfig.back_enable) return;
        yaml.set(player.getUniqueId() + ".location.world", location.getWorld().getName());
        yaml.set(player.getUniqueId() + ".location.x", location.getX());
        yaml.set(player.getUniqueId() + ".location.y", location.getY());
        yaml.set(player.getUniqueId() + ".location.z", location.getZ());
        yaml.set(player.getUniqueId() + ".location.pitch", location.getPitch());
        yaml.set(player.getUniqueId() + ".location.yaw", location.getYaw());
        yaml.set(player.getUniqueId() + ".type", backType.name());
        save();
    }

    public Location getLocation(Player player) {
        final World world = Bukkit.getWorld(yaml.getString(player.getUniqueId() + ".location.world"));
        final double x = yaml.getInt(player.getUniqueId() + ".location.x");
        final double y = yaml.getInt(player.getUniqueId() + ".location.y");
        final double z = yaml.getInt(player.getUniqueId() + ".location.z");
        final float pitch = (float) yaml.getInt(player.getUniqueId() + ".location.pitch");
        final float yaw = (float) yaml.getInt(player.getUniqueId() + ".location.yaw");
        return new Location(world, x, y, z, yaw, pitch);
    }

    public BackType getBackType(Player player) {
        return BackType.valueOf(yaml.getString(player.getUniqueId() + ".type"));
    }
}
