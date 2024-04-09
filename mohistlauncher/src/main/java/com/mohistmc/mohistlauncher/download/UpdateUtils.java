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

import com.mohistmc.mjson.Json;
import com.mohistmc.mohistlauncher.util.DataParser;
import com.mohistmc.mohistlauncher.util.I18n;
import com.mohistmc.tools.ConnectionUtil;
import com.mohistmc.tools.MD5Util;
import com.mohistmc.tools.NumberUtil;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateUtils {

    public static void versionCheck() {
        System.out.println(I18n.as("update.check"));
        System.out.println(I18n.as("update.stopcheck"));

        try {
            Json json = Json.read(new URL("https://mohistmc.com/api/v2/sources/jenkins/Mohist-1.20.4/builds/latest"));

            var jar_version = Integer.parseInt(DataParser.versionMap.get("mohist"));
            var build_number = json.asInteger("id");
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(json.asLong("createdAt")));

            if (jar_version >= build_number) {
                System.out.println(I18n.as("update.latest", jar_version, build_number));
            } else {
                System.out.println(I18n.as("update.detect", build_number, jar_version, time));
            }
        } catch (Throwable e) {
            System.out.println(I18n.as("check.update.noci"));
        }
    }

    public static void downloadFile(String URL, File f) throws Exception {
        downloadFile(URL, f, null, true);
    }

    public static void downloadFile(String URL, File f, String md5, boolean showlog) throws Exception {
        URLConnection conn = ConnectionUtil.getConn(URL);
        if (showlog) System.out.println(I18n.as("download.file", f.getName(), NumberUtil.getSize(conn.getContentLength())));
        ReadableByteChannel rbc = Channels.newChannel(conn.getInputStream());
        FileChannel fc = FileChannel.open(f.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

        fc.transferFrom(rbc, 0, Long.MAX_VALUE);
        fc.close();
        rbc.close();
        String MD5 = MD5Util.get(f);
        if (f.getName().endsWith(".jar") && md5 != null && MD5 != null && !MD5.equals(md5.toLowerCase())) {
            f.delete();
            if (showlog) System.out.println(I18n.as("file.download.nook.md5", URL, MD5, md5.toLowerCase()));
            return;
        }
        if (showlog) System.out.println(I18n.as("download.file.ok", f.getName()));
    }
}
