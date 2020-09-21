package com.mohistmc.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.File;

import static com.mohistmc.network.download.UpdateUtils.downloadFile;

public class DownloadFileCommand extends Command {

  public DownloadFileCommand(String name) {
    super(name);
    this.description = "Download a file from url directly in the choosed folder.";
    this.usageMessage = "/downloadfile";
    this.setPermission("mohist.command.downloadfile");
  }

  @Override
  public boolean execute(CommandSender sender, String currentAlias, String[] args) {
    if(testPermission(sender) || sender.isOp()) {
      if(args.length > 0) {
        if(args.length == 1) {
          sender.sendMessage("[MOHIST] - You need to specify the path of the file.");
          return false;
        }
        if(args.length == 2) {
          sender.sendMessage("[MOHIST] - You need to specify the link of the file.");
          return false;
        }
        if(args.length == 3) {
          try {
            downloadFile(args[2], new File(args[1] + "/" + args[0]));
            return true;
          } catch (Exception e) {
            sender.sendMessage("[MOHIST] - Failed to download the file.");
            e.printStackTrace();
            return false;
          }
        }
      } else {
        sender.sendMessage("[MOHIST] - You need to specify the file name.");
        return false;
      }
    }
    return true;
  }
}
