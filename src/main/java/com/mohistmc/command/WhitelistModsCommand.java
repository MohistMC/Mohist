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

package com.mohistmc.command;

import com.mohistmc.configuration.MohistConfig;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class WhitelistModsCommand extends Command {

	private static String list = "";

	public WhitelistModsCommand(String name) {
		super(name);
		this.description = "Command to update, enable or disable the mods whitelist.";
		this.usageMessage = "/whitelistmods [enable|disable|update]";
		this.setPermission("mohist.command.whitelistmods");
	}

	private static String makeModList() {
		for (ModInfo mod : ModList.get().getMods())
			if (!mod.getModId().equals("mohist")) list += mod.getModId() + ",";
		return list.substring(0, list.length() - 1);
	}

	@Override
	public boolean execute(CommandSender sender, String currentAlias, String[] args) {
		if (!testPermission(sender)) return true;
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
			return false;
		}

		switch (args[0].toLowerCase()) {
			case "enable":
				MohistConfig.setValueMohist("forge.modswhitelist.list", makeModList());
				MohistConfig.setValueMohist("forge.modswhitelist.enable", true);
				break;
			case "disable":
				MohistConfig.setValueMohist("forge.modswhitelist.enable", false);
				break;
			case "update":
				MohistConfig.setValueMohist("forge.modswhitelist.list", makeModList());
				break;
		}

		return true;
	}
}
