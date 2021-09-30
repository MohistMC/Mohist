package com.mohistmc.libraries;

import com.mohistmc.configuration.MohistConfigUtil;
import static com.mohistmc.configuration.MohistConfigUtil.bMohist;

import com.mohistmc.network.download.DownloadSource;
import com.mohistmc.network.download.UpdateUtils;
import com.mohistmc.util.JarLoader;
import com.mohistmc.util.JarTool;
import com.mohistmc.util.MD5Util;
import com.mohistmc.util.i18n.Message;
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

    public static void run() throws Exception {
        System.out.println(Message.getString("libraries.checking.start"));
        String url = DownloadSource.get().getUrl();
        LinkedHashMap<File, String> libs = getDefaultLibs();
		AtomicLong currentSize = new AtomicLong();
		Set<File> defaultLibs = new LinkedHashSet<>();
		for (File lib : getDefaultLibs().keySet()) {
			if(lib.exists() && MohistConfigUtil.getString(MohistConfigUtil.mohistyml, "libraries_black_list:", "xxxxx").contains(lib.getName())) {
				continue;
			}
			if(lib.getName().contains("launchwrapper")) {
				File customLaunchwrapper = new File(JarTool.getJarDir() + "/libraries/customize_libraries/launchwrapper-fccb-1.12.jar");
				if(bMohist("forge_can_call_bukkit")) {
					if(lib.exists()) lib.delete();
					if(!customLaunchwrapper.exists() || !MD5Util.md5CheckSum(customLaunchwrapper, "8f121345f96b77620fcfa69a4330947a"))
						defaultLibs.add(customLaunchwrapper);
					continue;
				} else if(customLaunchwrapper.exists()) customLaunchwrapper.delete();
			}
			if(lib.exists() && MD5Util.md5CheckSum(lib, libs.get(lib))){
				currentSize.addAndGet(lib.length());
				continue;
			}
			defaultLibs.add(lib);
		}

        for (File lib : defaultLibs) {
                lib.getParentFile().mkdirs();
                String u = url + "libraries/" + lib.getAbsolutePath().replaceAll("\\\\", "/").split("/libraries/")[1];
				System.out.println(Message.getString("libraries.global.percentage") + Math.round(currentSize.get() * 100 / 70000000d) + "%"); //Global percentage

                try {
                    UpdateUtils.downloadFile(u, lib);
					currentSize.addAndGet(lib.length());
                    fail.remove(u.replace(url, ""));
                } catch (Exception e) {
                    System.out.println(Message.getFormatString("file.download.nook", new Object[]{u}));
                    lib.delete();
					fail.put(u.replace(url, ""), lib.getAbsolutePath());
                }
        }
        /*FINISHED | RECHECK IF A FILE FAILED*/
        if (!fail.isEmpty()) {
			run();
        } else System.out.println(Message.getString("libraries.checking.end"));
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

    public static void loadDefaultLibs() throws Exception {
        for (File lib : getDefaultLibs().keySet())
            if(lib.exists() && !MohistConfigUtil.getString(MohistConfigUtil.mohistyml, "libraries_black_list:", "xxxxx").contains(lib.getName()))
                JarLoader.loadjar(lib.getAbsolutePath());
    }
}
