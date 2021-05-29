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
