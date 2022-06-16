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

package com.mohistmc.forge;

import com.mohistmc.configuration.MohistConfig;
import java.util.List;
import java.util.Objects;

public class MohistForgeUtils {
	public static boolean modsblacklist(List<String> modslist) {
		if (MohistConfig.instance.modsblacklistenable.getValue() && !MohistConfig.instance.modswhitelistenable.getValue()) {
			String[] strings = MohistConfig.instance.modsblacklist.getValue().split(",");
			for (String mods : modslist) {
				for (String modsblacklist : strings) {
					if (Objects.equals(mods, modsblacklist)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean modswhitelist(List<String> clientMods) {
		String[] strings = MohistConfig.instance.modswhitelist.getValue().split(",");
		if (!MohistConfig.instance.modsblacklistenable.getValue() && MohistConfig.instance.modswhitelistenable.getValue()) {
			if (MohistConfig.instance.modsnumber.getValue() > 0) {
				for (String mods : clientMods) {
					for (String modswhitelist : strings) {
						return Objects.equals(mods, modswhitelist) && clientMods.size() == MohistConfig.instance.modsnumber.getValue();
					}
				}
			} else {
				for (String mods : clientMods) {
					for (String modswhitelist : strings) {
						return Objects.equals(mods, modswhitelist);
					}
				}
			}
		}
		return true;
	}
}
