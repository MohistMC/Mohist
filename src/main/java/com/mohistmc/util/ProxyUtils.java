package com.mohistmc.util;

import com.mohistmc.MohistConfig;
import org.spigotmc.SpigotConfig;

/**
 * @author Mgazul by MohistMC
 * @date 2023/10/18 19:15:48
 */
public class ProxyUtils {

    public static boolean ignoreRejected() {
        if (MohistConfig.velocity) {
            return true;
        }
        return SpigotConfig.bungee;
    }
}
