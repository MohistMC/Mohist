package red.mohist.bukkit.nms;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import red.mohist.bukkit.nms.utils.Decoder;
import red.mohist.bukkit.nms.utils.Downloader;
import red.mohist.util.JarTool;
import red.mohist.util.MD5Util;
import red.mohist.util.i18n.Message;

/**
 * @author Mgazul
 * @date 2020/6/5 0:44
 */
public class MappingFix {

    public static void init() throws Exception {
        Decoder dc = new Decoder();
        Downloader dw = new Downloader();
        //specify the dir
        String basedir = JarTool.getJarDir();
        File lib = new File(basedir + "/libraries/red/mohist/mappings/nms.srg");
        if (!lib.exists() || lib.length() < 4000000 || !checkMD5(lib)){
            //start download
            dw.execute(basedir);
            // File map = new File("resources/mappings/map.srg");
            File joined = new File(basedir + "/joined.srg");
            boolean directory = new File(basedir + "/libraries/red/mohist/mappings").mkdirs();
            if (directory)  {
                System.out.println(Message.getString("mappingfix.created.mappings"));
            }
            File nms = new File(basedir + "/libraries/red/mohist/mappings/nms.srg");
            if (nms.createNewFile()) {
                System.out.println(Message.getString("mappingfix.created.nms"));
            }
            //start decode
            System.out.println(Message.getString("mappingfix.decoding.start"));
            System.out.println("######Powered by MCP######");
            System.out.println(Message.getString("mappingfix.decoding.info"));
            dc.Decode(joined, nms);
            System.out.println(Message.getString("mappingfix.decoding.end"));
            joined.delete();
        }
    }

    public static boolean checkMD5(File lib) throws Exception {
        List<String> md5 = Arrays.asList(new String[]{"c55c10c1a5b56e1a1929cecc5027e49b", "b74eae233657e89bb98b8bbf737a9f51"});
        return md5.contains(MD5Util.getMD5(lib));
    }
}

