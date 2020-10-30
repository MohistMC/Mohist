package org.spigotmc;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.Player;

public class RestartCommand extends Command
{

    public RestartCommand(String name)
    {
        super( name );
        this.description = "Restarts the server";
        this.usageMessage = "/restart";
        this.setPermission( "bukkit.command.restart" );
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args)
    {
        if ( testPermission( sender ) )
        {
            restart();
        }
        return true;
    }
    
    public static void restart() {
        restart(false);
    }

    public static void restart(boolean forbidShutdown)
    {
        try
        {
            final File file = new File( SpigotConfig.restartScript );
            if ( file.isFile() )
            {
                System.out.println( "Attempting to restart with " + SpigotConfig.restartScript );

                // Forbid new logons
                net.minecraft.server.dedicated.DedicatedServer.allowPlayerLogins = false;

                // Kick all players
                for ( Object p :  net.minecraft.server.MinecraftServer.getServer().getConfigurationManager().playerEntityList.toArray() )
                {
                    if(p instanceof net.minecraft.entity.player.EntityPlayerMP)
                    {
                        net.minecraft.entity.player.EntityPlayerMP mp = ( net.minecraft.entity.player.EntityPlayerMP)p;
                        mp.playerNetServerHandler.kickPlayerFromServer(SpigotConfig.restartMessage);
                        mp.playerNetServerHandler.netManager.isChannelOpen();
                    }

                }

                // Give the socket a chance to send the packets
                try
                {
                    Thread.sleep( 100 );
                } catch ( InterruptedException ex )
                {
                }
                // Close the socket so we can rebind with the new process
                net.minecraft.server.MinecraftServer.getServer().func_147137_ag().terminateEndpoints();

                // Give time for it to kick in
                try
                {
                    Thread.sleep( 100 );
                } catch ( InterruptedException ex )
                {
                }

                // Actually shutdown
                try
                {
                    Bukkit.shutdown();
                } catch ( Throwable t )
                {
                }

                // This will be done AFTER the server has completely halted
                Thread shutdownHook = new Thread()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            String os = System.getProperty( "os.name" ).toLowerCase();
                            if ( os.contains( "win" ) )
                            {
                                Runtime.getRuntime().exec( "cmd /c start " + file.getPath() );
                            } else
                            {
                                Runtime.getRuntime().exec( new String[]
                                {
                                    "/bin/sh", file.getPath()
                                } );
                            }
                        } catch ( Exception e )
                        {
                            e.printStackTrace();
                        }
                    }
                };

                shutdownHook.setDaemon( true );
                Runtime.getRuntime().addShutdownHook( shutdownHook );
            } else
            {
                if (forbidShutdown) {
                    System.out.println("Attempt to restart server without restart script, decline request");
                    return;
                }
                System.out.println( "Startup script '" + SpigotConfig.restartScript + "' does not exist! Stopping server." );
            }
            cpw.mods.fml.common.FMLCommonHandler.instance().exitJava(0, false);
        } catch ( Exception ex )
        {
            ex.printStackTrace();
        }
    }
}
