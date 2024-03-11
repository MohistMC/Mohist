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
import com.mohistmc.tools.ConnectionUtil;
import com.mohistmc.util.I18n;

public class DefaultLibraries {

    public static void run() {
        String ds = System.getProperty("downloadsource") == null ? MohistConfigUtil.LIBRARIES_DOWNLOADSOURCE() : System.getProperty("downloadsource");
        String downloadSource = MohistConfigUtil.LIBRARIES_DOWNLOADSOURCE();
        if (ConnectionUtil.isValid(ds)) {
            downloadSource = ds;
        }

        LibrariesDownloadQueue queue = LibrariesDownloadQueue.create()
                .inputStream(DefaultLibraries.class.getClassLoader().getResourceAsStream("libraries.txt"))
                .downloadSource(downloadSource)
                .build();

        System.out.println(I18n.as("libraries.checking.start"));
        if (queue.needDownload()) {
            System.out.println(I18n.as("libraries.downloadsource", queue.downloadSource.name()));
            System.out.println(I18n.as("libraries.global.percentage"));
            queue.progressBar();
        }
        System.out.println(I18n.as("libraries.check.end"));
    }
}
