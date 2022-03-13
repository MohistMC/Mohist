/*
 * Minecraft Forge - Forge Development LLC
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.fml.loading.moddiscovery;

import com.mohistmc.util.i18n.i18n;
import net.minecraftforge.fml.loading.LogMarkers;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.forgespi.locating.IModLocator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ExplodedDirectoryLocator implements IModLocator {
    private static final Logger LOGGER = LogManager.getLogger();

    public record ExplodedMod(String modid, List<Path> paths) {}

    private final List<ExplodedMod> explodedMods = new ArrayList<>();
    private final Map<ExplodedMod, IModFile> mods = new HashMap<>();

    @Override
    public List<IModFile> scanMods() {
        explodedMods.forEach(explodedMod ->
                ModJarMetadata.buildFile(this,
                        jar->jar.findFile("/META-INF/mods.toml").isPresent(),
                        (a,b) -> true,
                        explodedMod.paths().toArray(Path[]::new))
                .ifPresentOrElse(f->mods.put(explodedMod, f), () -> LOGGER.warn(LogMarkers.LOADING, i18n.get("explodeddirectorylocator.1", explodedMod.paths().get(0).toString()))));
        return List.copyOf(mods.values());
    }

    @Override
    public String name() {
        return "exploded directory";
    }

    @Override
    public void scanFile(final IModFile file, final Consumer<Path> pathConsumer) {
        LOGGER.debug(LogMarkers.SCAN, i18n.get("explodeddirectorylocator.2", file.getFilePath().toString()));
        try (Stream<Path> files = Files.find(file.getSecureJar().getRootPath(), Integer.MAX_VALUE, (p, a) -> p.getNameCount() > 0 && p.getFileName().toString().endsWith(".class"))) {
            files.forEach(pathConsumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.debug(LogMarkers.SCAN, i18n.get("explodeddirectorylocator.3", file.getFilePath().toString()));
    }

    @Override
    public String toString()
    {
        return "{ExplodedDir locator}";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initArguments(final Map<String, ?> arguments) {
        final var explodedTargets = ((Map<String, List<ExplodedMod>>) arguments).get("explodedTargets");
        if (explodedTargets != null && !explodedTargets.isEmpty()) {
            explodedMods.addAll(explodedTargets);
        }
    }

    @Override
    public boolean isValid(final IModFile modFile) {
        return true;
    }
}
