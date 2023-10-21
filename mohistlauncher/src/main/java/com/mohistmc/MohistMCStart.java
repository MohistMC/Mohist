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

package com.mohistmc;

import com.mohistmc.action.v_1_18_2;
import com.mohistmc.config.MohistConfigUtil;
import com.mohistmc.libraries.CustomLibraries;
import com.mohistmc.libraries.DefaultLibraries;
import com.mohistmc.util.BootstrapLauncher;
import com.mohistmc.util.DataParser;
import com.mohistmc.util.MohistModuleManager;
import com.mohistmc.util.i18n.i18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static com.mohistmc.util.EulaUtil.hasAcceptedEULA;
import static com.mohistmc.util.EulaUtil.writeInfos;

public class MohistMCStart {

    public static List<String> mainArgs = new ArrayList<>();
    public static float javaVersion = Float.parseFloat(System.getProperty("java.class.version"));

    public static String getVersion() {
        return (MohistMCStart.class.getPackage().getImplementationVersion() != null) ? MohistMCStart.class.getPackage().getImplementationVersion() : "unknown";
    }

    public static void main(String[] args) throws Exception {
        mainArgs.addAll(List.of(args));
        DataParser.parseVersions();
        DataParser.parseLaunchArgs();

        MohistConfigUtil.copyMohistConfig();

        if (!MohistConfigUtil.INSTALLATIONFINISHED() && MohistConfigUtil.aBoolean("mohist.show_logo", true)) {
            String test = """

                     ███╗   ███╗  ██████╗  ██╗  ██╗ ██╗ ███████╗ ████████╗
                     ████╗ ████║ ██╔═══██╗ ██║  ██║ ██║ ██╔════╝ ╚══██╔══╝
                     ██╔████╔██║ ██║   ██║ ███████║ ██║ ███████╗    ██║
                     ██║╚██╔╝██║ ██║   ██║ ██╔══██║ ██║ ╚════██║    ██║
                     ██║ ╚═╝ ██║ ╚██████╔╝ ██║  ██║ ██║ ███████║    ██║
                     ╚═╝     ╚═╝  ╚═════╝  ╚═╝  ╚═╝ ╚═╝ ╚══════╝    ╚═╝
                                        
                    
                    %s - %s, Java(%s) %s
                    """;
            System.out.println(test.formatted(i18n.get("mohist.launch.welcomemessage"), getVersion(), System.getProperty("java.version"), System.getProperty("java.class.version")));
        }

        if (System.getProperty("log4j.configurationFile") == null) {
            System.setProperty("log4j.configurationFile", "log4j2_mohist.xml");
        }

        CustomLibraries.loadCustomLibs();
        if (!MohistConfigUtil.INSTALLATIONFINISHED() && MohistConfigUtil.CHECK_LIBRARIES()) {
            DefaultLibraries.run();
            v_1_18_2.run();
        }

        List<String> forgeArgs = new ArrayList<>();
        for (String arg : DataParser.launchArgs.stream().filter(s -> s.startsWith("--launchTarget") || s.startsWith("--fml.forgeVersion") || s.startsWith("--fml.mcVersion") || s.startsWith("--fml.forgeGroup") || s.startsWith("--fml.mcpVersion")).toList()) {
            forgeArgs.add(arg.split(" ")[0]);
            forgeArgs.add(arg.split(" ")[1]);
        }
        new MohistModuleManager(DataParser.launchArgs);

        if (!hasAcceptedEULA()) {
            System.out.println(i18n.get("eula"));
            while (!"true".equals(new Scanner(System.in).next())) {
            }
            writeInfos();
        }

        String[] args_ = Stream.concat(forgeArgs.stream(), mainArgs.stream()).toArray(String[]::new);
        BootstrapLauncher.startServer(args_);
    }
}
