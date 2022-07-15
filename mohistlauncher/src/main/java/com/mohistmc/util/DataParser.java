/*
 * MohistMC
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Shawiiz_z
 * @version 0.1
 * @date 09/05/2022 22:05
 */
public class DataParser {
	public static HashMap<String, String> versionMap = new HashMap<>();
	public static List<String> launchArgs = new ArrayList<>();
	public static List<File> librariesClassPath = new ArrayList<>();
	public static void parseVersions() {
		versionMap.put("forge", FileUtil.readFileFromJar("versions/forge.txt").get(0));
		versionMap.put("minecraft", FileUtil.readFileFromJar("versions/minecraft.txt").get(0));
		versionMap.put("mcp", FileUtil.readFileFromJar("versions/mcp.txt").get(0));
		versionMap.put("mohist", FileUtil.readFileFromJar("versions/mohist.txt").get(0));
	}

	public static void parseLaunchArgs() {
		launchArgs.addAll(FileUtil.readFileFromJar("data/"+(OSUtil.getOS().equals(OSUtil.OS.WINDOWS) ? "win" : "unix") + "_args.txt"));
	}

	public static void parseLibrariesClassPath() {
		for(String lib : FileUtil.readFileFromJar("data/libraries.txt").get(0).split(";")) {
			librariesClassPath.add(new File(lib));
		}
	}
}
