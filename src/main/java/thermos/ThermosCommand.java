package thermos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;

public class ThermosCommand extends Command {
    public static final String NAME = "thermos";
    public static final String CHECK = NAME + ".check";
    public static final String TPS = NAME + ".tps";
    public static final String RESTART = NAME + ".restart";
    public static final String DUMP = NAME + ".dump";
    public static final String UPDATE = NAME + ".update";
    public ThermosCommand() {
        super(NAME);

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("-------------------[" + ChatColor.RED + "Thermos" + ChatColor.RESET + "]-------------------\n"));
        builder.append(String.format("/%s check - Check for an update.\n", NAME));
        builder.append(String.format("/%s tps - Show tps statistics.\n", NAME));
        builder.append(String.format("/%s restart - Restart the server.\n", NAME));
        builder.append(String.format("/%s dump - Dump statistics into a thermos.dump file.\n", NAME));
        setUsage(builder.toString());

        setPermission("thermos");
    }

    public boolean testPermission(CommandSender target, String permission) {
        if (testPermissionSilent(target, permission)) {
            return true;
        }
        target.sendMessage(ChatColor.RED + "[Thermos] " + ChatColor.DARK_RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is an error.");
        return false;
    }

    public boolean testPermissionSilent(CommandSender target, String permission) {
        if (!super.testPermissionSilent(target)) {
            return false;
        }
        for (String p : permission.split(";"))
            if (target.hasPermission(p))
                return true;
        return false;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender))
            return true;
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "[Thermos] " + ChatColor.GRAY + "Please specify action");
            sender.sendMessage(ChatColor.RED + "[Thermos] " + ChatColor.GRAY + usageMessage);
            return true;
        }
        String action = args[0];
        if ("tps".equals(action)) {
            if (!testPermission(sender, TPS))
                return true;
            World currentWorld = null;
            if (sender instanceof CraftPlayer) {
                currentWorld = ((CraftPlayer) sender).getWorld();
            }
            sender.sendMessage(ChatColor.DARK_RED + "---------------------------------------");
            final MinecraftServer server = MinecraftServer.getServer();
            ChatColor colourTPS;
            for (World world : server.server.getWorlds()) {
                if (world instanceof CraftWorld) {
                    boolean current = currentWorld != null && currentWorld == world;
                    net.minecraft.world.WorldServer mcWorld = ((CraftWorld) world).getHandle();
                    String bukkitName = world.getName();
                    int dimensionId = mcWorld.provider.dimensionId;
                    String name = mcWorld.provider.getDimensionName();
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
        } else if ("dump".equals(action)) {
            if (!testPermission(sender, DUMP))
                return true;
            try {
                File outputFile = new File("thermos.dump");
                OutputStream os = new FileOutputStream(outputFile);
                Writer writer = new OutputStreamWriter(os);
                for (WorldServer world : DimensionManager.getWorlds()) {
                    writer.write(String.format("Stats for %s [%s] with id %d\n", world,
                            world.provider.getDimensionName(), world.dimension));
                    writer.write("Current tick: " + world.worldInfo.getWorldTotalTime() + "\n");
                    writer.write("\nEntities: ");
                    writer.write("count - " + world.loadedEntityList.size() + "\n");
                    for (Entity entity : (Iterable<Entity>) world.loadedEntityList) {
                        writer.write(String.format("  %s at (%.4f;%.4f;%.4f)\n", entity.getClass().getName(),
                                entity.posX, entity.posY, entity.posZ));
                    }
                    writer.write("\nTileEntities: ");
                    writer.write("count - " + world.loadedTileEntityList.size() + "\n");
                    for (TileEntity entity : (Iterable<TileEntity>) world.loadedTileEntityList) {
                        writer.write(String.format("  %s at (%d;%d;%d)\n", entity.getClass().getName(), entity.xCoord,
                                entity.yCoord, entity.zCoord));
                    }
                    writer.write("\nLoaded chunks: ");
                    writer.write("count - " + world.activeChunkSet.size() + "\n");
                    for (ChunkCoordIntPair chunkCoords : (Iterable<ChunkCoordIntPair>) world.activeChunkSet) {
                        final int x = chunkCoords.chunkXPos;
                        final int z = chunkCoords.chunkZPos;
                        Chunk chunk = world.chunkProvider.provideChunk(x, z);
                        if (chunk == null)
                            continue;
                        writer.write(String.format("Chunk at (%d;%d)\n", x, z));
                        @SuppressWarnings("unchecked")
                        List<NextTickListEntry> updates = world.getPendingBlockUpdates(chunk, false);
                        writer.write("Pending block updates [" + updates.size() + "]:\n");
                        for (NextTickListEntry entry : updates) {
                            writer.write(String.format("(%d;%d;%d) at %d with priority %d\n", entry.xCoord,
                                    entry.yCoord, entry.zCoord, entry.scheduledTime, entry.priority));
                        }
                    }
                    writer.write("-------------------------\n");
                }
                writer.close();
                sender.sendMessage(ChatColor.RED + "Dump saved!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Unknown action");
        }
        return true;
    }

    private static final long mean(long[] array) {
        long r = 0;
        for (long i : array)
            r += i;
        return r / array.length;
    }

}
