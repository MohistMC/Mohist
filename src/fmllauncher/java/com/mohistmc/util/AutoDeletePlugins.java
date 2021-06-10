package com.mohistmc.util;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Mgazul
 * @date 2020/3/22 23:15
 */
public class AutoDeletePlugins {

    public static ArrayList<PluginsModsDelete.Fix> LIST = new ArrayList<>(Arrays.asList(
    		new PluginsModsDelete.Fix("com.earth2me.essentials.Essentials",
			"https://github.com/KR33PY/Essentials/releases/download/essxmohist/EssentialsX-2.19.0-dev+99-fd961d5.jar",
			"2.19.0-dev+99-fd961d5",
			"https://github.com/MohistMC/Essentials",
					"This fix make modded blocks and items compatible"),

			new PluginsModsDelete.Fix("com.sk89q.worldedit.bukkit.WorldEditPlugin",
					"https://github.com/MohistMC/WorldEdit/releases/download/1.16.5-1.0/worldedit-bukkit-7.3.0-MOHIST-dist.jar",
					"\"7.3.0-MOHIST+cf3b83f\"",
					"https://github.com/MohistMC/WorldEdit/tree/1.16.5",
					"This fix make modded blocks compatible")));
}
