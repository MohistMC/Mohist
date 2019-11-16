package org.spigotmc;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class VanillaCommandWrapper
{

    public static final HashSet<String> allowedCommands = new HashSet<String>();

    public static int dispatch(CommandSender sender, String commandLine)
    {
        int pos = commandLine.indexOf( ' ' );
        if ( pos == -1 )
        {
            pos = commandLine.length();
        }
        String name = commandLine.substring( 0, pos );
        if ( !allowedCommands.contains( name ) )
        {
            return -1;
        }
        if ( !sender.hasPermission( "bukkit.command." + name ) )
        {
            sender.sendMessage( ChatColor.RED + "You do not have permission for this command" );
            return 0;
        }
        net.minecraft.command.ICommandSender listener = getListener( sender );
        if ( listener == null )
        {
            return -1;
        }
        return net.minecraft.server.MinecraftServer.getServer().getCommandManager().executeCommand( listener, commandLine );
    }

    public static List<String> complete(CommandSender sender, String commandLine)
    {
        int pos = commandLine.indexOf( ' ' );
        if ( pos == -1 )
        {
            List<String> completions = new ArrayList<String>();
            commandLine = commandLine.toLowerCase();
            for ( String command : allowedCommands )
            {
                if ( command.startsWith( commandLine ) && sender.hasPermission( "bukkit.command." + command ) )
                {
                    completions.add( "/" + command );
                }
            }
            return completions;
        }
        String name = commandLine.substring( 0, pos );
        if ( !allowedCommands.contains( name ) || !sender.hasPermission( "bukkit.command." + name ) )
        {
            return ImmutableList.<String>of();
        }
        net.minecraft.command.ICommandSender listener = getListener( sender );
        if ( listener == null )
        {
            return ImmutableList.<String>of();
        }
        return net.minecraft.server.MinecraftServer.getServer().getCommandManager().getPossibleCommands( listener, commandLine );
    }

    private static net.minecraft.command.ICommandSender getListener(CommandSender sender)
    {
        if ( sender instanceof CraftPlayer )
        {
            return new PlayerListener( ( (CraftPlayer) sender ).getHandle() );
        }
        if ( sender instanceof CraftBlockCommandSender )
        {
            CraftBlockCommandSender commandBlock = (CraftBlockCommandSender) sender;
            Block block = commandBlock.getBlock();
            return ( (net.minecraft.tileentity.TileEntityCommandBlock) ( (CraftWorld) block.getWorld() ).getTileEntityAt( block.getX(), block.getY(), block.getZ() ) ).func_145993_a();
        }
        if ( sender instanceof CraftMinecartCommand )
        {
            return ( (net.minecraft.entity.EntityMinecartCommandBlock) ( (CraftMinecartCommand) sender ).getHandle() ).func_145822_e();
        }
        return new ConsoleListener(sender); // Assume console/rcon
    }

    private static class PlayerListener implements net.minecraft.command.ICommandSender
    {

        private final net.minecraft.command.ICommandSender handle;

        public PlayerListener(net.minecraft.command.ICommandSender handle)
        {
            this.handle = handle;
        }

        @Override
        public String getCommandSenderName()
        {
            return handle.getCommandSenderName();
        }

        @Override
        public net.minecraft.util.IChatComponent func_145748_c_()
        {
            return handle.func_145748_c_();
        }

        @Override
        public void addChatMessage(net.minecraft.util.IChatComponent iChatBaseComponent)
        {
            handle.addChatMessage( iChatBaseComponent );
        }

        @Override
        public boolean canCommandSenderUseCommand(int i, String s)
        {
            return true;
        }

        @Override
        public net.minecraft.util.ChunkCoordinates getPlayerCoordinates()
        {
            return handle.getPlayerCoordinates();
        }

        @Override
        public net.minecraft.world.World getEntityWorld()
        {
            return handle.getEntityWorld();
        }
    }

    private static class ConsoleListener implements net.minecraft.command.ICommandSender {

        private final CommandSender sender;

        public ConsoleListener( CommandSender sender )
        {
            this.sender = sender;
        }

        @Override
        public String getCommandSenderName()
        {
            return sender.getName();
        }

        @Override
        public net.minecraft.util.IChatComponent func_145748_c_()
        {
            return new net.minecraft.util.ChatComponentText( getCommandSenderName() );
        }

        @Override
        public void addChatMessage( net.minecraft.util.IChatComponent iChatBaseComponent )
        {
            sender.sendMessage( iChatBaseComponent.getUnformattedTextForChat() );
        }

        @Override
        public boolean canCommandSenderUseCommand( int i, String s )
        {
            return true;
        }

        @Override
        public net.minecraft.util.ChunkCoordinates getPlayerCoordinates()
        {
            return new net.minecraft.util.ChunkCoordinates( 0, 0, 0 );
        }

        @Override
        public net.minecraft.world.World getEntityWorld()
        {
            return net.minecraft.server.MinecraftServer.getServer().getEntityWorld();
        }
    }
}
