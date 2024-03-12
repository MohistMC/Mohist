/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2024.
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
import com.mohistmc.tools.ZipUtil;
import com.mohistmc.util.I18n;
import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BackupWorldCommand extends Command {

    public BackupWorldCommand(String name) {
        super(name);
        this.description = I18n.as("worldbackupcmd.description");
        this.usageMessage = "/backupworld";
        this.setPermission("mohist.command.backupworld");
    }

    @Override
    public boolean execute(CommandSender sender, @NotNull String currentAlias, String[] args) {
        if (sender.isOp()) {
            if (args.length != 1) {
                sender.sendMessage(I18n.as("worldbackupcmd.notice.promptWorldName"));
                return true;
            }

            World world = Bukkit.getWorld(args[0]);
            if (!new File(args[0]).exists() || world == null) {
                sender.sendMessage(I18n.as("worldbackupcmd.notice.worldDontExists")  + Bukkit.getWorlds());
                return true;
            }
            world.save();
            new Thread(() -> {
                try {
                    sender.sendMessage(I18n.as("worldbackupcmd.notice.creatingWorldBackup"));
                    LocalDateTime now = LocalDateTime.now();
                    File zip = new File("./MohistBackups/" + args[0] + "-" + now.getDayOfMonth() + "-" + now.getMonthValue() + "-" + now.getYear() + "-" + now.getHour() + "-" + now.getMinute() + "-" + now.getSecond() + ".zip");
                    zip.getParentFile().mkdirs();
                    zip.createNewFile();

                    ZipUtil.zipFolder(Paths.get("./" + args[0]), zip.toPath());
                    sender.sendMessage(I18n.as("worldbackupcmd.notice.worldComplete"));
                } catch (Exception e) {
                    MohistMC.LOGGER.error("Failed to save world or this world doesn't exists.", e);
                }
            }).start();
        }
        return true;
    }
}