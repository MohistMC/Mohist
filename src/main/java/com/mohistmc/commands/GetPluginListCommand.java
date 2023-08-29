/*
 * Mohist - MohistMC
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

package com.mohistmc.commands;

import com.mohistmc.MohistMC;
import com.mohistmc.util.HasteUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

public class GetPluginListCommand extends Command {
    private static String sendToHaste = "";

    public GetPluginListCommand(String name) {
        super(name);
        this.description = MohistMC.i18n.get("getPluginList.description");
        this.usageMessage = "/getpluginlist";
        this.setPermission("mohist.command.getpluginlist");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) {
            return true;
        }
        for (Plugin p : Bukkit.getServer().getPluginManager().getPlugins()) {
            sendToHaste = sendToHaste + "\nName : " + p.getName() + "\nVersion : " + p.getDescription().getVersion() + "\n---------";
        }
        try {
            sender.sendMessage(MohistMC.i18n.get("getPluginList.pluginList") + HasteUtils.pasteMohist(sendToHaste));
        } catch (IOException e) {
            System.out.println("Unable to paste the list of your plugins.");
        }

        return true;
    }
}
