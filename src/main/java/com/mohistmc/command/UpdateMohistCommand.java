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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


import static com.mohistmc.config.MohistConfigUtil.bMohist;

public class UpdateMohistCommand extends Command {

	public UpdateMohistCommand(String name) {
		super(name);
		this.description = "Update Mohist to the latest build.";
		this.usageMessage = "/updatemohist";
		this.setPermission("mohist.command.updatemohist");
	}

	@Override
	public boolean execute(CommandSender sender, String currentAlias, String[] args) {
		if(sender.isOp()) {
			boolean val = bMohist("check_update_auto_download", "false");
			MohistConfig.setValueMohist("mohist.check_update_auto_download", !val);
			if(!val) System.out.println("[Mohist] Auto update is now enabled. To update Mohist, you need to restart the server.");
			else System.out.println("[Mohist] Auto update is now disabled.");
		}
		return true;
	}
}