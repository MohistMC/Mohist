/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.versions.mcp;

import com.mohistmc.util.i18n.i18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.JarVersionLookupHandler;

import static net.minecraftforge.fml.Logging.CORE;

public class MCPVersion {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String mcVersion;
    private static final String mcpVersion;
    static {
        LOGGER.debug(CORE, i18n.get("mcpversion.1", MCPVersion.class.getPackage(), MCPVersion.class.getClassLoader()));
        mcVersion = JarVersionLookupHandler.getSpecificationVersion(MCPVersion.class).orElse(FMLLoader.versionInfo().mcVersion());
        if (mcVersion == null) throw new RuntimeException("Missing MC version, cannot continue");

        mcpVersion = JarVersionLookupHandler.getImplementationVersion(MCPVersion.class).orElse(FMLLoader.versionInfo().mcpVersion());
        if (mcpVersion == null) throw new RuntimeException("Missing MCP version, cannot continue");

        LOGGER.debug(CORE, i18n.get("mcpversion.2", mcVersion));
        LOGGER.debug(CORE, i18n.get("mcpversion.3", mcpVersion));
    }
    public static String getMCVersion() {
        return mcVersion;
    }

    public static String getMCPVersion() {
        return mcpVersion;
    }

    public static String getMCPandMCVersion()
    {
        return mcVersion+"-"+mcpVersion;
    }
}
