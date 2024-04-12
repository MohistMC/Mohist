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

package com.mohistmc.util;

import com.mohistmc.MohistMCStart;
import com.mohistmc.config.MohistConfigUtil;
import com.mohistmc.mjson.Json;
import com.mohistmc.tools.ConnectionUtil;
import com.mohistmc.tools.NumberUtil;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class UpdateUtils {

    public static void versionCheck() {
        System.out.println(I18n.as("update.check"));
        System.out.println(I18n.as("update.stopcheck"));

        try {
            Json json = Json.read(new URL("https://mohistmc.com/api/v2/sources/jenkins/Mohist-%s/builds/latest".formatted(MohistMCStart.MCVERSION)));

            var jar_version = Integer.parseInt(DataParser.versionMap.get("mohist"));
            var build_number = json.asInteger("id");
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(json.asLong("createdAt")));
            String url = json.asString("url");

            if (jar_version >= build_number) {
                System.out.println(I18n.as("update.latest", jar_version, build_number));
            } else {
                System.out.println(I18n.as("update.detect", build_number, jar_version, time));
                if (MohistConfigUtil.CHECK_UPDATE_AUTO_DOWNLOAD()) {
                    File mohistjar = MohistMCStart.jarTool.getFile();
                    System.out.println(I18n.as("download.file", mohistjar.getName(), NumberUtil.getSize(ConnectionUtil.getConn(url).getContentLength())));
                    ConnectionUtil.downloadFile(url, mohistjar);
                    System.out.println(I18n.as("download.file.ok", mohistjar.getName()));
                    restartServer(Arrays.asList("java", "-jar", MohistMCStart.jarTool.getJarName()), true);
                }
            }
        } catch (Throwable e) {
            System.out.println(I18n.as("check.update.noci"));
        }
    }

    public static void restartServer(List<String> cmd, boolean shutdown) throws Exception {
        System.out.println(I18n.as("jarfile.restart"));
        if (cmd.stream().anyMatch(s -> s.contains("-Xms")))
            System.out.println(I18n.as("xmswarn"));
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.directory(MohistMCStart.jarTool.getJarDir());
        pb.inheritIO().start().waitFor();
        Thread.sleep(2000);
        if (shutdown) System.exit(0);
    }
}
