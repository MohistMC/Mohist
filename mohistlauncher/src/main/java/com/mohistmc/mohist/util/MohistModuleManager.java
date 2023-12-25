/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2023.
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
package com.mohistmc.mohist.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Shawiiz_z
 * @version 0.1
 * @date 04/05/2022 22:57
 */
public class MohistModuleManager {

    private static final MethodHandles.Lookup IMPL_LOOKUP;
    private static String MODULE_PATH = null;

    static {
        try {
            Field theUnsafe = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            sun.misc.Unsafe unsafe = (sun.misc.Unsafe) theUnsafe.get(null);
            unsafe.ensureClassInitialized(MethodHandles.Lookup.class);
            Field field = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            Object base = unsafe.staticFieldBase(field);
            long offset = unsafe.staticFieldOffset(field);
            IMPL_LOOKUP = (MethodHandles.Lookup) unsafe.getObject(base, offset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void addOpens(String module, String pkg, String target) {
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

    private static void addExtra(List<String> extras, MethodHandle implAddExtraMH, MethodHandle implAddExtraToAllUnnamedMH) {
        extras.parallelStream().forEach(extra -> {
            ParserData data = parseModuleExtra(extra);
            if(data != null) {
                ModuleLayer.boot().findModule(data.module).ifPresent(m -> {
                    try {
                        if("ALL-UNNAMED".equals(data.target)) {
                            implAddExtraToAllUnnamedMH.invokeWithArguments(m, data.packages);
                            //System.out.println("Added extra to all unnamed modules: " + data);
                        } else {
                            ModuleLayer.boot().findModule(data.target).ifPresent(tm -> {
                                try {
                                    implAddExtraMH.invokeWithArguments(m, data.packages, tm);
                                    //System.out.println("Added extra: " + data);
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

    public static void loadExternalFileSystems(URLClassLoader loader) {
        try {
            addOpens("java.base", "java.nio.file.spi", "ALL-UNNAMED");
            List<String> knownSchemes = FileSystemProvider.installedProviders().stream().map(FileSystemProvider::getScheme).toList();
            ServiceLoader<FileSystemProvider> sl = ServiceLoader.load(FileSystemProvider.class, loader);
            List<FileSystemProvider> newProviders = sl.stream().map(ServiceLoader.Provider::get).filter(provider -> !knownSchemes.contains(provider.getScheme())).toList();

            final Field installedProviders = FileSystemProvider.class.getDeclaredField("installedProviders");
            installedProviders.setAccessible(true);
            List<FileSystemProvider> providers = new ArrayList<>((List<FileSystemProvider>) installedProviders.get(null));
            providers.addAll(newProviders);
            installedProviders.set(null, providers);
        } catch (Exception e) {
            throw new RuntimeException("Could not load new file systems", e);
        }
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
    }
}
