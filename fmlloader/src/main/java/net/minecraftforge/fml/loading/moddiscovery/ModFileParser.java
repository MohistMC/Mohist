/*
 * Minecraft Forge - Forge Development LLC
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.fml.loading.moddiscovery;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mohistmc.util.i18n.i18n;
import net.minecraftforge.fml.loading.LogMarkers;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.forgespi.locating.ModFileFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModFileParser {
    private static final Logger LOGGER = LogManager.getLogger();

    public static IModFileInfo readModList(final ModFile modFile, final ModFileFactory.ModFileInfoParser parser) {
        return parser.build(modFile);
    }

    public static IModFileInfo modsTomlParser(final IModFile imodFile) {
        ModFile modFile = (ModFile) imodFile;
        LOGGER.debug(LogMarkers.LOADING,i18n.get("modfileparser.1", modFile.getFilePath()));
        final Path modsjson = modFile.findResource("META-INF", "mods.toml");
        if (!Files.exists(modsjson)) {
            LOGGER.warn(LogMarkers.LOADING, i18n.get("modfileparser.2", modFile.getFilePath()));
            return null;
        }

        final FileConfig fileConfig = FileConfig.builder(modsjson).build();
        fileConfig.load();
        fileConfig.close();
        final NightConfigWrapper configWrapper = new NightConfigWrapper(fileConfig);
        final ModFileInfo modFileInfo = new ModFileInfo(modFile, configWrapper);
        configWrapper.setFile(modFileInfo);
        return modFileInfo;
    }

    protected static List<CoreModFile> getCoreMods(final ModFile modFile) {
        Map<String,String> coreModPaths;
        try {
            final Path coremodsjson = modFile.findResource("META-INF", "coremods.json");
            if (!Files.exists(coremodsjson)) {
                return Collections.emptyList();
            }
            final Type type = new TypeToken<Map<String, String>>() {}.getType();
            final Gson gson = new Gson();
            coreModPaths = gson.fromJson(Files.newBufferedReader(coremodsjson), type);
        } catch (IOException e) {
            LOGGER.debug(LogMarkers.LOADING,i18n.get("modfileparser.3"), e);
            return Collections.emptyList();
        }

        return coreModPaths.entrySet().stream()
                .peek(e-> LOGGER.debug(LogMarkers.LOADING,i18n.get("modfileparser.4", e.getKey(), e.getValue())))
                .map(e -> new CoreModFile(e.getKey(), modFile.findResource(e.getValue()),modFile))
                .collect(Collectors.toList());
    }
}
