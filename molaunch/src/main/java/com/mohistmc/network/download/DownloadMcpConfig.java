/*
 * MohistMC
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

import java.io.File;
import java.nio.file.Files;

public class DownloadMcpConfig {

    public static void run(String mc_version, String mcp_version) {
        File mcp_config = new File("libraries/de/oceanlabs/mcp/mcp_config/" + mc_version + "-" + mcp_version + "/mcp_config-" + mc_version + "-" + mcp_version + ".zip");
        if (Files.exists(mcp_config.toPath()))
            return;
        mcp_config.getParentFile().mkdirs();
        try {
            UpdateUtils.downloadFile("https://maven.minecraftforge.net/de/oceanlabs/mcp/mcp_config/" + mc_version + "-" + mcp_version + "/mcp_config-" + mc_version + "-" + mcp_version + ".zip",
                    mcp_config);
        } catch (Exception e) {
            System.out.println("Can't find mcp_config");
            e.printStackTrace();
        }
    }

}
