package com.mohistmc.command;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import static com.mohistmc.configuration.MohistConfig.setValueMohist;

public class WhitelistModsCommand extends Command {

  public WhitelistModsCommand(String name) {
    super(name);
    this.description = "Command to update, enable or disable the mods whitelist.";
    this.usageMessage = "/whitelistmods [enable|disable|update]";
    this.setPermission( "mohist.command.whitelistmods" );
  }

  @Override
  public boolean execute(CommandSender sender, String currentAlias, String[] args) {
    if(testPermission(sender) || sender.isOp()) {
      if (args.length == 0) {
        sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
        return false;
      }

      switch (args[0].toLowerCase()) {
        case "enable":
          setValueMohist("forge.modswhitelist.list", makeModList());
          setValueMohist("forge.enable_mods_whitelist", true);
          break;
        case "disable":
          setValueMohist("forge.enable_mods_whitelist", false);
          break;
        case "update":
          setValueMohist("forge.modswhitelist.list", makeModList());
          break;
      }
    }
    return true;
  }

  private static String list = "";
  private static String makeModList() {
    for (ModContainer mod : Loader.instance().getModList())
      if(!mod.getModId().toLowerCase().equals("mohist"))
        list = list + mod.getModId()+"@"+mod.getVersion()+",";
    return list.substring(0, list.length() - 1);
  }

}
