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

package com.mohistmc;

import com.mohistmc.action.ZipTree;
import com.mohistmc.action.v_1_20_1;
import com.mohistmc.config.MohistConfigUtil;
import com.mohistmc.feature.AutoDeleteMods;
import com.mohistmc.feature.CustomLibraries;
import com.mohistmc.feature.DefaultLibraries;
import com.mohistmc.i18n.i18n;
import com.mohistmc.tools.JarTool;
import com.mohistmc.tools.Logo;
import com.mohistmc.util.DataParser;
import com.mohistmc.util.EulaUtil;
import com.mohistmc.util.MohistModuleManager;
import com.mohistmc.util.UpdateUtils;
import cpw.mods.bootstraplauncher.BootstrapLauncher;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class MohistMCStart {

    public static final List<String> mainArgs = new ArrayList<>();
    public static String MCVERSION;
    public static i18n i18n;
    public static JarTool jarTool;

    public static String getVersion() {
        return (MohistMCStart.class.getPackage().getImplementationVersion() != null) ? MohistMCStart.class.getPackage().getImplementationVersion() : "unknown";
    }

    public static void main(String[] args) throws Exception {
        mainArgs.addAll(List.of(args));
        jarTool = new JarTool(MohistMCStart.class);
        DataParser.parseVersions();
        DataParser.parseLaunchArgs();
        MohistConfigUtil.init();
        MohistConfigUtil.i18n();
        if (MohistConfigUtil.INSTALLATIONFINISHED() && MohistConfigUtil.aBoolean("mohist.show_logo", true)) {
            System.out.printf("%n%s%n%s - %s, Java(%s) %s PID: %s%n",
                    Logo.asMohist(),
                    i18n.as("mohist.launch.welcomemessage"),
                    getVersion(),
                    System.getProperty("java.class.version"),
                    System.getProperty("java.version"),
                    ManagementFactory.getRuntimeMXBean().getName().split("@")[0]
            );
            if (i18n.isCN()) {
                System.out.printf("官方交流QQ群: 158638%n");
            }
        }

        if (System.getProperty("log4j.configurationFile") == null) {
            System.setProperty("log4j.configurationFile", "log4j2_mohist.xml");
        }

        if (MohistConfigUtil.INSTALLATIONFINISHED() && MohistConfigUtil.CHECK_UPDATE()) {
            new UpdateUtils().init();
        }

        ZipTree.init();

        if (MohistConfigUtil.INSTALLATIONFINISHED() && MohistConfigUtil.CHECK_LIBRARIES()) {
            DefaultLibraries.run();
        }

        if (MohistConfigUtil.INSTALLATIONFINISHED()) {
            v_1_20_1.run();
        }

        AutoDeleteMods.jar();

        CustomLibraries.loadCustomLibs();
        List<String> forgeArgs = new ArrayList<>();
        for (String arg : DataParser.launchArgs.stream().filter(s ->
                        s.startsWith("--launchTarget")
                                || s.startsWith("--fml.forgeVersion")
                                || s.startsWith("--fml.mcVersion")
                                || s.startsWith("--fml.forgeGroup")
                                || s.startsWith("--fml.mcpVersion"))
                .toList()) {
            forgeArgs.add(arg.split(" ")[0]);
            forgeArgs.add(arg.split(" ")[1]);
        }
        new MohistModuleManager(DataParser.launchArgs);

        if (!EulaUtil.hasAcceptedEULA()) {
            System.out.println(i18n.as("eula"));
            while (!"true".equals(new Scanner(System.in).nextLine().trim())) {
                System.out.println(i18n.as("eula_notrue"));
            }
            EulaUtil.writeInfos();
        }
        String[] args_ = Stream.concat(forgeArgs.stream(), mainArgs.stream()).toArray(String[]::new);
        BootstrapLauncher.main(args_);
    }
}
