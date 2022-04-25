/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.fml.loading;

import com.mohistmc.util.i18n.i18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;

public class LibraryFinder {
    private static final Logger LOGGER = LogManager.getLogger();
    private static Path libsPath;
    static Path findLibsPath() {
        if (libsPath == null) {
            libsPath = Path.of(System.getProperty("libraryDirectory","crazysnowmannonsense/cheezwhizz"));
            if (!Files.isDirectory(libsPath)) {
                throw new IllegalStateException("Missing libraryDirectory system property, cannot continue");
            }
        }
        return libsPath;
    }

    static Path getForgeLibraryPath(final String mcVersion, final String forgeVersion, final String forgeGroup) {
        Path forgePath = findLibsPath().resolve(MavenCoordinateResolver.get(forgeGroup, "forge", "", "universal", mcVersion+"-"+forgeVersion));
        LOGGER.debug(LogMarkers.CORE, i18n.get("libraryfinder.1", forgePath, pathStatus(forgePath)));
        return forgePath;
    }

    static String pathStatus(final Path path) {
        return Files.exists(path) ? "present" : "missing";
    }

    static Path[] getMCPaths(final String mcVersion, final String mcpVersion, final String forgeVersion, final String forgeGroup, final String type) {
        Path srgMcPath = findLibsPath().resolve(MavenCoordinateResolver.get("net.minecraft", type, "", "srg", mcVersion+"-"+mcpVersion));
        Path mcExtrasPath = findLibsPath().resolve(MavenCoordinateResolver.get("net.minecraft", type, "", "extra", mcVersion+"-"+mcpVersion));
        Path patchedBinariesPath = findLibsPath().resolve(MavenCoordinateResolver.get(forgeGroup, "forge", "", type, mcVersion+"-"+forgeVersion));
        LOGGER.debug(LogMarkers.CORE, i18n.get("libraryfinder.2", srgMcPath.toString(), pathStatus(srgMcPath)));
        LOGGER.debug(LogMarkers.CORE, i18n.get("libraryfinder.3", mcExtrasPath.toString(), pathStatus(mcExtrasPath)));
        LOGGER.debug(LogMarkers.CORE, i18n.get("libraryfinder.4", patchedBinariesPath.toString(), pathStatus(patchedBinariesPath)));
        return new Path[] { patchedBinariesPath, mcExtrasPath, srgMcPath };
    }

    public static Path findPathForMaven(final String group, final String artifact, final String extension, final String classifier, final String version) {
        return findLibsPath().resolve(MavenCoordinateResolver.get(group, artifact, extension, classifier, version));
    }
    public static Path findPathForMaven(final String maven) {
        return findLibsPath().resolve(MavenCoordinateResolver.get(maven));
    }
}
