/*
 * Minecraft Forge - Forge Development LLC
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.fml.loading;

import com.mohistmc.util.i18n.i18n;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.api.*;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import cpw.mods.modlauncher.util.ServiceLoaderUtils;
import net.minecraftforge.fml.loading.moddiscovery.BackgroundScanHandler;
import net.minecraftforge.fml.loading.moddiscovery.ModDiscoverer;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModValidator;
import net.minecraftforge.accesstransformer.service.AccessTransformerService;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.progress.EarlyProgressVisualization;
import net.minecraftforge.fml.loading.progress.StartupMessageManager;
import net.minecraftforge.fml.loading.targets.CommonLaunchHandler;
import net.minecraftforge.forgespi.Environment;
import net.minecraftforge.forgespi.coremod.ICoreModProvider;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static net.minecraftforge.fml.loading.LogMarkers.CORE;
import static net.minecraftforge.fml.loading.LogMarkers.SCAN;

public class FMLLoader
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static AccessTransformerService accessTransformer;
    private static ModDiscoverer modDiscoverer;
    private static ICoreModProvider coreModProvider;
    private static ILaunchPluginService eventBus;
    private static LanguageLoadingProvider languageLoadingProvider;
    private static Dist dist;
    private static String naming;
    private static LoadingModList loadingModList;
    private static RuntimeDistCleaner runtimeDistCleaner;
    private static Path gamePath;
    private static VersionInfo versionInfo;
    private static String launchHandlerName;
    private static CommonLaunchHandler commonLaunchHandler;
    public static Runnable progressWindowTick;
    private static ModValidator modValidator;
    public static BackgroundScanHandler backgroundScanHandler;
    private static boolean production;
    private static IModuleLayerManager moduleLayerManager;

    static void onInitialLoad(IEnvironment environment, Set<String> otherServices) throws IncompatibleEnvironmentException
    {
        final String version = LauncherVersion.getVersion();
        LOGGER.debug(CORE, i18n.get("fmlloader.1", version));
        final Package modLauncherPackage = ITransformationService.class.getPackage();
        LOGGER.debug(CORE, i18n.get("fmlloader.2", modLauncherPackage.getImplementationVersion()));
        if (!modLauncherPackage.isCompatibleWith("4.0")) {
            LOGGER.fatal(CORE, i18n.get("fmlloader.3", modLauncherPackage.getSpecificationVersion(), modLauncherPackage.getImplementationVersion(), modLauncherPackage.getImplementationVendor()));
            throw new IncompatibleEnvironmentException("Incompatible modlauncher found "+modLauncherPackage.getSpecificationVersion());
        }

        accessTransformer = (AccessTransformerService) environment.findLaunchPlugin("accesstransformer").orElseThrow(()-> {
            LOGGER.fatal(CORE, i18n.get("fmlloader.4"));
            return new IncompatibleEnvironmentException("Missing AccessTransformer, cannot run");
        });

        final Package atPackage = accessTransformer.getClass().getPackage();
        LOGGER.debug(CORE, i18n.get("fmlloader.5", atPackage.getImplementationVersion()));
        if (!atPackage.isCompatibleWith("1.0")) {
            LOGGER.fatal(CORE, i18n.get("fmlloader.6", atPackage.getSpecificationVersion(), atPackage.getImplementationVersion(), atPackage.getImplementationVendor()));
            throw new IncompatibleEnvironmentException("Incompatible accesstransformer found "+atPackage.getSpecificationVersion());
        }

        eventBus = environment.findLaunchPlugin("eventbus").orElseThrow(()-> {
            LOGGER.fatal(CORE,i18n.get("fmlloader.7"));
            return new IncompatibleEnvironmentException("Missing EventBus, cannot run");
        });

        final Package eventBusPackage = eventBus.getClass().getPackage();
        LOGGER.debug(CORE, i18n.get("fmlloader.8", eventBusPackage.getImplementationVersion()));
        if (!eventBusPackage.isCompatibleWith("1.0")) {
            LOGGER.fatal(CORE, i18n.get("fmlloader.9", eventBusPackage.getSpecificationVersion(), eventBusPackage.getImplementationVersion(), eventBusPackage.getImplementationVendor()));
            throw new IncompatibleEnvironmentException("Incompatible eventbus found "+eventBusPackage.getSpecificationVersion());
        }

        runtimeDistCleaner = (RuntimeDistCleaner)environment.findLaunchPlugin("runtimedistcleaner").orElseThrow(()-> {
            LOGGER.fatal(CORE, i18n.get("fmlloader.10"));
            return new IncompatibleEnvironmentException("Missing DistCleaner, cannot run!");
        });
        LOGGER.debug(CORE, "Found Runtime Dist Cleaner");

        var coreModProviders = ServiceLoaderUtils.streamWithErrorHandling(ServiceLoader.load(FMLLoader.class.getModule().getLayer(), ICoreModProvider.class), sce -> LOGGER.fatal(CORE, "Failed to load a coremod library, expect problems", sce)).toList();

        if (coreModProviders.isEmpty()) {
            LOGGER.fatal(CORE, i18n.get("fmlloader.11"));
            throw new IncompatibleEnvironmentException("No coremod library found");
        } else if (coreModProviders.size() > 1) {
            LOGGER.fatal(CORE, i18n.get("fmlloader.12", coreModProviders.stream().map(p -> p.getClass().getName()).collect(Collectors.toList())));
            throw new IncompatibleEnvironmentException("Multiple coremod libraries found");
        }

        coreModProvider = coreModProviders.get(0);
        final Package coremodPackage = coreModProvider.getClass().getPackage();
        LOGGER.debug(CORE, i18n.get("fmlloader.13", coremodPackage.getImplementationVersion()));


        LOGGER.debug(CORE, i18n.get("fmlloader.14", Environment.class.getPackage().getImplementationVersion()));
        LOGGER.debug(CORE, i18n.get("fmlloader.15", Environment.class.getPackage().getSpecificationVersion()));
        if (Integer.parseInt(Environment.class.getPackage().getSpecificationVersion()) < 2) {
            LOGGER.fatal(CORE, i18n.get("fmlloader.16", Environment.class.getPackage().getSpecificationVersion()));
            throw new IncompatibleEnvironmentException("ForgeSPI is out of date, we cannot continue");
        }

        try {
            Class.forName("com.electronwill.nightconfig.core.Config", false, environment.getClass().getClassLoader());
            Class.forName("com.electronwill.nightconfig.toml.TomlFormat", false, environment.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.fatal(CORE, i18n.get("fmlloader.17"));
            throw new IncompatibleEnvironmentException("Missing NightConfig");
        }
    }

    static void setupLaunchHandler(final IEnvironment environment, final Map<String, Object> arguments)
    {
        final String launchTarget = environment.getProperty(IEnvironment.Keys.LAUNCHTARGET.get()).orElse("MISSING");
        arguments.put("launchTarget", launchTarget);
        final Optional<ILaunchHandlerService> launchHandler = environment.findLaunchHandler(launchTarget);
        LOGGER.debug(CORE, i18n.get("fmlloader.18", launchTarget));
        if (launchHandler.isEmpty()) {
            LOGGER.fatal(CORE, i18n.get("fmlloader.19", launchTarget));
            throw new RuntimeException("Missing launch handler: " + launchTarget);
        }

        if (!(launchHandler.get() instanceof CommonLaunchHandler)) {
            LOGGER.fatal(CORE, i18n.get("fmlloader.20", launchHandler.get().getClass().getName()));
            throw new RuntimeException("Incompatible launch handler found");
        }
        commonLaunchHandler = (CommonLaunchHandler)launchHandler.get();
        launchHandlerName = launchHandler.get().name();
        gamePath = environment.getProperty(IEnvironment.Keys.GAMEDIR.get()).orElse(Paths.get(".").toAbsolutePath());

        naming = commonLaunchHandler.getNaming();
        dist = commonLaunchHandler.getDist();
        production = commonLaunchHandler.isProduction();

        versionInfo = new VersionInfo(arguments);

        StartupMessageManager.modLoaderConsumer().ifPresent(c->c.accept("Early Loading!"));
        accessTransformer.getExtension().accept(Pair.of(naming, "srg"));

        LOGGER.debug(CORE, i18n.get("fmlloader.21", versionInfo));

        runtimeDistCleaner.getExtension().accept(dist);
    }
    public static List<ITransformationService.Resource> beginModScan(final Map<String,?> arguments)
    {
        LOGGER.debug(SCAN, i18n.get("fmlloader.22"));
        modDiscoverer = new ModDiscoverer(arguments);
        modValidator = modDiscoverer.discoverMods();
        var pluginResources = modValidator.getPluginResources();
        return List.of(pluginResources);
    }

    public static List<ITransformationService.Resource> completeScan(IModuleLayerManager layerManager) {
        progressWindowTick = EarlyProgressVisualization.INSTANCE.accept(dist, commonLaunchHandler.isData(), versionInfo.mcVersion());
        moduleLayerManager = layerManager;
        languageLoadingProvider = new LanguageLoadingProvider();
        backgroundScanHandler = modValidator.stage2Validation();
        loadingModList = backgroundScanHandler.getLoadingModList();
        return List.of(modValidator.getModResources());
    }

    public static ICoreModProvider getCoreModProvider() {
        return coreModProvider;
    }

    public static LanguageLoadingProvider getLanguageLoadingProvider()
    {
        return languageLoadingProvider;
    }

    static ModDiscoverer getModDiscoverer() {
        return modDiscoverer;
    }

    public static CommonLaunchHandler getLaunchHandler() {
        return commonLaunchHandler;
    }

    public static void addAccessTransformer(Path atPath, ModFile modName)
    {
        LOGGER.debug(SCAN, i18n.get("fmlloader.23", modName.getFilePath()));
        accessTransformer.offerResource(atPath, modName.getFileName());
    }

    public static Dist getDist()
    {
        return dist;
    }

    public static void beforeStart(ClassLoader launchClassLoader)
    {
        StartupMessageManager.modLoaderConsumer().ifPresent(c->c.accept("Launching minecraft"));
        progressWindowTick.run();
    }

    public static LoadingModList getLoadingModList()
    {
        return loadingModList;
    }

    public static Path getGamePath()
    {
        return gamePath;
    }

    public static String getNaming() {
        return naming;
    }

    public static Optional<BiFunction<INameMappingService.Domain, String, String>> getNameFunction(final String naming) {
        return Launcher.INSTANCE.environment().findNameMapping(naming);
    }

    public static String getLauncherInfo() {
        return Launcher.INSTANCE.environment().getProperty(IEnvironment.Keys.MLIMPL_VERSION.get()).orElse("MISSING");
    }

    public static List<Map<String, String>> modLauncherModList() {
        return Launcher.INSTANCE.environment().getProperty(IEnvironment.Keys.MODLIST.get()).orElseGet(Collections::emptyList);
    }

    public static String launcherHandlerName() {
        return launchHandlerName;
    }

    public static boolean isProduction() {
        return production;
    }

    public static boolean isSecureJarEnabled() {
        return true;
    }

    public static ModuleLayer getGameLayer() {
        return moduleLayerManager.getLayer(IModuleLayerManager.Layer.GAME).orElseThrow();
    }

    public static VersionInfo versionInfo() {
        return versionInfo;
    }
}
