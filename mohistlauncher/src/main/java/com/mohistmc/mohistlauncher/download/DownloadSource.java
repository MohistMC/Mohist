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

package com.mohistmc.mohistlauncher.download;

import com.mohistmc.mohistlauncher.Main;
import com.mohistmc.mohistlauncher.config.MohistConfigUtil;
import com.mohistmc.tools.ConnectionUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum DownloadSource {

    MOHIST("https://maven.mohistmc.com/"),
    CHINA("http://s1.devicloud.cn:25119/"),
    GITHUB("https://mohistmc.github.io/maven/"),
    CUSTOM(null);

    public static final DownloadSource defaultSource = isCN() ? CHINA : MOHIST;
    public String url;

    public static DownloadSource get() {
        String ds = System.getProperty("libraries.downloadsource") == null ? MohistConfigUtil.LIBRARIES_DOWNLOADSOURCE() : System.getProperty("libraries.downloadsource");
        if (ds.startsWith("http")) {
            CUSTOM.url = ds;
            return CUSTOM;
        }
        DownloadSource urL;
        for (DownloadSource me : DownloadSource.values()) {
            if (me.name().equalsIgnoreCase(ds)) {
                urL = me;
                if (ConnectionUtil.isDown(me.url)) {
                    if (me.equals(CHINA)) {
                        urL = MOHIST;
                    }
                }
                if (ConnectionUtil.isDown(urL.url)) {
                    return GITHUB;
                }
                return urL;
            }
        }
        return defaultSource;
    }

    public static boolean isCN() {
        return Main.i18n.isCN() && ConnectionUtil.getUrlMillis(CHINA.getUrl()) < ConnectionUtil.getUrlMillis(MOHIST.getUrl());
    }
}
