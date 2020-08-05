package red.mohist.bukkit.nms.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipFile;
import red.mohist.bukkit.nms.utils.srgutils.IMappingFile;
import red.mohist.network.download.UpdateUtils;
import red.mohist.util.i18n.Message;

/**
 * @Author Phobetor
 *
 * @create 2020/6/7 3:29
 */

public class Downloader {

    public void execute(String temppath) {
      String url = "https://files.minecraftforge.net/maven/";
      if(Message.isCN()) url = "http://bmclapi2.bangbang93.com/maven/";
      System.out.println(Message.getString("mcp.download"));
      File mcptool = new File(temppath + "/mcp.zip");
      File partz = new File(temppath + "/joined.tsrg");
      String newurl = url + "de/oceanlabs/mcp/mcp_config/1.12.2-20200226.224830/mcp_config-1.12.2-20200226.224830.zip";
      try {
        UpdateUtils.downloadFile(newurl, mcptool);
        System.out.println(Message.getString("mcp.extract"));
        ZipFile mcp = new ZipFile(mcptool);
        Files.copy(mcp.getInputStream(mcp.getEntry("config/joined.tsrg")), partz.toPath());
        mcp.close();
        IMappingFile.load(partz).write(Paths.get(temppath + "/joined.srg"), IMappingFile.Format.SRG, false);
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
