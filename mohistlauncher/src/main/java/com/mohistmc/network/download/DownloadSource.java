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

package com.mohistmc.network.download;

import com.mohistmc.MohistMCStart;
import com.mohistmc.config.MohistConfigUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

@Getter
@ToString
@AllArgsConstructor
public enum DownloadSource {

    MOHIST("https://maven.mohistmc.com/"),
    CHINA("http://s1.devicloud.cn:25119/"),
    GITHUB("https://mavenmirror.mohistmc.com/");

    public static final DownloadSource defaultSource = isCN() ? CHINA : MOHIST;
    final
    String url;

    public static DownloadSource get() throws IOException {
        String ds = MohistConfigUtil.defaultSource();
        for (DownloadSource me : DownloadSource.values()) {
            if (me.name().equalsIgnoreCase(ds)) {
                if (isDown(me.url) != 200) return GITHUB;
                return me;
            }
        }
        return defaultSource;
    }

    public static boolean isCN() {
        return MohistMCStart.i18n.isCN();
    }

    public static int isDown(String s) throws IOException {
        URL url = new URL(s);
        URLConnection rulConnection = url.openConnection();
        HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;
        httpUrlConnection.connect();
        return httpUrlConnection.getResponseCode();
    }
}
