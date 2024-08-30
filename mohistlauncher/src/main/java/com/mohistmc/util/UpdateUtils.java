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

    public Json json;
    public String jar_version;
    public int build_number;
    public String time;
    public String url;

    public UpdateUtils() {
        jar_version = DataParser.versionMap.get("mohist");
    }

    public void init() {
        System.out.println(I18n.as("update.check"));
        System.out.println(I18n.as("update.stopcheck"));
        if (jar_version.equals("dev")) {
            return;
        }
        if (canUpdate()) {
            System.out.println(I18n.as("update.latest", jar_version, build_number));
        } else {
            System.out.println(I18n.as("update.detect", build_number, jar_version, time));
            if (MohistConfigUtil.CHECK_UPDATE_AUTO_DOWNLOAD()) {
                File mohistjar = MohistMCStart.jarTool.getFile();
                System.out.println(I18n.as("download.file", mohistjar.getName(), NumberUtil.getSize(ConnectionUtil.getConn(url).getContentLength())));
                if (ConnectionUtil.downloadFile(url, mohistjar)) {
                    System.out.println(I18n.as("download.file.ok", mohistjar.getName()));
                    try {
                        restartServer(Arrays.asList("java", "-jar", MohistMCStart.jarTool.getJarName()), true);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private boolean canUpdate() {
        try {
            build_number = Integer.parseInt(jar_version); // Add try Cache
            json = Json.read(new URL("https://ci.codemc.io/job/MohistMC/job/Mohist-%s/lastSuccessfulBuild/api/json".formatted(MohistMCStart.MCVERSION)));
            build_number = json.asInteger("number");
            time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(json.asLong("timestamp")));
            url = "https://ci.codemc.io/job/MohistMC/job/Mohist-%s/lastSuccessfulBuild/artifact/projects/mohist/build/libs/mohist-%s-%s-server.jar".formatted(MohistMCStart.MCVERSION, MohistMCStart.MCVERSION, build_number);
            return Integer.parseInt(jar_version) >= build_number;
        } catch (Throwable e) {
            return false;
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
