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

package com.mohistmc.configuration;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;

public class MohistConfigUtil {

    public static File mohistyml = new File("mohist-config/mohist.yml");
    private static HashMap<String, String> argsConfig = new HashMap<>();

    public static String getString(File f, String key, String defaultReturn) {
        String _key = key.replace(":", "");
        if (argsConfig.containsKey(_key)) return argsConfig.get(_key);
        try {
            for (String line : Files.readAllLines(f.toPath()))
                if (line.contains(key + ":")) {
                    String[] spl = line.split(key + ":");
                    if (spl[1].startsWith(" ")) spl[1] = spl[1].substring(1);
                    return spl[1].replace("'", "").replace("\"", "");
                }
        } catch (Throwable ignored) {
        }
        return defaultReturn;
    }

    public static String sMohist(String key, String defaultReturn) {
        return getString(mohistyml, key, defaultReturn);
    }

    public static Boolean bMohist(String key, String defaultReturn) {
        return Boolean.parseBoolean(sMohist(key, defaultReturn));
    }
}
