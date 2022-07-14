/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2022.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.util;

import java.io.File;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.lang.module.ResolvedModule;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Shawiiz_z
 * @version 0.1
 * @date 04/05/2022 22:57
 */
public class MohistModuleManager {

    private static final MethodHandles.Lookup IMPL_LOOKUP = Unsafe.lookup();
    private static String MODULE_PATH = null;

    public MohistModuleManager(List<String> args) {
        this.applyLaunchArgs(args);
    }

    public static void addToPath(Path path) {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Field ucpField;
            try {
                ucpField = loader.getClass().getDeclaredField("ucp");
            } catch (NoSuchFieldException e) {
                ucpField = loader.getClass().getSuperclass().getDeclaredField("ucp");
            }
            long offset = Unsafe.objectFieldOffset(ucpField);
            Object ucp = Unsafe.getObject(loader, offset);
            if (ucp == null) {
                var cl = Class.forName("jdk.internal.loader.URLClassPath");
                var handle = Unsafe.lookup().findConstructor(cl, MethodType.methodType(void.class, URL[].class, AccessControlContext.class));
                ucp = handle.invoke(new URL[]{}, (AccessControlContext) null);
                Unsafe.putObjectVolatile(loader, offset, ucp);
            }
            Method method = ucp.getClass().getDeclaredMethod("addURL", URL.class);
            Unsafe.lookup().unreflect(method).invoke(ucp, path.toUri().toURL());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void applyLaunchArgs(List<String> args) {
        //Just read each lines of launch args
        List<String> opens = new ArrayList<>();
        List<String> exports = new ArrayList<>();
        opens.add("java.base/java.lang.invoke=ALL-UNNAMED");
        exports.add("cpw.mods.securejarhandler/cpw.mods.niofs.union=ALL-UNNAMED");
        exports.add("cpw.mods.securejarhandler/cpw.mods.jarhandling=ALL-UNNAMED");

        args.parallelStream().parallel().forEach(arg -> {
            if (arg.startsWith("-p ")) {
                MODULE_PATH = arg.substring(2).trim();
            } else if (arg.startsWith("--add-opens")) {
                opens.add(arg.substring("--add-opens ".length()).trim());
            } else if (arg.startsWith("--add-exports")) {
                exports.add(arg.substring("--add-exports ".length()).trim());
            } else if (arg.startsWith("-D")) {
                String[] params = arg.substring(2).split("=", 2);
                System.setProperty(params[0], params[1]);
            }
        });

        try {
            loadModules(MODULE_PATH);
            Thread.sleep(500);
            addOpens(opens);
            addExports(exports);
            Thread.sleep(500);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    public static void addExports(String module, String pkg, String target){
        if(target == null) target = "ALL-UNNAMED";

        try {
            addExports(List.of(module + "/" + pkg + "=" + target));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void addExports(List<String> exports) throws Throwable {
        MethodHandle implAddExportsMH = IMPL_LOOKUP.findVirtual(Module.class, "implAddExports", MethodType.methodType(void.class, String.class, Module.class));
        MethodHandle implAddExportsToAllUnnamedMH = IMPL_LOOKUP.findVirtual(Module.class, "implAddExportsToAllUnnamed", MethodType.methodType(void.class, String.class));

        addExtra(exports, implAddExportsMH, implAddExportsToAllUnnamedMH);
    }

    public static void addOpens(String module, String pkg, String target){
        if(target == null) target = "ALL-UNNAMED";

        try {
            addOpens(List.of(module + "/" + pkg + "=" + target));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void addOpens(List<String> opens) throws Throwable {
        MethodHandle implAddOpensMH = IMPL_LOOKUP.findVirtual(Module.class, "implAddOpens", MethodType.methodType(void.class, String.class, Module.class));
        MethodHandle implAddOpensToAllUnnamedMH = IMPL_LOOKUP.findVirtual(Module.class, "implAddOpensToAllUnnamed", MethodType.methodType(void.class, String.class));

        addExtra(opens, implAddOpensMH, implAddOpensToAllUnnamedMH);
    }

    private static ParserData parseModuleExtra(String extra) {
        String[] all = extra.split("=", 2);
        if(all.length < 2) {
            return null;
        }

        String[] source = all[0].split("/", 2);
        if(source.length < 2) {
            return null;
        }
        return new ParserData(source[0], source[1], all[1]);
    }

    private record ParserData(String module, String packages, String target) {

        @Override
        public boolean equals(Object obj) {
            if(obj == this) return true;
            if(obj == null || obj.getClass() != this.getClass()) return false;
            var that = (ParserData) obj;
            return Objects.equals(this.module, that.module) &&
                    Objects.equals(this.packages, that.packages) &&
                    Objects.equals(this.target, that.target);
        }

        @Override
        public String toString() {
            return "ParserData[" +
                    "module=" + module + ", " +
                    "packages=" + packages + ", " +
                    "target=" + target + ']';
        }


    }


    private static void addExtra(List<String> extras, MethodHandle implAddExtraMH, MethodHandle implAddExtraToAllUnnamedMH) {
        extras.forEach(extra -> {
            ParserData data = parseModuleExtra(extra);
            if(data != null) {
                ModuleLayer.boot().findModule(data.module).ifPresent(m -> {
                    try {
                        if("ALL-UNNAMED".equals(data.target)) {
                            implAddExtraToAllUnnamedMH.invokeWithArguments(m, data.packages);
                            // System.out.println("Added extra to all unnamed modules: " + data);
                        } else {
                            ModuleLayer.boot().findModule(data.target).ifPresent(tm -> {
                                try {
                                    implAddExtraMH.invokeWithArguments(m, data.packages, tm);
                                    // System.out.println("Added extra: " + data);
                                } catch (Throwable t) {
                                    throw new RuntimeException(t);
                                }
                            });
                        }
                    } catch (Throwable t) {
                        throw new RuntimeException(t);
                    }
                });
            }
        });
    }

    //Codesnipped from (https://github.com/IzzelAliz/Arclight/blob/f98046185ebfc183a242ac5497619dc35d741042/forge-installer/src/main/java/io/izzel/arclight/forgeinstaller/ForgeInstaller.java#L420)
    public void loadModules(String modulePath) throws Throwable {
        // Find all extra modules
        ModuleFinder finder = ModuleFinder.of(Arrays.stream(modulePath.split(OSUtil.getOS() == OSUtil.OS.WINDOWS ? ";" : ":")).map(Paths::get).peek(MohistModuleManager::addToPath).toArray(Path[]::new));
        MethodHandle loadModuleMH = IMPL_LOOKUP.findVirtual(Class.forName("jdk.internal.loader.BuiltinClassLoader"), "loadModule", MethodType.methodType(void.class, ModuleReference.class));

        // Resolve modules to a new config
        Configuration config = Configuration.resolveAndBind(finder, List.of(ModuleLayer.boot().configuration()), finder, finder.findAll().stream().peek(mref -> {
            try {
                // Load all extra modules in system class loader (unnamed modules for now)
                loadModuleMH.invokeWithArguments(Thread.currentThread().getContextClassLoader(), mref);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }).map(ModuleReference::descriptor).map(ModuleDescriptor::name).collect(Collectors.toList()));

        // Copy the new config graph to boot module layer config
        MethodHandle graphGetter = IMPL_LOOKUP.findGetter(Configuration.class, "graph", Map.class);
        HashMap<ResolvedModule, Set<ResolvedModule>> graphMap = new HashMap<>((Map<ResolvedModule, Set<ResolvedModule>>) graphGetter.invokeWithArguments(config));
        MethodHandle cfSetter = IMPL_LOOKUP.findSetter(ResolvedModule.class, "cf", Configuration.class);
        // Reset all extra resolved modules config to boot module layer config
        graphMap.forEach((k, v) -> {
            try {
                cfSetter.invokeWithArguments(k, ModuleLayer.boot().configuration());
                v.forEach(m -> {
                    try {
                        cfSetter.invokeWithArguments(m, ModuleLayer.boot().configuration());
                    } catch (Throwable throwable) {
                        throw new RuntimeException(throwable);
                    }
                });
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        });
        graphMap.putAll((Map<ResolvedModule, Set<ResolvedModule>>) graphGetter.invokeWithArguments(ModuleLayer.boot().configuration()));
        IMPL_LOOKUP.findSetter(Configuration.class, "graph", Map.class).invokeWithArguments(ModuleLayer.boot().configuration(), new HashMap<>(graphMap));

        // Reset boot module layer resolved modules as new config resolved modules to prepare define modules
        Set<ResolvedModule> oldBootModules = ModuleLayer.boot().configuration().modules();
        MethodHandle modulesSetter = IMPL_LOOKUP.findSetter(Configuration.class, "modules", Set.class);
        HashSet<ResolvedModule> modulesSet = new HashSet<>(config.modules());
        modulesSetter.invokeWithArguments(ModuleLayer.boot().configuration(), new HashSet<>(modulesSet));

        // Prepare to add all of the new config "nameToModule" to boot module layer config
        MethodHandle nameToModuleGetter = IMPL_LOOKUP.findGetter(Configuration.class, "nameToModule", Map.class);
        HashMap<String, ResolvedModule> nameToModuleMap = new HashMap<>((Map<String, ResolvedModule>) nameToModuleGetter.invokeWithArguments(ModuleLayer.boot().configuration()));
        nameToModuleMap.putAll((Map<String, ResolvedModule>) nameToModuleGetter.invokeWithArguments(config));
        IMPL_LOOKUP.findSetter(Configuration.class, "nameToModule", Map.class).invokeWithArguments(ModuleLayer.boot().configuration(), new HashMap<>(nameToModuleMap));

        // Define all extra modules and add all of the new config "nameToModule" to boot module layer config
        ((Map<String, Module>) IMPL_LOOKUP.findGetter(ModuleLayer.class, "nameToModule", Map.class).invokeWithArguments(ModuleLayer.boot())).putAll((Map<String, Module>) IMPL_LOOKUP.findStatic(Module.class, "defineModules", MethodType.methodType(Map.class, Configuration.class, Function.class, ModuleLayer.class)).invokeWithArguments(ModuleLayer.boot().configuration(), (Function<String, ClassLoader>) name -> Thread.currentThread().getContextClassLoader(), ModuleLayer.boot()));

        // Add all of resolved modules
        modulesSet.addAll(oldBootModules);
        modulesSetter.invokeWithArguments(ModuleLayer.boot().configuration(), new HashSet<>(modulesSet));

        // Reset cache of boot module layer
        IMPL_LOOKUP.findSetter(ModuleLayer.class, "modules", Set.class).invokeWithArguments(ModuleLayer.boot(), null);
        IMPL_LOOKUP.findSetter(ModuleLayer.class, "servicesCatalog", Class.forName("jdk.internal.module.ServicesCatalog")).invokeWithArguments(ModuleLayer.boot(), null);

        // Add reads from extra modules to jdk modules
        MethodHandle implAddReadsMH = IMPL_LOOKUP.findVirtual(Module.class, "implAddReads", MethodType.methodType(void.class, Module.class));
        config.modules().forEach(rm -> ModuleLayer.boot().findModule(rm.name()).ifPresent(m -> oldBootModules.forEach(brm -> ModuleLayer.boot().findModule(brm.name()).ifPresent(bm -> {
            try {
                implAddReadsMH.invokeWithArguments(m, bm);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }))));
    }
}

