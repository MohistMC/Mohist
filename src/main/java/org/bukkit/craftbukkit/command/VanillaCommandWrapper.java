package org.bukkit.craftbukkit.command;

import java.util.List;


import net.minecraft.entity.EntityMinecartCommandBlockListener;
import net.minecraft.tileentity.TileEntityCommandBlockListener;

import org.apache.commons.lang.Validate;
import org.apache.logging.log4j.Level;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.defaults.*;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

public final class VanillaCommandWrapper extends VanillaCommand {
    protected final net.minecraft.command.CommandBase vanillaCommand;

    public VanillaCommandWrapper(net.minecraft.command.CommandBase vanillaCommand) {
        super(vanillaCommand.getCommandName());
        this.vanillaCommand = vanillaCommand;
    }

    public VanillaCommandWrapper(net.minecraft.command.CommandBase vanillaCommand, String usage) {
        super(vanillaCommand.getCommandName());
        this.vanillaCommand = vanillaCommand;
        this.description = "A Mojang provided command.";
        this.usageMessage = usage;
        this.setPermission("minecraft.command." + vanillaCommand.getCommandName());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;

        net.minecraft.command.ICommandSender icommandlistener = getListener(sender);
        // Some commands use the worldserver variable but we leave it full of null values,
        // so we must temporarily populate it with the world of the commandsender
        net.minecraft.world.WorldServer[] prev = net.minecraft.server.MinecraftServer.getServer().worldServers;
        net.minecraft.server.MinecraftServer.getServer().worldServers = new net.minecraft.world.WorldServer[]{(net.minecraft.world.WorldServer) icommandlistener.getEntityWorld()};
        try {
            vanillaCommand.processCommand(icommandlistener, args);
        } catch (net.minecraft.command.WrongUsageException exceptionusage) {
            net.minecraft.util.ChatComponentTranslation chatmessage = new net.minecraft.util.ChatComponentTranslation("commands.generic.usage", new Object[] {new net.minecraft.util.ChatComponentTranslation(exceptionusage.getMessage(), exceptionusage.getErrorOjbects())});
            chatmessage.getChatStyle().setColor(net.minecraft.util.EnumChatFormatting.RED);
            icommandlistener.addChatMessage(chatmessage);
        } catch (net.minecraft.command.CommandException commandexception) {
            net.minecraft.util.ChatComponentTranslation chatmessage = new net.minecraft.util.ChatComponentTranslation(commandexception.getMessage(), commandexception.getErrorOjbects());
            chatmessage.getChatStyle().setColor(net.minecraft.util.EnumChatFormatting.RED);
            icommandlistener.addChatMessage(chatmessage);
        } finally {
            net.minecraft.server.MinecraftServer.getServer().worldServers = prev;
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return (List<String>) vanillaCommand.addTabCompletionOptions(getListener(sender), args);
    }

    public final int dispatchVanillaCommandBlock(net.minecraft.command.server.CommandBlockLogic icommandlistener, String s) {
        // Copied from net.minecraft.server.CommandHandler
        s = s.trim();
        if (s.startsWith("/")) {
            s = s.substring(1);
        }
        String as[] = s.split(" ");
        as = dropFirstArgument(as);
        int i = getPlayerListSize(as);
        int j = 0;
        // Some commands use the worldserver variable but we leave it full of null values,
        // so we must temporarily populate it with the world of the commandsender
        net.minecraft.world.WorldServer[] prev = net.minecraft.server.MinecraftServer.getServer().worldServers;
        net.minecraft.server.MinecraftServer.getServer().worldServers = new net.minecraft.world.WorldServer[]{(net.minecraft.world.WorldServer) icommandlistener.getEntityWorld()};
        try {
            if (vanillaCommand.canCommandSenderUseCommand(icommandlistener)) {
                if (i > -1) {
                    net.minecraft.entity.player.EntityPlayerMP aentityplayer[] = net.minecraft.command.PlayerSelector.matchPlayers(icommandlistener, as[i]);
                    String s2 = as[i];
                    net.minecraft.entity.player.EntityPlayerMP aentityplayer1[] = aentityplayer;
                    int k = aentityplayer1.length;
                    for (int l = 0; l < k;) {
                        net.minecraft.entity.player.EntityPlayerMP entityplayer = aentityplayer1[l];
                        as[i] = entityplayer.getCommandSenderName();
                        try {
                            vanillaCommand.processCommand(icommandlistener, as);
                            j++;
                            continue;
                        } catch (net.minecraft.command.CommandException commandexception1) {
                            net.minecraft.util.ChatComponentTranslation chatmessage4 = new net.minecraft.util.ChatComponentTranslation(commandexception1.getMessage(), commandexception1.getErrorOjbects());
                            chatmessage4.getChatStyle().setColor(net.minecraft.util.EnumChatFormatting.RED);
                            icommandlistener.addChatMessage(chatmessage4);
                            l++;
                        }
                    }

                    as[i] = s2;
                } else {
                    vanillaCommand.processCommand(icommandlistener, as);
                    j++;
                }
            } else {
                net.minecraft.util.ChatComponentTranslation chatmessage = new net.minecraft.util.ChatComponentTranslation("commands.generic.permission", new Object[0]);
                chatmessage.getChatStyle().setColor(net.minecraft.util.EnumChatFormatting.RED);
                icommandlistener.addChatMessage(chatmessage);
            }
        } catch (net.minecraft.command.WrongUsageException exceptionusage) {
            net.minecraft.util.ChatComponentTranslation chatmessage1 = new net.minecraft.util.ChatComponentTranslation("commands.generic.usage", new Object[] { new net.minecraft.util.ChatComponentTranslation(exceptionusage.getMessage(), exceptionusage.getErrorOjbects()) });
            chatmessage1.getChatStyle().setColor(net.minecraft.util.EnumChatFormatting.RED);
            icommandlistener.addChatMessage(chatmessage1);
        } catch (net.minecraft.command.CommandException commandexception) {
            net.minecraft.util.ChatComponentTranslation chatmessage2 = new net.minecraft.util.ChatComponentTranslation(commandexception.getMessage(), commandexception.getErrorOjbects());
            chatmessage2.getChatStyle().setColor(net.minecraft.util.EnumChatFormatting.RED);
            icommandlistener.addChatMessage(chatmessage2);
        } catch (Throwable throwable) {
            net.minecraft.util.ChatComponentTranslation chatmessage3 = new net.minecraft.util.ChatComponentTranslation("commands.generic.exception", new Object[0]);
            chatmessage3.getChatStyle().setColor(net.minecraft.util.EnumChatFormatting.RED);
            icommandlistener.addChatMessage(chatmessage3);
            if(icommandlistener instanceof TileEntityCommandBlockListener) {
                TileEntityCommandBlockListener listener = (TileEntityCommandBlockListener) icommandlistener;
                net.minecraft.server.MinecraftServer.getLogger().log(Level.WARN, String.format("CommandBlock at (%d,%d,%d) failed to handle command", listener.getPlayerCoordinates().posX, listener.getPlayerCoordinates().posY, listener.getPlayerCoordinates().posZ), throwable);
            } else if (icommandlistener instanceof EntityMinecartCommandBlockListener) {
                EntityMinecartCommandBlockListener listener = (EntityMinecartCommandBlockListener) icommandlistener;
                net.minecraft.server.MinecraftServer.getLogger().log(Level.WARN, String.format("MinecartCommandBlock at (%d,%d,%d) failed to handle command", listener.getPlayerCoordinates().posX, listener.getPlayerCoordinates().posY, listener.getPlayerCoordinates().posZ), throwable);
            } else {
                net.minecraft.server.MinecraftServer.getLogger().log(Level.WARN, String.format("Unknown CommandBlock failed to handle command"), throwable);
            }
        } finally {
            net.minecraft.server.MinecraftServer.getServer().worldServers = prev;
        }
        return j;
    }

    private net.minecraft.command.ICommandSender getListener(CommandSender sender) {
        if (sender instanceof Player) {
            return ((CraftPlayer) sender).getHandle();
        }
        if (sender instanceof BlockCommandSender) {
            return ((CraftBlockCommandSender) sender).getTileEntity();
        }
        if (sender instanceof CommandMinecart) {
            return ((net.minecraft.entity.EntityMinecartCommandBlock) ((CraftMinecartCommand) sender).getHandle()).func_145822_e();
        }
        if (sender instanceof RemoteConsoleCommandSender) {
            return net.minecraft.network.rcon.RConConsoleSource.instance;
        }
        if (sender instanceof ConsoleCommandSender) {
            return ((CraftServer) sender.getServer()).getServer();
        }
        return null;
    }

    private int getPlayerListSize(String as[]) {
        for (int i = 0; i < as.length; i++) {
            if (vanillaCommand.isUsernameIndex(as, i) && net.minecraft.command.PlayerSelector.matchesMultiplePlayers(as[i])) {
                return i;
            }
        }
        return -1;
    }

    private String[] dropFirstArgument(String as[]) {
        String as1[] = new String[as.length - 1];
        for (int i = 1; i < as.length; i++) {
            as1[i - 1] = as[i];
        }

        return as1;
    }
}
