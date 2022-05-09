/*
 * Mohist - MohistMC
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

package com.mohistmc.util;

import com.mohistmc.config.MohistConfigUtil;
import com.mohistmc.network.download.UpdateUtils;
import com.mohistmc.util.i18n.i18n;
import net.minecraftforge.server.ServerMain;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;

public class CustomFlagsHandler {

  public static ArrayList<String> launchArgs = new ArrayList<>(Arrays.asList("java", "-jar"));
  public static ArrayList<String> addedFlags = new ArrayList<>();
  public static ArrayList<String> bannedFlags = new ArrayList<>(Arrays.asList("xmx", "xms"));
  public static boolean hasCustomFlags = false;

  public static void handleCustomArgs() {
    if(ServerMain.mainArgs.contains("launchedWithCustomArgs")) return;

    getCustomFlags();
    if(!hasCustomFlags) return;

    launchArgs.addAll(addedFlags);
    launchArgs.add(new File(CustomFlagsHandler.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1)).getName());
    launchArgs.addAll(ServerMain.mainArgs);
    launchArgs.add("launchedWithCustomArgs");

    try {
      System.out.println(i18n.get("customflags.restartmessage", String.join(" ", launchArgs)));
      UpdateUtils.restartServer(launchArgs, true);
    } catch (Exception e) {
      System.out.println(i18n.get("customflags.restarterror"));
      e.printStackTrace();
    }
  }

  public static ArrayList<String> getCustomFlags() {
    String configFlags = MohistConfigUtil.sMohist("custom_flags", "aaaa bbbb");

    if(configFlags != null && !configFlags.equals("aaaa bbbb") && !configFlags.replaceAll(" ", "").equals("")) {
      if(configFlags.contains(" "))
        addedFlags.addAll(Arrays.asList(configFlags.split(" ")));
      else addedFlags.add(configFlags);
    }

    if(addedFlags.size() >= 1) hasCustomFlags = true;

    if(bannedFlags.stream().anyMatch(addedFlags.toString().toLowerCase()::contains)) {
      System.out.println(i18n.get("customflags.illegalflags", String.join(", ", bannedFlags)));
      System.exit(0);
    }

    for (String defaultArg : ManagementFactory.getRuntimeMXBean().getInputArguments())
      if(!addedFlags.contains(defaultArg))
        addedFlags.add(defaultArg);

    return addedFlags;
  }

}
