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

package com.mohistmc.network.download;

import com.mohistmc.config.MohistConfigUtil;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public enum DownloadSource {

    MOHIST("https://maven.mohistmc.com/"),
    GITHUB("https://mohistmc.github.io/maven/");

    final String url;

    DownloadSource(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public static DownloadSource get() {
        String ds = MohistConfigUtil.sMohist("mohist.libraries_downloadsource", MOHIST.name());
        for (DownloadSource me : DownloadSource.values()) {
            if (me.name().equalsIgnoreCase(ds)) {
                if (isDown(me.url.replace("releases/", "")) != 200) return GITHUB;
                return me;
            }
        }
        return MOHIST;
    }

    public static int isDown(String s) {
        try {
            URL url = new URL(s);
            URLConnection rulConnection = url.openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;
            httpUrlConnection.connect();
            return httpUrlConnection.getResponseCode();
        } catch (Exception e) {
            return 0;
        }
    }
}
