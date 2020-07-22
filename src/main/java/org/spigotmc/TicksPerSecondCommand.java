package org.spigotmc;

import net.minecraft.server.MinecraftServer;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import red.mohist.util.i18n.Message;

public class TicksPerSecondCommand extends Command {

    public TicksPerSecondCommand(String name) {
        super(name);
        this.description = "Gets the current ticks per second for the server";
        this.usageMessage = "/tps";
        this.setPermission("bukkit.command.tps");
    }

    private boolean hasShownMemoryWarning; // Paper

    public static String format(double tps)  // Paper - Made static
    {
        return ((tps > 18.0) ? ChatColor.GREEN : (tps > 16.0) ? ChatColor.YELLOW : ChatColor.RED).toString()
                + ( ( tps > 21.0 ) ? "*" : "" ) + Math.min( Math.round( tps * 100.0 ) / 100.0, 20.0 ); // Paper - only print * at 21, we commonly peak to 20.02 as the tick sleep is not accurate enough, stop the noise
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) {
            sender.sendMessage(Message.getString("command.nopermission"));
            return true;
        }
        World currentWorld = null;
        if (sender instanceof CraftPlayer) {
            currentWorld = ((CraftPlayer) sender).getWorld();
        }
        sender.sendMessage(ChatColor.DARK_RED + "---------------------------------------");
        final MinecraftServer server = MinecraftServer.getServerInst();
        ChatColor colourTPS;
        for (World world : server.server.getWorlds()) {
            if (world instanceof CraftWorld) {
                boolean current = currentWorld != null && currentWorld == world;
                net.minecraft.world.WorldServer mcWorld = ((CraftWorld) world).getHandle();
                String bukkitName = world.getName();
                int dimensionId = mcWorld.provider.getDimension();
                String name = mcWorld.provider.getDimensionType().getName();
                String displayName = name.equals(bukkitName) ? name : String.format("%s | %s", name, bukkitName);

                double worldTickTime = mean(server.worldTickTimes.get(dimensionId)) * 1.0E-6D;
                double worldTPS = Math.min(1000.0 / worldTickTime, 20);

                if (worldTPS >= 18.0) {
                    colourTPS = ChatColor.GREEN;
                } else if (worldTPS >= 15.0) {
                    colourTPS = ChatColor.YELLOW;
                } else {
                    colourTPS = ChatColor.RED;
                }

                sender.sendMessage(String.format("%s[%d] %s%s %s- %s%.2fms / %s%.2ftps", ChatColor.GOLD, dimensionId,
                        current ? ChatColor.GREEN : ChatColor.YELLOW, displayName, ChatColor.RESET,
                        ChatColor.DARK_RED, worldTickTime, colourTPS, worldTPS));
            }
        }

        double meanTickTime = mean(server.tickTimeArray) * 1.0E-6D;
        double meanTPS = Math.min(1000.0 / meanTickTime, 20);
        if (meanTPS >= 18.0) {
            colourTPS = ChatColor.GREEN;
        } else if (meanTPS >= 15.0) {
            colourTPS = ChatColor.YELLOW;
        } else {
            colourTPS = ChatColor.RED;
        }
        sender.sendMessage(String.format("%sOverall - %s%s%.2fms / %s%.2ftps", ChatColor.BLUE, ChatColor.RESET,
                ChatColor.DARK_RED, meanTickTime, colourTPS, meanTPS));
        sender.sendMessage(ChatColor.DARK_RED + "---------------------------------------");
        // Paper start - Further improve tick handling
        double[] tps = org.bukkit.Bukkit.getTPS();
        String[] tpsAvg = new String[tps.length];
        for (int i = 0; i < tps.length; i++) {
            tpsAvg[i] = format(tps[i]);
        }
        sender.sendMessage(ChatColor.GOLD + "TPS from last 1m, 5m, 15m: " + org.apache.commons.lang.StringUtils.join(tpsAvg, ", "));
        if (args.length > 0 && args[0].equals("mem") && sender.hasPermission("bukkit.command.tpsmemory")) {
            sender.sendMessage(ChatColor.GOLD + "Current Memory Usage: " + ChatColor.GREEN + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + "/" + (Runtime.getRuntime().totalMemory() / (1024 * 1024)) + " mb (Max: " + (Runtime.getRuntime().maxMemory() / (1024 * 1024)) + " mb)");
            if (!hasShownMemoryWarning) {
                sender.sendMessage(ChatColor.RED + "Warning: " + ChatColor.GOLD + " Memory usage on modern garbage collectors is not a stable value and it is perfectly normal to see it reach max. Please do not pay it much attention.");
                hasShownMemoryWarning = true;
            }
        }
        return true;
    }

    private static final long mean(long[] array) {
        if (array == null || array.length == 0) return 0L;
        long r = 0L;
        for (long i : array)
            r += i;
        return r / array.length;
    }

}
