package com.mohistmc.network.download;

import com.mohistmc.configuration.MohistConfigUtil;
import com.mohistmc.util.JarLoader;
import com.mohistmc.util.MD5Util;
import com.mohistmc.util.i18n.Message;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URLClassLoader;
import java.util.HashMap;

public class DownloadLibraries {
    static HashMap<String, String> fail = new HashMap<>();

    public static void run() throws Exception {
        System.out.println(Message.getString("libraries.checking.start"));
        String str;
        String url = "https://www.mgazul.cn/";
        if (Message.isCN()) url = "https://mohist-community.gitee.io/mohistdown/"; //Gitee Mirror
        BufferedReader b = new BufferedReader(new InputStreamReader(DownloadLibraries.class.getClassLoader().getResourceAsStream("mohist_libraries.txt")));
        while ((str = b.readLine()) != null) {
            String[] args = str.split("\\|");
            if (args.length == 2) {
                File file = new File(args[0]);
                //System.out.println(MD5Util.getMD5(file) + " : " +  args[1]);
                if ((!file.exists() || !MD5Util.md5CheckSum(file, args[1]))) {
                    if (MohistConfigUtil.getString(MohistConfigUtil.mohistyml, "libraries_black_list:", "xxxxx").contains(file.getName())) continue;
                    file.getParentFile().mkdirs();
                    String u = url + args[0];
                    System.out.println(Message.getString("libraries.global.percentage") + String.valueOf((float) UpdateUtils.getSizeOfDirectory(new File("libraries")) / 35 * 100).substring(0, 2).replace(".", "") + "%"); //Global percentage

                    try {
                        UpdateUtils.downloadFile(u, file);
                        if (!file.getName().equals("minecraft_server.1.12.2.jar")) {
                            JarLoader.loadjar(new JarLoader((URLClassLoader) ClassLoader.getSystemClassLoader()), file.getParent());
                        }
                        if (fail.containsKey(u)) fail.remove(u);
                    } catch (Exception e) {
                        System.out.println(Message.getFormatString("file.download.nook", new Object[]{u}));
                        file.delete();
                        fail.put(u, file.getAbsolutePath());
                    }
                }
            }
        }
        b.close();
        /*FINISHED | RECHECK IF A FILE FAILED*/
        if (!fail.isEmpty()) {
            run();
        } else {
            System.out.println(Message.getString("libraries.checking.end"));
            if (!fail.isEmpty()) {
                System.out.println(Message.getString("libraries.cant.start.server"));
                for (String lib : fail.keySet()) {
                    System.out.println("Link : " + lib + "\nPath : " + fail.get(lib) + "\n");
                }
                System.exit(0);
            }
        }
    }
}
