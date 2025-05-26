package com.mohistmc.libraries;

import com.mohistmc.MohistMC;
import com.mohistmc.configuration.MohistConfigUtil;
import com.mohistmc.network.download.DownloadSource;
import com.mohistmc.network.download.UpdateUtils;
import com.mohistmc.util.JarLoader;
import com.mohistmc.util.JarTool;
import com.mohistmc.util.MD5Util;
import com.mohistmc.util.i18n.Message;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static com.mohistmc.configuration.MohistConfigUtil.bMohist;

public class DefaultLibraries {
    public static HashMap<String, String> fail = new HashMap<>();

    public static void run() throws Exception {
        System.out.println(Message.getString("libraries.checking.start"));
        LinkedHashMap<File, String> libs = getDefaultLibs();
        AtomicLong currentSize = new AtomicLong();
        Set<File> defaultLibs = new LinkedHashSet<>();
        for (File lib : getDefaultLibs().keySet()) {
            if (lib.exists() && MohistConfigUtil.getString(MohistConfigUtil.mohistyml, "libraries_black_list:", "xxxxx").contains(lib.getName())) {
                continue;
            }
            if (lib.getName().contains("launchwrapper")) {
                File customLaunchwrapper = new File(JarTool.getJarDir() + "/libraries/customize_libraries/launchwrapper-fccb-1.12.jar");
                if (bMohist("forge_can_call_bukkit")) {
                    if (lib.exists()) lib.delete();
                    if (!customLaunchwrapper.exists() || !MD5Util.md5CheckSum(customLaunchwrapper, "8f121345f96b77620fcfa69a4330947a"))
                        defaultLibs.add(customLaunchwrapper);
                    continue;
                } else if (customLaunchwrapper.exists()) customLaunchwrapper.delete();
            }
            if (lib.exists() && MD5Util.md5CheckSum(lib, libs.get(lib))) {
                currentSize.addAndGet(lib.length());
                continue;
            }
            defaultLibs.add(lib);
        }

        for (File lib : defaultLibs) {
            lib.getParentFile().mkdirs();
            String url = "META-INF/" + lib.getPath().replaceAll("\\\\", "/");
            try {
                if (copyFileFromJar(lib, url)) {
                    fail.remove(lib);
                }
                currentSize.addAndGet(lib.length());
            } catch (Exception e) {
                lib.delete();
            }
        }
    }

    protected static boolean copyFileFromJar(File file, String pathInJar) {
        InputStream is = MohistMC.class.getClassLoader().getResourceAsStream(pathInJar);
        if (file.exists()) return true;
        file.getParentFile().mkdirs();
        if (is != null) {
            try {
                file.createNewFile();
                Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                return true;
            } catch (IOException ignored) {
            }
        } else {
            System.out.println("[Mohist] The file " + pathInJar + " doesn't exists in the Mohist jar !");
            return false;
        }

        return true;
    }

    public static LinkedHashMap<File, String> getDefaultLibs() throws Exception {
        LinkedHashMap<File, String> temp = new LinkedHashMap<>();
        BufferedReader b = new BufferedReader(new InputStreamReader(DefaultLibraries.class.getClassLoader().getResourceAsStream("mohist_libraries.txt")));
        String str;
        while ((str = b.readLine()) != null) {
            String[] s = str.split("\\|");
            temp.put(new File(s[0]), s[1]);
        }
        b.close();
        return temp;
    }

    public static void loadDefaultLibs() throws Exception {
        for (File lib : getDefaultLibs().keySet())
            if (lib.exists() && !MohistConfigUtil.getString(MohistConfigUtil.mohistyml, "libraries_black_list:", "xxxxx").contains(lib.getName()))
                JarLoader.loadjar(lib.getAbsolutePath());
    }
}
