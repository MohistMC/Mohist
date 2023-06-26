package org.example;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Mgazul by MohistMC
 * @date 2023/6/26 20:02:25
 */
public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            MohistPapiHook.init();
        }
    }
}