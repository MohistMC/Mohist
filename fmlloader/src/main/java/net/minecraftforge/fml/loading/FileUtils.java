/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.fml.loading;

import com.mohistmc.util.i18n.i18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

import static net.minecraftforge.fml.loading.LogMarkers.CORE;

public class FileUtils
{
    private static final Logger LOGGER = LogManager.getLogger();

    public static Path getOrCreateDirectory(Path dirPath, String dirLabel) {
        if (!Files.isDirectory(dirPath.getParent())) {
            getOrCreateDirectory(dirPath.getParent(), "parent of "+dirLabel);
        }
        if (!Files.isDirectory(dirPath))
        {
            LOGGER.debug(CORE, i18n.get("fileutils.1", dirLabel, dirPath));
            try {
                Files.createDirectory(dirPath);
            } catch (IOException e) {
                if (e instanceof FileAlreadyExistsException) {
                    LOGGER.fatal(CORE, i18n.get("fileutils.2", dirLabel));
                } else {
                    LOGGER.fatal(CORE, i18n.get("fileutils.3", dirLabel), e);
                }
                throw new RuntimeException("Problem creating directory", e);
            }
            LOGGER.debug(CORE, i18n.get("fileutils.4", dirLabel, dirPath));
        } else {
            LOGGER.debug(CORE, i18n.get("fileutils.5", dirLabel, dirPath));
        }
        return dirPath;
    }


    public static String fileExtension(final Path path) {
        String fileName = path.getFileName().toString();
        int idx = fileName.lastIndexOf('.');
        if (idx > -1) {
            return fileName.substring(idx+1);
        } else {
            return "";
        }
    }
}
