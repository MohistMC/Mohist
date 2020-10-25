package com.mohistmc.bukkit.nms.utils;

import com.mohistmc.bukkit.nms.utils.srgutils.IMappingFile;
import com.mohistmc.network.download.UpdateUtils;
import com.mohistmc.util.i18n.Message;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipFile;

/**
 * @Author Phobetor
 *
 * @create 2020/6/7 3:29
 */

public class Downloader {

  public void execute() {
    System.out.println(Message.getString("mcp.download"));
    File mcptool = new File("mcp.zip");
    File partz = new File("joined.tsrg");
    String u = "https://files.minecraftforge.net/maven/de/oceanlabs/mcp/mcp_config/1.12.2-20200226.224830/mcp_config-1.12.2-20200226.224830.zip";
    String mu = "https://gitee.com/Mohist-Community/MohistDown/raw/master/dl/de/oceanlabs/mcp/mcp_config/1.12.2-20200226.224830/mcp_config-1.12.2-20200226.224830.zip"; //Gitee Mirror
    try {
      if (Message.isCN()) {
        System.out.println(Message.getString("mcp.download.gitee"));
        UpdateUtils.downloadFile(mu, mcptool);
      } else {
        System.out.println(Message.getString("mcp.download.forge"));
        UpdateUtils.downloadFile(u, mcptool);
      }
      System.out.println(Message.getString("mcp.extract"));
      ZipFile mcp = new ZipFile(mcptool);
      Files.copy(mcp.getInputStream(mcp.getEntry("config/joined.tsrg")), partz.toPath());
      mcp.close();
      IMappingFile.load(partz).write(Paths.get("joined.srg"), IMappingFile.Format.SRG, false);
      System.gc();
      Thread.sleep(100);
      mcptool.delete();
      partz.delete();
    } catch (Throwable e) {
      System.out.println(Message.getString("mcp.dl.fail"));
      System.exit(-1);
    }
  }
}
