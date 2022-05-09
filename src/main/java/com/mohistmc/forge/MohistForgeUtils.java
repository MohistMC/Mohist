/*
 * MohistMC
 * Copyright (C) 2019-2022.
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

package com.mohistmc.forge;

import com.mohistmc.configuration.MohistConfig;
import java.util.Arrays;
import java.util.List;

public class MohistForgeUtils {
	public static boolean modsblacklist(List<String> modslist) {
		if (MohistConfig.instance.modsblacklistenable.getValue() && !MohistConfig.instance.modswhitelistenable.getValue())
			return modslist.containsAll(Arrays.asList(MohistConfig.instance.modsblacklist.getValue().split(",")));
		return false;
	}

	public static boolean modswhitelist(List<String> clientMods) {
		if (!MohistConfig.instance.modsblacklistenable.getValue() && MohistConfig.instance.modswhitelistenable.getValue()) {
			if (MohistConfig.instance.modsnumber.getValue() > 0)
				return Arrays.asList(MohistConfig.instance.modswhitelist.getValue().split(",")).containsAll(clientMods) && clientMods.size() == MohistConfig.instance.modsnumber.getValue();
			else
				return Arrays.asList(MohistConfig.instance.modswhitelist.getValue().split(",")).containsAll(clientMods);
		}
		return true;
	}
}
