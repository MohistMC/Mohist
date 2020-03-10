package red.mohist.down;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import red.mohist.util.JarLoader;
import red.mohist.util.MD5Util;
import red.mohist.util.i18n.Message;

public class DownloadLibraries {

    public static String url = "https://www.mgazul.cn/";

    public static void run() throws Exception {
        String path = null;
        try {
            if (Message.getLocale().contains("CN") || Message.getCountry().contains("CN")) {
                url = "https://mohist-community.gitee.io/mohistdown/"; //Gitee Mirror
            } else {
                url = "https://www.mgazul.cn/"; //Github Mirror
            }
        } catch (Exception e) {
            url = "https://www.mgazul.cn/"; //Github Mirror
        }
        InputStream listStream = DownloadLibraries.class.getClassLoader().getResourceAsStream("lib.red");
        if (listStream == null) return;
        Map<File, String> lib = new HashMap<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(listStream));
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                String[] args = str.split("\\|");
                if (args.length == 2) {
                    path = args[0];
                    String md5 = args[1];

                    try {
                        File file = new File(path);
                        // Judgement files and MD5
                        if ((!file.exists() || !MD5Util.getMD5(file).equals(md5))) {
                            lib.put(file, md5);
                        }
                    } catch (IOException e) {
                        System.out.println(e.toString());
                    }
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        if (lib.size() > 0) {
            for (Map.Entry<File, String> entry : lib.entrySet()) {

                String[] args = entry.getKey().getPath().replaceAll("\\\\", "/").split("/");
                int size = args.length;
                String jarname = args[size - 1];
                String filepath = entry.getKey().getPath().replaceAll("\\\\", "/").replace("/" + jarname, "");
                File newfile = new File(filepath);
                if (!newfile.exists()) {
                    newfile.mkdirs();
                }
                File jar = new File(filepath, jarname);
                new Download(url + entry.getKey().getPath().replace("\\", "/"), jar, jarname);
                JarLoader jarLoader = new JarLoader((URLClassLoader) ClassLoader.getSystemClassLoader());

                JarLoader.loadjar(jarLoader, filepath);
            }
        }
    }
}
