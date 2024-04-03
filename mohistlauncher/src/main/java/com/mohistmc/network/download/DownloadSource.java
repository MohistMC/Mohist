/*
 * MohistMC
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
    GITHUB("https://mohistmc.github.io/maven/");

    public static final DownloadSource defaultSource = isCN() ? CHINA : MOHIST;
    public final String url;

    public static DownloadSource get() {
        String ds = MohistConfigUtil.LIBRARIES_DOWNLOADSOURCE();
        DownloadSource urL;
        for (DownloadSource me : DownloadSource.values()) {
            if (me.name().equalsIgnoreCase(ds)) {
                if (!ConnectionUtil.canAccess(me.url)) {
                    if (ds.equals(CHINA.name())) {
                        urL = MOHIST;
                        if (!ConnectionUtil.canAccess(urL.url)) {
                            return GITHUB;
                        }
                    }
                    return GITHUB;
                }
                return me;
            }
        }
        return defaultSource;
    }

    public static boolean isCN() {
        return MohistMCStart.i18n.isCN() && ConnectionUtil.measureLatency(CHINA.getUrl()) < ConnectionUtil.measureLatency(MOHIST.getUrl());
    }
}
