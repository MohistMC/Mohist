/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2024.
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

package com.mohistmc.feature;

import com.mohistmc.action.v_1_20_1;
import com.mohistmc.config.MohistConfigUtil;
import com.mohistmc.libraries.Libraries;
import com.mohistmc.libraries.LibrariesDownloadQueue;
import com.mohistmc.tools.ConnectionUtil;
import com.mohistmc.util.I18n;
import com.mohistmc.util.JarLoader;
import java.io.File;
import java.util.Objects;

public class DefaultLibraries {

    public static void run() {
        LibrariesDownloadQueue queue = LibrariesDownloadQueue.create()
                .inputStream(DefaultLibraries.class.getClassLoader().getResourceAsStream("libraries.txt"))
                .build();

        System.out.println(I18n.as("libraries.checking.start"));
        if (queue.needDownload()) {
            System.out.println(I18n.as("libraries.global.percentage"));
            queue.progressBar();
            for (Libraries libraries : queue.need_download) {
                if (!libraries.isInstaller() && libraries.getPath().endsWith(".jar")) {
                    File file = new File(queue.parentDirectory, libraries.getPath());
                    if (file.exists()) {
                        JarLoader.loadJar(file.toPath());
                    }
                }
            }
        }

        for (Libraries libraries : queue.allLibraries) {
            File file = new File(queue.parentDirectory, libraries.getPath());
            v_1_20_1.loadedLibsPaths.add(file.getPath());
        }
        System.out.println(I18n.as("libraries.check.end"));
    }
}
