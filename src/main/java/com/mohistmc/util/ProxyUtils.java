package com.mohistmc.util;

import com.mohistmc.MohistConfig;
import org.spigotmc.SpigotConfig;

/**
 * @author Mgazul by MohistMC
 * @date 2023/10/20 1:43:19
 */
public class ProxyUtils {

    public static boolean ignoreRejected() {
        return /*MohistConfig.ignoreConnectionType && */ (MohistConfig.velocity_enabled || SpigotConfig.bungee);
    }
}
