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

package com.mohistmc.libraries;

import com.mohistmc.config.MohistConfigUtil;
import com.mohistmc.network.download.DownloadSource;
import com.mohistmc.network.download.UpdateUtils;
import com.mohistmc.util.JarLoader;
import com.mohistmc.util.JarTool;
import com.mohistmc.util.MD5Util;
import com.mohistmc.util.i18n.i18n;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultLibraries {
	public static HashMap<String, String> fail = new HashMap<>();

	public static void run() throws Exception {
		System.out.println(i18n.get("libraries.checking.start"));
		String url = DownloadSource.get().getUrl();
		LinkedHashMap<File, String> libs = getDefaultLibs();
		AtomicLong currentSize = new AtomicLong();
		Set<File> defaultLibs = new LinkedHashSet<>();
		for (File lib : libs.keySet()) {
			if(lib.exists() && MohistConfigUtil.getString(MohistConfigUtil.mohistyml, "libraries_black_list:", "xxxxx").contains(lib.getName())) {
				continue;
			}
			if(lib.exists() && Objects.equals(MD5Util.getMd5(lib), libs.get(lib))) {
				currentSize.addAndGet(lib.length());
				continue;
			}
			defaultLibs.add(lib);
		}
		for (File lib : defaultLibs) {
			lib.getParentFile().mkdirs();

			String u = url + "libraries/" + lib.getAbsolutePath().replaceAll("\\\\", "/").split("/libraries/")[1];
			System.out.println(
					i18n.get("libraries.global.percentage") +
							Math.round(currentSize.get() * 100 / 62557711d) + "%"); //Global percentage
			try {
				UpdateUtils.downloadFile(u, lib, libs.get(lib));
				currentSize.addAndGet(lib.length());
				fail.remove(u.replace(url, ""));
			} catch (Exception e) {
				if(!e.getMessage().equals("md5")) {
					System.out.println(i18n.get("file.download.nook", u));
					lib.delete();
				}
				fail.put(u.replace(url, ""), lib.getAbsolutePath());
			}
		}
		/*FINISHED | RECHECK IF A FILE FAILED*/
		if(!fail.isEmpty()) {
			run();
		} else System.out.println(i18n.get("libraries.check.end"));
	}

	public static void downloadRepoLibs() throws Exception {
		HashMap<File, List<String>> dependencies = new HashMap<>();
		dependencies.put(new File("libraries/dev/vankka/dependencydownload-common/1.2.1/dependencydownload-common-1.2.1.jar"), new ArrayList<>(Arrays.asList("e6b19d4a5e3687432530a0aa9edf6fcc", "https://repo1.maven.org/maven2/dev/vankka/dependencydownload-common/1.2.1/dependencydownload-common-1.2.1.jar")));
		dependencies.put(new File("libraries/dev/vankka/dependencydownload-runtime/1.2.2-SNAPSHOT/dependencydownload-runtime-1.2.2-20220425.122523-9.jar"), new ArrayList<>(Arrays.asList("e8cee80f1719c02ef3076ff42bab0ad9", "https://s01.oss.sonatype.org/content/repositories/snapshots/dev/vankka/dependencydownload-runtime/1.2.2-SNAPSHOT/dependencydownload-runtime-1.2.2-20220425.122523-9.jar")));

		for (File lib : dependencies.keySet()) {
			if(lib.exists() && Objects.equals(MD5Util.getMd5(lib), dependencies.get(lib).get(0))) {
				System.out.println("Loading library " + lib.getName());
				new JarLoader().loadJar(lib.toPath());
				continue;
			}
			System.out.println("Downloading " + lib.getName() + "...");
			lib.getParentFile().mkdirs();
			try {
				UpdateUtils.downloadFile(dependencies.get(lib).get(1), lib, dependencies.get(lib).get(0));
				new JarLoader().loadJar(lib.toPath());
				System.out.println("Downloaded and loaded " + lib.getName() + ". md5: " + MD5Util.getMd5(lib));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static LinkedHashMap<File, String> getDefaultLibs() throws Exception {
		LinkedHashMap<File, String> temp = new LinkedHashMap<>();
		BufferedReader b = new BufferedReader(new InputStreamReader(Objects.requireNonNull(DefaultLibraries.class.getClassLoader().getResourceAsStream("data/mohist-libraries.txt"))));
		String str;
		while ((str = b.readLine()) != null) {
			String[] s = str.split("\\|");
			temp.put(new File(JarTool.getJarDir(), s[0]), s[1]);
		}
		b.close();
		return temp;
	}
}
