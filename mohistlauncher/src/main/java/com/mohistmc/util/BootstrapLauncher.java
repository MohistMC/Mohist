/*
 * BootstrapLauncher - for launching Java programs with added modular fun!
 *
 *     Copyright (C) 2021 - cpw
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, version 3 of the License.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.util;

import cpw.mods.cl.JarModuleFinder;
import cpw.mods.cl.ModuleClassLoader;
import cpw.mods.jarhandling.SecureJar;

import java.io.File;
import java.io.IOException;
import java.lang.module.ModuleFinder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class BootstrapLauncher {

    @SuppressWarnings("unchecked")
    public static boolean startServer(String[] args) {
        var legacyClasspath = loadLegacyClassPath();
        System.setProperty("legacyClassPath", String.join(File.pathSeparator, legacyClasspath));

        var ignoreList = System.getProperty("ignoreList", "asm,securejarhandler");
        var ignores = ignoreList.split(",");

        var previousPackages = new HashSet<String>();
        var jars = new ArrayList<SecureJar>();
        var filenameMap = getMergeFilenameMap();
        var mergeMap = new HashMap<Integer, List<Path>>();

        outer:
        for (var legacy : legacyClasspath) {
            var path = Paths.get(legacy);
            var filename = path.getFileName().toString();

            for (var filter : ignores) {
                if(filename.startsWith(filter)) {
                    continue outer;
                }
            }

            if(filenameMap.containsKey(filename)) {
                mergeMap.computeIfAbsent(filenameMap.get(filename), k -> new ArrayList<>()).add(path);
                continue;
            }

            var jar = SecureJar.from(new PackageTracker(Set.copyOf(previousPackages), path), path);
            var packages = jar.getPackages();

            previousPackages.addAll(packages);
            jars.add(jar);
        }

        mergeMap.forEach((idx, paths) -> {
            var pathsArray = paths.toArray(Path[]::new);
            var jar = SecureJar.from(new PackageTracker(Set.copyOf(previousPackages), pathsArray), pathsArray);
            var packages = jar.getPackages();

            previousPackages.addAll(packages);
            jars.add(jar);
        });
        var secureJarsArray = jars.toArray(SecureJar[]::new);

        var allTargets = Arrays.stream(secureJarsArray).map(SecureJar::name).toList();
        var jarModuleFinder = JarModuleFinder.of(secureJarsArray);
        var bootModuleConfiguration = ModuleLayer.boot().configuration();
        var bootstrapConfiguration = bootModuleConfiguration.resolveAndBind(jarModuleFinder, ModuleFinder.ofSystem(), allTargets);
        var moduleClassLoader = new ModuleClassLoader("MC-BOOTSTRAP", bootstrapConfiguration, List.of(ModuleLayer.boot()));
        var layer = ModuleLayer.defineModules(bootstrapConfiguration, List.of(ModuleLayer.boot()), m -> moduleClassLoader);
        Thread.currentThread().setContextClassLoader(moduleClassLoader);

        System.out.println(Thread.currentThread().getContextClassLoader());
        final var loader = ServiceLoader.load(layer.layer(), Consumer.class);
        ((Consumer<String[]>) loader.stream().findFirst().orElseThrow().get()).accept(args);
        return true;
    }

    private static Map<String, Integer> getMergeFilenameMap() {
        var mergeModules = System.getProperty("mergeModules");
        if(mergeModules == null) return Map.of();
        // `mergeModules` is a semicolon-separated set of comma-separated set of paths, where each (comma) set of paths is
        // combined into a single modules
        // example: filename1.jar,filename2.jar;filename2.jar,filename3.jar

        Map<String, Integer> filenameMap = new HashMap<>();
        int i = 0;
        for (var merge : mergeModules.split(";")) {
            var targets = merge.split(",");
            for (String target : targets) {
                filenameMap.put(target, i);
            }
            i++;
        }

        return filenameMap;
    }

    private record PackageTracker(Set<String> packages, Path... paths) implements BiPredicate<String, String> {
        @Override
        public boolean test(final String path, final String basePath) {
            // This method returns true if the given path is allowed within the JAR (filters out 'bad' paths)

            if(packages.isEmpty() || // This is the first jar, nothing is claimed yet, so allow everything
                    path.startsWith("META-INF/")) // Every module can have their own META-INF
                return true;

            int idx = path.lastIndexOf('/');
            return idx < 0 || // Resources at the root are allowed to co-exist
                    idx == path.length() - 1 || // All directories can have a potential to exist without conflict, we only care about real files.
                    !packages.contains(path.substring(0, idx).replace('/', '.')); // If the package hasn't been used by a previous JAR
        }
    }

    private static List<String> loadLegacyClassPath() {
        var legacyCpPath = System.getProperty("legacyClassPath.file");

        if(legacyCpPath != null) {
            var legacyCPFileCandidatePath = Paths.get(legacyCpPath);
            if(Files.exists(legacyCPFileCandidatePath) && Files.isRegularFile(legacyCPFileCandidatePath)) {
                try {
                    return Files.readAllLines(legacyCPFileCandidatePath);
                } catch (IOException e) {
                    throw new IllegalStateException("Failed to load the legacy class path from the specified file: " + legacyCpPath, e);
                }
            }
        }

        var legacyClasspath = System.getProperty("legacyClassPath", System.getProperty("java.class.path"));
        Objects.requireNonNull(legacyClasspath, "Missing legacyClassPath, cannot bootstrap");
        return Arrays.asList(legacyClasspath.split(File.pathSeparator));
    }
}
