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

import com.mohistmc.tools.ConnectionUtil;
import com.mohistmc.tools.MD5Util;
import com.mohistmc.util.I18n;

import java.io.File;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.StandardOpenOption;
import java.util.Timer;
import java.util.TimerTask;

public class UpdateUtils {

    private static int percentage = 0;

    public static void downloadFile(String URL, File f) throws Exception {
        downloadFile(URL, f, null);
    }

    public static void downloadFile(String URL, File f, String md5) throws Exception {
        URLConnection conn = ConnectionUtil.getConn(URL);
        System.out.println(I18n.as("download.file", f.getName(), ConnectionUtil.getSize(conn.getContentLength())));
        ReadableByteChannel rbc = Channels.newChannel(conn.getInputStream());
        FileChannel fc = FileChannel.open(f.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        int fS = conn.getContentLength();
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (rbc.isOpen()) {
                    if (percentage != Math.round((float) f.length() / fS * 100) && percentage < 100)
                        System.out.println(I18n.as("file.download.percentage", f.getName(), percentage));
                    percentage = Math.round((float) f.length() / fS * 100);
                } else t.cancel();
            }
        }, 3000, 1000);
        fc.transferFrom(rbc, 0, Long.MAX_VALUE);
        fc.close();
        rbc.close();
        percentage = 0;
        String MD5 = MD5Util.get(f);
        if (f.getName().endsWith(".jar") && md5 != null && MD5 != null && !MD5.equals(md5.toLowerCase())) {
            f.delete();
            System.out.println(I18n.as("file.download.nook.md5", URL, MD5, md5.toLowerCase()));
            throw new Exception("md5");
        }
        System.out.println(I18n.as("download.file.ok", f.getName()));
    }
}
