package com.mohistmc.plugins.back;

import com.mohistmc.plugins.config.MohistPluginConfig;
import java.io.File;
import org.bukkit.Location;
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
        yaml.set(player.getUniqueId() + ".location", location);
        yaml.set(player.getUniqueId() + ".type", backType.name());
        save();
    }

    public Location getLocation(Player player) {
        return yaml.getLocation(player.getUniqueId() + ".location");
    }

    public BackType getBackType(Player player) {
        return BackType.valueOf(yaml.getString(player.getUniqueId() + ".type"));
    }
}
