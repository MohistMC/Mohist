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
import red.mohist.util.Decoder;
import red.mohist.util.Downloader;


/**
 * @author Mgazul
 * @date 2020/6/5 0:44
 */
public class MappingFix {

    static FileChannel fileChannel;




    public static void init() throws Exception {

        Decoder dc = new Decoder();
        Downloader dw = new Downloader();

        //specify the dir
        String basedir = JarTool.getJarDir();

        File lib = new File(basedir + "/libraries/red/mohist/mappings/nms.srg");
        if (!lib.exists()){
        //start download

        dw.execute(basedir);
       // File map = new File("resources/mappings/map.srg");
        File joined = new File(basedir + "/joined.srg");

        boolean directory = new File(basedir + "/libraries/red/mohist/mappings").mkdirs();
        if (directory)  {
            System.out.println("/mappings created");
        }
        File nms = new File(basedir + "/libraries/red/mohist/mappings/nms.srg");
        if (nms.createNewFile()) {
            System.out.println("File nms is created!");
        }


        //start decode
            System.out.println("Start decoding");
            System.out.println("######Powered by MCP######");


            dc.Decode( joined, nms);
            System.out.println("Decoding Completed");
            joined.delete();

}










    }
}
