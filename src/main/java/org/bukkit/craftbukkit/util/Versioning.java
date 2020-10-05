package org.bukkit.craftbukkit.util;

import com.mohistmc.MohistMC;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;

public final class Versioning {
    public static String getBukkitVersion() {
        return MohistMC.getVersion();
    }
}
