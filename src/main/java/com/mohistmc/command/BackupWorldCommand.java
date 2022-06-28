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

import com.mohistmc.util.ZipUtil;
import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BackupWorldCommand extends Command {

    public BackupWorldCommand(String name) {
        super(name);
        this.description = "Create a backup of your world.";
        this.usageMessage = "/backupworld";
        this.setPermission("mohist.command.backupworld");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (sender.isOp()) {
            if (args.length != 1) {
                sender.sendMessage("You need to specify the world name.");
                return true;
            }

            World world = Bukkit.getWorld(args[0]);
            if (!new File(args[0]).exists() || world == null) {
                sender.sendMessage("This world doesn't exists.");
                return true;
            }
            world.save();
            new Thread(() -> {
                try {
                    sender.sendMessage("Creating world backup, please wait...");
                    LocalDateTime now = LocalDateTime.now();
                    File zip = new File("./MohistBackups/" + args[0] + "-" + now.getDayOfMonth() + "-" + now.getMonthValue() + "-" + now.getYear() + "-" + now.getHour() + "-" + now.getMinute() + "-" + now.getSecond() + ".zip");
                    zip.getParentFile().mkdirs();
                    zip.createNewFile();

                    ZipUtil.zipFolder(Paths.get("./" + args[0]), zip.toPath());
                    sender.sendMessage("The world has been successfully saved!");
                } catch (Exception e) {
                    sender.sendMessage("Failed to save world or this world doesn't exists.");
                    e.printStackTrace();
                }
            }).start();
        }
        return true;
    }
}