package com.mohistmc.libraries;

import com.mohistmc.config.MohistConfigUtil;
import com.mohistmc.network.download.UpdateUtils;
import com.mohistmc.util.JarLoader;
import com.mohistmc.util.JarTool;
import com.mohistmc.util.MD5Util;
import com.mohistmc.util.i18n.i18n;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultLibraries {
	public static HashMap<String, String> fail = new HashMap<>();
	private static String mirror = "";

	public static void run() throws Exception {
		System.out.println(i18n.get("libraries.checking.start"));
		String url = mirror.equals("") ? "https://maven.mohistmc.com/" : mirror;
		LinkedHashMap<File, String> libs = getDefaultLibs();
		AtomicLong currentSize = new AtomicLong();
		Set<File> defaultLibs = new LinkedHashSet<>();
		for (File lib : getDefaultLibs().keySet()) {
			if(lib.exists() && MohistConfigUtil.getString(MohistConfigUtil.mohistyml, "libraries_black_list:", "xxxxx").contains(lib.getName())) {
				continue;
			}
			if(lib.exists() && MD5Util.getMd5(lib).equals(libs.get(lib))) {
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
				if(lib.getName().endsWith(".jar") && !lib.getName().contains("asm-tree-6.1.1.jar"))
					new JarLoader().loadJar(lib);
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
			if(!mirror.equals("")) {
				System.out.println("Looks like there is no maven available at the moment, or you don't have any network connection.\nPlease try again later or come into our Discord server (discord.gg/mohist) to get help.");
				System.out.println(i18n.get("libraries.check.missing"));
				for (String lib : fail.keySet())
					System.out.println("Link : " + lib + "\nPath : " + fail.get(lib) + "\n");
				System.exit(0);
			}
			System.out.println(i18n.get("libraries.check.retry", 0));
			System.out.println("Something went wrong during download, trying to download with the mirror...");
			mirror = "http://mavenmirror.mohistmc.com/";
			run();
		} else System.out.println(i18n.get("libraries.check.end"));
	}

	public static LinkedHashMap<File, String> getDefaultLibs() throws Exception {
		LinkedHashMap<File, String> temp = new LinkedHashMap<>();
		BufferedReader b = new BufferedReader(new InputStreamReader(DefaultLibraries.class.getClassLoader().getResourceAsStream("mohist_libraries.txt")));
		String str;
		while ((str = b.readLine()) != null) {
			String[] s = str.split("\\|");
			temp.put(new File(JarTool.getJarDir() + "/" + s[0]), s[1]);
		}
		b.close();
		return temp;
	}
}
