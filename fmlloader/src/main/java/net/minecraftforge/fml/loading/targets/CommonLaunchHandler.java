/*
 * Minecraft Forge - Forge Development LLC
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.fml.loading.targets;

import com.mohistmc.util.i18n.i18n;
import cpw.mods.modlauncher.api.ILaunchHandlerService;
import cpw.mods.modlauncher.api.ITransformingClassLoaderBuilder;
import net.minecraftforge.fml.loading.LogMarkers;
import net.minecraftforge.api.distmarker.Dist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public abstract class CommonLaunchHandler implements ILaunchHandlerService {
    public record LocatedPaths(List<Path> minecraftPaths, BiPredicate<String, String> minecraftFilter, List<List<Path>> otherModPaths, List<Path> otherArtifacts) {}

    protected static final Logger LOGGER = LogManager.getLogger();

    public abstract Dist getDist();

    public abstract String getNaming();

    public boolean isProduction() {
        return false;
    }

    public boolean isData() {
        return false;
    }

    public abstract LocatedPaths getMinecraftPaths();

    @Override
    public void configureTransformationClassLoader(final ITransformingClassLoaderBuilder builder) {

    }

    protected final Map<String, List<Path>> getModClasses() {
        final String modClasses = Optional.ofNullable(System.getenv("MOD_CLASSES")).orElse("");
        LOGGER.debug(LogMarkers.CORE, i18n.get("commonlaunchhandler.1", modClasses));

        record ExplodedModPath(String modid, Path path) {}
        // "a/b/;c/d/;" -> "modid%%c:\fish\pepper;modid%%c:\fish2\pepper2\;modid2%%c:\fishy\bums;modid2%%c:\hmm"
        final var modClassPaths = Arrays.stream(modClasses.split(File.pathSeparator))
                .map(inp -> inp.split("%%", 2))
                .map(splitString -> new ExplodedModPath(splitString.length == 1 ? "defaultmodid" : splitString[0], Paths.get(splitString[splitString.length - 1])))
                .collect(Collectors.groupingBy(ExplodedModPath::modid, Collectors.mapping(ExplodedModPath::path, Collectors.toList())));

        LOGGER.debug(LogMarkers.CORE, i18n.get("commonlaunchhandler.2", modClassPaths));

        //final var explodedTargets = ((Map<String, List<ExplodedDirectoryLocator.ExplodedMod>>)arguments).computeIfAbsent("explodedTargets", a -> new ArrayList<>());
        //modClassPaths.forEach((modlabel,paths) -> explodedTargets.add(new ExplodedDirectoryLocator.ExplodedMod(modlabel, paths)));
        return modClassPaths;
    }
}
