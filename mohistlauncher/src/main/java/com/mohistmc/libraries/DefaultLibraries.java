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

import com.mohistmc.config.MohistConfigUtil;
import com.mohistmc.download.DownloadSource;
import com.mohistmc.download.UpdateUtils;
import com.mohistmc.tools.MD5Util;
import com.mohistmc.util.I18n;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import lombok.SneakyThrows;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;

public class DefaultLibraries {
    public static final Set<Libraries> fail = new HashSet<>();
    public static final Set<Libraries> librariesSet = new HashSet<>();
    public static final String MAVENURL = DownloadSource.get().getUrl();

    public static void run() {
        System.out.println(I18n.as("libraries.checking.start"));
        init();
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
                        UpdateUtils.downloadFile(u, file, lib.md5(), false);
                        fail.remove(lib);
                    } catch (Exception e) {
                        if (e.getMessage() != null && !"md5".equals(e.getMessage())) {
                            System.out.println(I18n.as("file.download.nook", u));
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

    @SneakyThrows
    public static void init() {
        BufferedReader b = new BufferedReader(new InputStreamReader(DefaultLibraries.class.getResourceAsStream("/libraries.txt"), StandardCharsets.UTF_8));
        for (String line = b.readLine(); line != null; line = b.readLine()) {
            Libraries libraries = Libraries.from(line);
            librariesSet.add(libraries);
        }
    }
}
