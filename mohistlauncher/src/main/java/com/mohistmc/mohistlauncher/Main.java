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

package com.mohistmc.mohistlauncher;

import com.mohistmc.i18n.i18n;
import com.mohistmc.mohistlauncher.action.v_1_20_R3;
import com.mohistmc.mohistlauncher.config.MohistConfigUtil;
import com.mohistmc.mohistlauncher.feature.DefaultLibraries;
import com.mohistmc.mohistlauncher.util.DataParser;
import com.mohistmc.tools.JarTool;
import com.mohistmc.tools.MojangEulaUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static final boolean DEBUG = Boolean.getBoolean("mohist.debug");
    public static String MCVERSION;
    public static i18n i18n;
    public static JarTool jarTool;

    public static String getVersion() {
        return (Main.class.getPackage().getImplementationVersion() != null) ? Main.class.getPackage().getImplementationVersion() : MCVERSION;
    }

    public static void main(String[] args) throws Exception {
        DataParser.parseVersions();
        MohistConfigUtil.init();
        MohistConfigUtil.i18n();
        jarTool = new JarTool(Main.class);
        if (MohistConfigUtil.aBoolean("mohist.show_logo", true)) {
            String test = """

                     ███╗   ███╗  ██████╗  ██╗  ██╗ ██╗ ███████╗ ████████╗
                     ████╗ ████║ ██╔═══██╗ ██║  ██║ ██║ ██╔════╝ ╚══██╔══╝
                     ██╔████╔██║ ██║   ██║ ███████║ ██║ ███████╗    ██║
                     ██║╚██╔╝██║ ██║   ██║ ██╔══██║ ██║ ╚════██║    ██║
                     ██║ ╚═╝ ██║ ╚██████╔╝ ██║  ██║ ██║ ███████║    ██║
                     ╚═╝     ╚═╝  ╚═════╝  ╚═╝  ╚═╝ ╚═╝ ╚══════╝    ╚═╝
                                        
                                        
                    %s - %s, Java(%s) %s
                    """;
            System.out.printf(test + "%n", i18n.as("mohist.launch.welcomemessage"), getVersion(), System.getProperty("java.version"), System.getProperty("java.class.version"));
        }

        // if (!MohistConfigUtil.INSTALLATIONFINISHED() && MohistConfigUtil.CHECK_UPDATE()) { UpdateUtils.versionCheck(); }

        DefaultLibraries.run();
        v_1_20_R3.run();

        if (!MojangEulaUtil.hasAcceptedEULA()) {
            System.out.println(i18n.as("eula"));
            while (!"true".equals(new Scanner(System.in).next())) {
            }
            MojangEulaUtil.writeInfos(i18n.as("eula.text", "https://account.mojang.com/documents/minecraft_eula") + "\n" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "\neula=true");
        }
    }
}
