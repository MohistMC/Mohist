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

package com.mohistmc.mohist.libraries;

import com.mohistmc.mohist.config.MohistConfigUtil;
import com.mohistmc.mohist.download.DownloadSource;
import com.mohistmc.mohist.download.UpdateUtils;
import com.mohistmc.mohist.util.I18n;
import com.mohistmc.tools.MD5Util;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.SneakyThrows;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;

public class DefaultLibraries {
    public static final Set<Libraries> fail = new HashSet<>();
    private static final Set<Libraries> librariesSet = new HashSet<>();
    public static List<URL> installer = new ArrayList<>();
    public static final String MAVENURL = DownloadSource.get().getUrl();

    public static void run() {
        init();
        if (!MohistConfigUtil.CHECK_LIBRARIES()) return;
        System.out.println(I18n.as("libraries.checking.start"));
        Set<Libraries> need_download = new LinkedHashSet<>();
        for (Libraries libraries : librariesSet) {
            File lib = new File(libraries.path());
            if (lib.exists() && MohistConfigUtil.yml.getStringList("libraries_black_list").contains(lib.getName())) {
                continue;
            }
            if (lib.exists() && Objects.equals(MD5Util.get(lib), libraries.md5())) {
                continue;
            }
            need_download.add(libraries);
        }

        if (!need_download.isEmpty()) {
            System.out.println(I18n.as("libraries.downloadsource", DownloadSource.get().name()));
            System.out.println(I18n.as("libraries.global.percentage"));
            ProgressBarBuilder builder = new ProgressBarBuilder().setTaskName("")
                    .setStyle(ProgressBarStyle.ASCII)
                    .setUpdateIntervalMillis(100)
                    .setInitialMax(need_download.size());
            try (ProgressBar pb = builder.build()) {
                for (Libraries lib : need_download) {
                    File file = new File(lib.path());
                    file.getParentFile().mkdirs();

                    String u = MAVENURL + "/" + lib.path();
                    try {
                        pb.setExtraMessage(file.getName());
                        UpdateUtils.downloadFile(u, file, lib.md5(), false);
                        fail.remove(lib);
                    } catch (Exception e) {
                        if (e.getMessage() != null && !"md5".equals(e.getMessage())) {
                            pb.setExtraMessage(I18n.as("file.download.nook", u));
                            file.delete();
                        }
                        fail.add(lib);
                    }
                    pb.step();
                }
            }
        }
        if (!fail.isEmpty()) {
            run();
        } else {
            System.out.println(I18n.as("libraries.check.end"));
        }
    }


    public static void init() {
        try {
            BufferedReader b = new BufferedReader(new InputStreamReader(DefaultLibraries.class.getResourceAsStream("/libraries.txt"), StandardCharsets.UTF_8));
            for (String line = b.readLine(); line != null; line = b.readLine()) {
                Libraries libraries = Libraries.from(line);
                librariesSet.add(libraries);
                if (libraries.installer()) {
                    File file = new File(libraries.path());
                    URL url = file.toURI().toURL();
                    installer.add(url);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
