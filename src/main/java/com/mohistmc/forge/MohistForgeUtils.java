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

import com.mohistmc.MohistConfig;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MohistForgeUtils {

	private static AtomicBoolean canLog = new AtomicBoolean(true);
	public static boolean modsblacklist(List<String> stringList) {
		if (MohistConfig.server_modlist_whitelist_enable) {
			if (!equalList(stringList, server_modlist_whitelist())) {
				canLog.set(false);
				return true;
			}
		}
		if (MohistConfig.player_modlist_blacklist_enable && MohistConfig.player_modlist_blacklist != null) {
			for (String config : MohistConfig.player_modlist_blacklist) {
				if (stringList.contains(config)) {
					canLog.set(false);
					return true;
				}
			}
		}
		return false;
	}

	public static List<String> server_modlist_whitelist() {
		String s = MohistConfig.server_modlist_whitelist.replaceAll("(?:\\[|null|\\]| +)", "");
		return Pattern.compile("\\s*,\\s*").splitAsStream(s).collect(Collectors.toList());
	}

	public static boolean canLog(boolean trunDef) {
		return trunDef ? canLog.getAndSet(true) : canLog.get();
	}

	public static boolean equalList(List list1, List list2) {
		if (list1.size() != list2.size()) return false;
		if (list2.containsAll(list1)) return true;
		return false;
	}
}
