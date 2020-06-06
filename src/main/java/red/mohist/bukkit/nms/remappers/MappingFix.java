package red.mohist.bukkit.nms.remappers;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import red.mohist.util.JarTool;
import red.mohist.util.MD5Util;
import red.mohist.util.i18n.Message;

/**
 * @author Mgazul
 * @date 2020/6/5 0:44
 */
public class MappingFix {

    static FileChannel fileChannel;

    public static void init() throws Exception {
        File nms = new File(JarTool.getJarDir() + "/libraries/red/mohist/mappings", "nms12.red");
        String p = "libraries/red/mohist/mappings/nms12.red";
        String url = "https://www.mgazul.cn/";
        if (!nms.exists() || MD5Util.md5CheckSum(nms, "6e2b64361122a2eb7c045c43aa3f3c76")) {
            nms.getParentFile().mkdirs();
            if(Message.isCN()) url = "https://mohist-community.gitee.io/mohistdown/"; //Gitee Mirror
            URLConnection conn = new URL(url + p).openConnection(); //MAKE CONNECTION TO GET THE CONTENT LENGTH
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
            conn.connect();

            fileChannel = FileChannel.open(Paths.get(nms.getAbsolutePath()), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            fileChannel.transferFrom(Channels.newChannel(conn.getInputStream()), 0L, Long.MAX_VALUE);
            if(fileChannel != null) fileChannel.close();
        }
    }
}
