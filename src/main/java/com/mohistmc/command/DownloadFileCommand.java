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

import java.io.File;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


import static com.mohistmc.network.download.UpdateUtils.downloadFile;

public class DownloadFileCommand extends Command {

  public DownloadFileCommand(String name) {
    super(name);
    this.description = "Download a file from url directly in the choosed folder.";
    this.usageMessage = "/downloadfile";
    this.setPermission("mohist.command.downloadfile");
  }

  @Override
  public boolean execute(CommandSender sender, String currentAlias, String[] args) {
    if (!testPermission(sender)) return true;

    if (args.length > 0) {
      if (args.length == 1) {
        sender.sendMessage("[MOHIST] - You need to specify the path of the file.");
        return false;
      }
      if (args.length == 2) {
        sender.sendMessage("[MOHIST] - You need to specify the link of the file.");
        return false;
      }
      if (args.length == 3) {
        try {
          downloadFile(args[2], new File(args[1] + "/" + args[0]));
          return true;
        } catch (Exception e) {
          sender.sendMessage("[MOHIST] - Failed to download the file.");
          e.printStackTrace();
          return false;
        }
      }
    }
    return true;
  }
}