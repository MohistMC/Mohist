package red.mohist.network.download;

import red.mohist.configuration.MohistConfigUtil;
import red.mohist.util.JarLoader;
import red.mohist.util.i18n.Message;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URLClassLoader;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.HashMap;

import static red.mohist.network.download.UpdateUtils.downloadFile;

public class DownloadLibraries {
    static boolean needToRecheck = false;
    static int retry = 0;
    static FileChannel fileChannel;
    static HashMap<String, String> fail = new HashMap<>();

    public static void run() throws Exception {
        System.out.println(Message.getString("libraries.checking.start"));
        String str;
        String url = "https://www.mgazul.cn/";
        if (Message.isCN()) url = "https://mohist-community.gitee.io/mohistdown/"; //Gitee Mirror
        BufferedReader b = new BufferedReader(new InputStreamReader(DownloadLibraries.class.getClassLoader().getResourceAsStream("lib.red")));
        while ((str = b.readLine()) != null) {
            String[] args = str.split("\\|");
            if (args.length == 2) {
                File file = new File(args[0]);
                if ((!file.exists() || !DatatypeConverter.printHexBinary(MessageDigest.getInstance("MD5").digest(Files.readAllBytes(file.toPath()))).toLowerCase().equals(args[1])) && !MohistConfigUtil.getString(new File("mohist-config", "mohist.yml"), "libraries_black_list:", "xxxxx").contains(file.getName())) {
                    file.getParentFile().mkdirs();
                    String u = url + args[0];
                    System.out.println("Global percentage » " + String.valueOf((float) UpdateUtils.getSizeOfDirectory(new File("libraries")) / 35 * 100).substring(0, 2).replace(".", "") + "%"); //Global percentage

                    try {
                        downloadFile(u, file);
                    } catch (Exception e) {
                        System.out.println(Message.getFormatString("file.download.nook", new Object[]{u}));
                        needToRecheck = true;
                        retry++;
                        if (retry == 2) fail.put(u, file.getAbsolutePath());
                    }

                    JarLoader.loadjar(new JarLoader((URLClassLoader) ClassLoader.getSystemClassLoader()), file.getParent());
                }
            }
        }
        b.close();
        /*FINISHED | RECHECK IF A FILE FAILED*/
        if (needToRecheck && retry < 3) {
            needToRecheck = false;
            System.out.println(Message.getFormatString("update.retry", new Object[]{retry}));
            run();
        } else {
            if (fileChannel != null)
                fileChannel.close();
            System.out.println(Message.getString("libraries.checking.end"));
            if (!fail.isEmpty()) {
                System.out.println(Message.getString("libraries.cant.start.server"));
                for (String lib : fail.keySet()) System.out.println("Link : " + lib + "\nPath : " + fail.get(lib));
            }
        }
    }
}
