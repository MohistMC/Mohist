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

package com.mohistmc.mohist;

import com.mohistmc.i18n.i18n;
import com.mohistmc.mohist.action.v_1_20_R3;
import com.mohistmc.mohist.config.MohistConfigUtil;
import com.mohistmc.mohist.feature.AutoDeleteMods;
import com.mohistmc.mohist.libraries.DefaultLibraries;
import com.mohistmc.mohist.util.DataParser;
import com.mohistmc.mohist.util.EulaUtil;
import java.util.Scanner;

public class Main {
    static final boolean DEBUG = Boolean.getBoolean("mohist.debug");
    public static String MCVERSION;
    public static i18n i18n;

    public static String getVersion() {
        return (Main.class.getPackage().getImplementationVersion() != null) ? Main.class.getPackage().getImplementationVersion() : MCVERSION;
    }

    public static void main(String[] args) throws Exception {
        DataParser.parseVersions();
        MohistConfigUtil.copyMohistConfig();
        MohistConfigUtil.i18n();
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

        if (!EulaUtil.hasAcceptedEULA()) {
            System.out.println(i18n.as("eula"));
            while (!"true".equals(new Scanner(System.in).next())) {
            }
            EulaUtil.writeInfos();
        }
    }
}
