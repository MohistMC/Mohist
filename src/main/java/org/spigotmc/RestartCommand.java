package org.spigotmc;

import com.mohistmc.network.download.DownloadJava;
import com.mohistmc.network.download.UpdateUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.FileWriter;

public class RestartCommand extends Command {

    public RestartCommand(String name) {
        super(name);
        this.description = "Restarts the server";
        this.usageMessage = "/restart";
        this.setPermission("bukkit.command.restart");
    }

    public static void restart() {
        AsyncCatcher.enabled = false; // Disable async catcher incase it interferes with us
        File script = new File(SpigotConfig.restartScript);
        String os = DownloadJava.os();
        try {
            if(!script.exists()) {
                String scriptExt = ".sh";
                String add = "";
                if(os.equals("Windows")) {
                    scriptExt = ".bat";
                    add = " pause";
                }
                SpigotConfig.config.set("settings.restart-script", "./start" + scriptExt);
                SpigotConfig.config.save(SpigotConfig.CONFIG_FILE);
                script = new File("./start"+scriptExt);
                if(!script.exists()) {
                    script.createNewFile();

                    FileWriter fw = new FileWriter(script);
                    fw.write("java -jar " + UpdateUtils.getMohistJar().getName() + add);
                    fw.close();
                }

                System.out.println("Startup script '" + script.getName() + "' does not exist ! Automatically creating it...");
            }

            System.out.println("Attempting to restart with " + script.getName());

            // Disable Watchdog
            WatchdogThread.doStop();

            // Kick all players
            for (EntityPlayerMP p : MinecraftServer.getServerInst().getPlayerList().playerEntityList)
                p.connection.disconnect(SpigotConfig.restartMessage);

            Thread.sleep(100);

            // Close the socket so we can rebind with the new process
            MinecraftServer.getServerInst().getNetworkSystem().terminateEndpoints();

            // Give time for it to kick in
            Thread.sleep(100);

            // Actually shutdown
            try {
                MinecraftServer.getServerInst().stopServer();
            } catch (Exception ignored) {
            }

            // This will be done AFTER the server has completely halted
            File finalScript = script;
            Thread shutdownHook = new Thread(() -> {
                try {
                    if(os.equals("Windows"))
                        Runtime.getRuntime().exec("cmd /c start " + finalScript.getPath());
                    else
                        Runtime.getRuntime().exec("sh " + finalScript.getPath());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            shutdownHook.setDaemon(true);
            Runtime.getRuntime().addShutdownHook(shutdownHook);

            FMLCommonHandler.instance().exitJava(0, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if(testPermission(sender)) {
            MinecraftServer.getServerInst().processQueue.add(RestartCommand::restart);
        }
        return true;
    }
}
