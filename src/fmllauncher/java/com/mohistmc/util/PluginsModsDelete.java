package com.mohistmc.util;

import com.mohistmc.util.i18n.i18n;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

import static com.mohistmc.network.download.UpdateUtils.downloadFile;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class PluginsModsDelete {
	public static final String NOT_COMPATIBLE = "not_compatible";
	public static final String FIX = "fix";
	public static final File PLUGIN = new File("plugins");
	public static final File MOD = new File("mods");

	public static void checkPlugins(ArrayList<Fix> fixes, File plugins) throws Exception {
		if(!plugins.exists() || fixes.size() == 0) return;

		for (File pom : plugins.listFiles((dir, name) -> name.endsWith(".jar"))) {
			ArrayList<String> entries = jarEntries(pom);
			if(entries == null) continue;
			String allLines = "";

			if(plugins.equals(PLUGIN)) {
				if(!entries.contains("plugin.yml")) {
					System.out.println("[WARN] Plugin with jar name " + pom.getName() + " doesn't contains any plugin.yml, the server may crash or the plugin will not load properly.");
					continue;
				}

				try (JarFile pluginJar = new JarFile(pom)) {
					try (BufferedReader reader = new BufferedReader(new InputStreamReader(pluginJar.getInputStream(pluginJar.getJarEntry("plugin.yml"))))) {
						String line;
						while ((line = reader.readLine()) != null)
							allLines += line + "\n";
					}
				}
			}

			for (Fix fix : fixes) {
				if(!entries.contains(fix.main.replaceAll("\\.", "/") + ".class")) continue;

				if(plugins.equals(PLUGIN)) {
					if(fix.type.equals(FIX)) {
						if(!allLines.contains("version: " + fix.version) && allLines.contains("main: " + fix.main)) {
							System.out.println(i18n.get("update.pluginversion", pom.getName(), fix.version, fix.repo, fix.aim));
							System.out.println(i18n.get("update.downloadpluginversion", pom.getName()));
							if(new Scanner(System.in).next().equals("yes"))
								downloadFile(fix.url, pom, fix.md5);
						}
					} else if(fix.type.equals(NOT_COMPATIBLE))
						if(allLines.contains("main: " + fix.main))
							delete("plugins", pom);

				} else delete("mods", pom);
			}
		}
	}

	private static void delete(String name, File f) throws Exception {
		System.out.println(i18n.get("update.deleting", f.getName(), name));
		File newPath = new File("delete/" + name + "/" + f.getName());
		newPath.getParentFile().mkdirs();
		Files.copy(f.toPath(), newPath.toPath(), REPLACE_EXISTING);
		f.delete();
	}

	private static ArrayList<String> jarEntries(File f) {
		try (JarFile jf = new JarFile(f)) {
			return jf.stream().sequential().map(ZipEntry::getName).collect(Collectors.toCollection(ArrayList<String>::new));
		} catch (Exception e) {
			System.out.println("[WARN] - The jar file " + f.getName() + " (at " + f.getAbsolutePath() + ") is maybe corrupted or empty.");
			return null;
		}
	}

	public static class Fix {

		public String main;
		public String url;
		public String version;
		public String repo;
		public String type;
		public String aim;
		public String md5;

		public Fix(String mainClass, String url, String version, String repo, String aim, String md5) {
			this.main = mainClass;
			this.url = url;
			this.version = version;
			this.repo = repo;
			this.type = FIX;
			this.aim = aim;
			this.md5 = md5;
		}

		public Fix(String mainClass) {
			this.main = mainClass;
			this.type = NOT_COMPATIBLE;
		}
	}
}
