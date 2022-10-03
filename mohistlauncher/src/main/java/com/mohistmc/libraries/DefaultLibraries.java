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

package com.mohistmc.libraries;

import com.mohistmc.action.v_1_19.v_1_19;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultLibraries {
    public static HashMap<String, String> fail = new HashMap<>();
    public static String MAVENURL = DownloadSource.get().getUrl();

    public static String libUrl(File lib) {
        return MAVENURL + "libraries/" + lib.getAbsolutePath().replaceAll("\\\\", "/").split("/libraries/")[1];
    }

    public static void run() throws Exception {
        System.out.println(i18n.get("libraries.checking.start"));
        LinkedHashMap<File, String> libs = getDefaultLibs();
        AtomicLong currentSize = new AtomicLong();
        Set<File> defaultLibs = new LinkedHashSet<>();
        AtomicLong allSize = new AtomicLong(); // global
        for (File lib : getDefaultLibs().keySet()) {
            allSize.addAndGet(UpdateUtils.getAllSizeOfUrl(libUrl(lib)));
            v_1_19.loadedLibsPaths.add(lib.getAbsolutePath());
            if (lib.exists() && MohistConfigUtil.getString(MohistConfigUtil.mohistyml, "libraries_black_list:", "xxxxx").contains(lib.getName())) {
                continue;
            }
            if (lib.exists() && MD5Util.getMd5(lib).equals(libs.get(lib))) {
                currentSize.addAndGet(lib.length());
                continue;
            }
            defaultLibs.add(lib);
        }
        for (File lib : defaultLibs) {
            lib.getParentFile().mkdirs();

            String u = libUrl(lib);
            System.out.println(i18n.get("libraries.global.percentage") + Math.round(currentSize.get() * 100 / allSize.get()) + "%"); //Global percentage
            try {
                UpdateUtils.downloadFile(u, lib, libs.get(lib));
                JarLoader.loadJar(lib.toPath());
                currentSize.addAndGet(lib.length());
                fail.remove(u.replace(MAVENURL, ""));
            } catch (Exception e) {
                if (e.getMessage() != null && !e.getMessage().equals("md5")) {
                    System.out.println(i18n.get("file.download.nook", u));
                    lib.delete();
                }
                fail.put(u.replace(MAVENURL, ""), lib.getAbsolutePath());
            }
        }
        /*FINISHED | RECHECK IF A FILE FAILED*/
        if (!fail.isEmpty()) run();
        else System.out.println(i18n.get("libraries.check.end"));
    }

    public static LinkedHashMap<File, String> getDefaultLibs() throws Exception {
        LinkedHashMap<File, String> temp = new LinkedHashMap<>();
        BufferedReader b = new BufferedReader(new InputStreamReader(Objects.requireNonNull(DefaultLibraries.class.getClassLoader().getResourceAsStream("libraries.txt"))));
        String str;
        while ((str = b.readLine()) != null) {
            String[] s = str.split("\\|");
            temp.put(new File(JarTool.getJarDir() + "/" + s[0]), s[1]);
        }
        b.close();
        return temp;
    }
}
