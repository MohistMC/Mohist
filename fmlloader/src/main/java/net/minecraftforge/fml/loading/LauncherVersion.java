/*
 * Minecraft Forge - Forge Development LLC
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.fml.loading;

import com.mohistmc.util.i18n.i18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.minecraftforge.fml.loading.LogMarkers.CORE;

public class LauncherVersion {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String launcherVersion;

    static {
        String vers = JarVersionLookupHandler.getImplementationVersion(LauncherVersion.class).orElse(System.getenv("LAUNCHER_VERSION"));
        if (vers == null) throw new RuntimeException("Missing FMLLauncher version, cannot continue");
        launcherVersion = vers;
        LOGGER.debug(CORE, i18n.get("launcherversion.1", launcherVersion));
    }

    public static String getVersion()
    {
        return launcherVersion;
    }
}
