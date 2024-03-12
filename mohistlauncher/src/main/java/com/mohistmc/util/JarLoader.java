/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2024.
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

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Path;
import java.util.jar.JarFile;

public class JarLoader {

    private static Instrumentation inst = null;

    public JarLoader() {
    }

    // The JRE will call method before launching your main()
    public static void agentmain(final String a, final Instrumentation inst) {
        JarLoader.inst = inst;
    }

    // Don't forget to specify -javaagent:<mohist jar> on Java 9+,
    // if you load the main Mohist jar from -cp rather than direct-jar
    public static void premain(String agentArgs, Instrumentation inst) {
        JarLoader.inst = inst;
    }

    public static void loadJar(Path path) {
        if (!path.toFile().getName().endsWith(".jar")) {
            return;
        }
        try {
            inst.appendToSystemClassLoaderSearch(new JarFile(path.toFile()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}