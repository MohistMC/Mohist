package red.mohist.bukkit.nms;

import java.io.File;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
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
  private static int percentage = 0;
  public static void init() throws Exception {
    Decoder dc = new Decoder();
    Downloader dw = new Downloader();
    //specify the dir
    String basedir = JarTool.getJarDir();
    File lib = new File(basedir + "/libraries/red/mohist/mappings/nms.srg");
    if (!lib.exists() || lib.length() < 4000000 || !checkMD5(lib)){
      File joined = new File(basedir + "/joined.srg");
      File nms = new File(basedir + "/libraries/red/mohist/mappings/nms.srg");
      //start download
      dw.execute(basedir);
      if(new File(basedir + "/libraries/red/mohist/mappings").mkdirs())
        System.out.println(Message.getString("mappingfix.created.mappings"));
      if(nms.createNewFile())
        System.out.println(Message.getString("mappingfix.created.nms"));
      //start decode
      System.out.println(Message.getString("mappingfix.decoding.start"));
      System.out.println("#################################################\n" +
              "                 Powered by MCP                  \n" +
              "             http://modcoderpack.com             \n" +
              "     by: Searge, ProfMobius, R4wk, ZeuX          \n" +
              "     Fesh0r, IngisKahn, bspkrs, LexManos         \n" +
              "#################################################");
      System.out.println(Message.getString("mappingfix.decoding.info"));
      Timer t = new Timer();
      t.schedule(new TimerTask() {
        @Override
        public void run() {
          if(percentage != Math.round((float)lib.length()/4000000*100))
            System.out.println(Message.getString("mapping.decoding.progress") + percentage + "%");
          percentage = Math.round((float)lib.length()/4000000*100);
        }
      }, 3000, 3000);
      long startTime = System.currentTimeMillis();
      dc.Decode(joined, nms);
      t.cancel();
      System.out.println(Message.getFormatString("mappingfix.decoding.end", new Object[]{(System.currentTimeMillis() - startTime) / 1000}));
      System.gc();
      Thread.sleep(100);
      joined.delete();
    }
  }

  public static boolean checkMD5(File lib) throws Exception {
    return Arrays.asList("c55c10c1a5b56e1a1929cecc5027e49b", "b74eae233657e89bb98b8bbf737a9f51").contains(MD5Util.getMD5(lib));
  }
}

