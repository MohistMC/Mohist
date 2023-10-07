/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2023.
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

import com.mohistmc.MohistMCStart;
import com.mohistmc.action.v_1_20_R2;
import com.mohistmc.config.MohistConfigUtil;
import com.mohistmc.network.download.DownloadSource;
import com.mohistmc.network.download.UpdateUtils;
import com.mohistmc.tools.MD5Util;
import com.mohistmc.util.I18n;
import com.mohistmc.util.JarLoader;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;

public class DefaultLibraries {
    public static final HashMap<String, String> fail = new HashMap<>();
    public static final AtomicLong allSize = new AtomicLong(); // global
    public static final String MAVENURL = DownloadSource.get().getUrl();

    public static String libUrl(File lib) {
        return MAVENURL + "libraries/" + lib.getAbsolutePath().replaceAll("\\\\", "/").split("/libraries/")[1];
    }

    public static void run() throws Exception {
        System.out.println(I18n.as("libraries.checking.start"));
        LinkedHashMap<File, String> libs = getDefaultLibs();
        AtomicLong currentSize = new AtomicLong();
        Set<File> defaultLibs = new LinkedHashSet<>();
        for (File lib : libs.keySet()) {
            v_1_20_R2.loadedLibsPaths.add(lib.getAbsolutePath());
            if (lib.exists() && MohistConfigUtil.yml.getStringList("libraries_black_list").contains(lib.getName())) {
                continue;
            }
            if (lib.exists() && Objects.equals(MD5Util.get(lib), libs.get(lib))) {
                currentSize.addAndGet(lib.length());
                continue;
            }
            defaultLibs.add(lib);
        }

        if (!defaultLibs.isEmpty()) {
            System.out.println(I18n.as("libraries.downloadsource", DownloadSource.get().name()));
            System.out.println(I18n.as("libraries.global.percentage"));
            ProgressBarBuilder builder = new ProgressBarBuilder().setTaskName("")
                    .setStyle(ProgressBarStyle.ASCII)
                    .setUpdateIntervalMillis(100)
                    .setInitialMax(defaultLibs.size());
            try (ProgressBar pb = builder.build()) {
                for (File lib : defaultLibs) {
                    lib.getParentFile().mkdirs();

                    String u = libUrl(lib);
                    String failKey = u.replace(MAVENURL, "");
                    try {
                        UpdateUtils.downloadFile(u, lib, libs.get(lib), false);
                        JarLoader.loadJar(lib.toPath());
                        currentSize.addAndGet(lib.length());
                        fail.remove(failKey);
                    } catch (Exception e) {
                        if (e.getMessage() != null && !"md5".equals(e.getMessage())) {
                            System.out.println(I18n.as("file.download.nook", u));
                            lib.delete();
                        }
                        fail.put(failKey, lib.getAbsolutePath());
                    }
                    pb.step();
                }
            }
        }
        /*FINISHED | RECHECK IF A FILE FAILED*/
        if (!fail.isEmpty()) {
            run();
        } else {
            System.out.println(I18n.as("libraries.check.end"));
        }
    }

    public static LinkedHashMap<File, String> getDefaultLibs() throws Exception {
        LinkedHashMap<File, String> temp = new LinkedHashMap<>();
        BufferedReader b = new BufferedReader(new InputStreamReader(DefaultLibraries.class.getClassLoader().getResourceAsStream("libraries.txt")));
        String str;
        while ((str = b.readLine()) != null) {
            String[] s = str.split("\\|");
            temp.put(new File(MohistMCStart.jarTool.getJarDir() + "/" + s[0]), s[1]);
            allSize.addAndGet(Long.parseLong(s[2]));
        }
        b.close();
        return temp;
    }
}
