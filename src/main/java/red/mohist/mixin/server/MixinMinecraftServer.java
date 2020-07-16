package red.mohist.mixin.server;

import jline.console.ConsoleReader;
import joptsimple.OptionSet;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import red.mohist.extra.server.ExtraMinecraftServer;

import java.io.File;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer implements ExtraMinecraftServer {

    // CraftBukkit start
    public OptionSet options;
    public org.bukkit.command.ConsoleCommandSender console;
    public org.bukkit.command.RemoteConsoleCommandSender remoteConsole;
    public ConsoleReader reader;
    public int currentTick = (int) (System.currentTimeMillis() / 50);
    public java.util.Queue<Runnable> processQueue = new java.util.concurrent.ConcurrentLinkedQueue<Runnable>();
    public int autosavePeriod;
    public File bukkitDataPackFolder;
    private boolean forceTicks;
    // CraftBukkit end

    @Override
    public File getBukkitDataPackFolder() {
        return this.bukkitDataPackFolder;
    }
}
