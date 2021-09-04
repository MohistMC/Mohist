package org.spigotmc;

import com.mohistmc.util.i18n.i18n;
import net.minecraft.server.MinecraftServer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TicksPerSecondCommand extends Command
{

    public TicksPerSecondCommand(String name)
    {
        super( name );
        this.description = "Gets the current ticks per second for the server";
        this.usageMessage = "/tps";
        this.setPermission( "bukkit.command.tps" );
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) {
            return true;
        }

        // Paper start - Further improve tick handling
        double[] tps = org.bukkit.Bukkit.getTPS();
        String[] tpsAvg = new String[tps.length];

        for (int i = 0; i < tps.length; i++) {
            tpsAvg[i] = TicksPerSecondCommand.format(tps[i]);
        }
        sender.sendMessage(ChatColor.GOLD + i18n.get("tickspersecondcommand.1") + " " + org.apache.commons.lang.StringUtils.join(tpsAvg, ", "));
        if (args.length > 0 && args[0].equals("mem") && sender.hasPermission("bukkit.command.tpsmemory")) {
            sender.sendMessage(ChatColor.GOLD + i18n.get("tickspersecondcommand.2") + " " + ChatColor.GREEN + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + "/" + (Runtime.getRuntime().totalMemory() / (1024 * 1024)) + " mb (Max: " + (Runtime.getRuntime().maxMemory() / (1024 * 1024)) + " mb)");
            if (!hasShownMemoryWarning) {
                sender.sendMessage(ChatColor.RED + "Warning: " + ChatColor.GOLD + " " + i18n.get("tickspersecondcommand.3"));
                hasShownMemoryWarning = true;
            }
        }
        // Paper end

        return true;
    }

    private boolean hasShownMemoryWarning; // Paper

    private static String format(double tps) {// Paper - Made static
        return ((tps > 18.0) ? ChatColor.GREEN : (tps > 16.0) ? ChatColor.YELLOW : ChatColor.RED).toString()
                + ((tps > 21.0) ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0); // Paper - only print * at 21, we commonly peak to 20.02 as the tick sleep is not accurate enough, stop the noise
    }
}
