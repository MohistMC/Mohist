package com.mohistmc.config;

import com.mohistmc.MohistMCStart;
import net.minecraftforge.server.ServerMain;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

public class MohistConfigUtil {

	public static File mohistyml = new File("mohist-config/mohist.yml");
	private static HashMap<String, String> argsConfig = new HashMap<>();

	public static String getString(File f, String key, String defaultReturn) {
		String _key = key.replace(":", "");
		if(argsConfig.containsKey(_key)) return argsConfig.get(_key);
		try {
			for (String line : Files.readAllLines(f.toPath()))
				if(line.contains(key + ":")) {
					String[] spl = line.split(key + ":");
					if(spl[1].startsWith(" ")) spl[1] = spl[1].substring(1);
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

	public static void copyMohistConfig() {
		try {
			if(!mohistyml.exists()) {
				mohistyml.mkdirs();
				Files.copy(MohistMCStart.class.getClassLoader().getResourceAsStream("configs/mohist.yml"), mohistyml.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception e) {
			System.out.println("File copy exception!");
		}

		for (String arg : ServerMain.mainArgs)
			if(arg.contains("=")) {
				String[] spl = arg.split("=");
				argsConfig.put(spl[0], spl[1]);
			}
	}
}
