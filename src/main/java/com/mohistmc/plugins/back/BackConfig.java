package com.mohistmc.plugins.back;

import com.mohistmc.util.YamlUtils;
import java.io.File;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class BackConfig {

    public static File config = new File("mohist-config", "back.yml");
    public static FileConfiguration yaml = YamlConfiguration.loadConfiguration(config);

    public static void init() {
        if (!config.exists()) {
            save();
        }
    }

    public static void save() {
        YamlUtils.save(config, yaml);
    }

    public static void saveLocation(Player player, Location location, BackType backType) {
        yaml.set(player.getUniqueId() + ".location", location);
        yaml.set(player.getUniqueId() + ".type", backType);
        save();
    }

    public static Location getLocation(Player player) {
        return yaml.getLocation(player.getUniqueId() + ".location");
    }

    public static BackType getBackType(Player player) {
        return BackType.valueOf(yaml.getString(player.getUniqueId() + ".type"));
    }

    public static boolean hasLocation(Player player) {
        return yaml.get(player.getUniqueId().toString()) != null;
    }
}
