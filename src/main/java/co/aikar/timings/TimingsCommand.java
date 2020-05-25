/*
 * This file is licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 Daniel Ennis <http://aikar.co>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package co.aikar.timings;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.util.StringUtil;
import red.mohist.util.i18n.Message;


public class TimingsCommand extends BukkitCommand {
    private static final List<String> TIMINGS_SUBCOMMANDS = ImmutableList.of("report", "reset", "on", "off", "paste", "verbon", "verboff");
    private long lastResetAttempt = 0;

    public TimingsCommand(String name) {
        super(name);
        this.description = Message.getString("timings.command.1");
        this.usageMessage = "/timings <reset|report|on|off|verbon|verboff>";
        this.setPermission("bukkit.command.timings");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) {
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return true;
        }
        final String arg = args[0];
        if ("on".equalsIgnoreCase(arg)) {
            Timings.setTimingsEnabled(true);
            sender.sendMessage(Message.getString("timings.command.2"));
            return true;
        } else if ("off".equalsIgnoreCase(arg)) {
            Timings.setTimingsEnabled(false);
            sender.sendMessage(Message.getString("timings.command.3"));
            return true;
        }

        if (!Timings.isTimingsEnabled()) {
            sender.sendMessage(Message.getString("timings.command.4"));
            return true;
        }

        long now = System.currentTimeMillis();
        if ("verbon".equalsIgnoreCase(arg)) {
            Timings.setVerboseTimingsEnabled(true);
            sender.sendMessage(Message.getString("timings.command.5"));
            return true;
        } else if ("verboff".equalsIgnoreCase(arg)) {
            Timings.setVerboseTimingsEnabled(false);
            sender.sendMessage(Message.getString("timings.command.6"));
            return true;
        } else if ("reset".equalsIgnoreCase(arg)) {
            if (now - lastResetAttempt < 30000) {
                TimingsManager.reset();
                sender.sendMessage(ChatColor.RED + Message.getString("timings.command.7"));
            } else {
                lastResetAttempt = now;
                sender.sendMessage(ChatColor.RED + Message.getString("timings.command.8"));
            }

        } else if ("cost".equals(arg)) {
            sender.sendMessage(Message.getString("timings.command.9") + ": " + TimingsExport.getCost());
        } else  if (
            "paste".equalsIgnoreCase(arg) ||
                "report".equalsIgnoreCase(arg) ||
                "get".equalsIgnoreCase(arg) ||
                "merged".equalsIgnoreCase(arg) ||
                "separate".equalsIgnoreCase(arg)
            ) {
            Timings.generateReport(sender);
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], TIMINGS_SUBCOMMANDS,
                new ArrayList<String>(TIMINGS_SUBCOMMANDS.size()));
        }
        return ImmutableList.of();
    }
}
