package com.mohistmc.libraries;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import com.mohistmc.config.MohistConfigUtil;
import com.mohistmc.network.download.UpdateUtils;
import com.mohistmc.util.JarLoader;
import com.mohistmc.util.JarTool;
import com.mohistmc.util.MD5Util;
import com.mohistmc.util.i18n.i18n;

public class DefaultLibraries {
    private static int retry = 0;
    public static HashMap<String, String> fail = new HashMap<>();

    public static void run() throws Exception {
        System.out.println(i18n.get("libraries.checking.start"));
        String url = "https://maven.mohistmc.com/";
        LinkedHashMap<File, String> libs = getDefaultLibs();
        AtomicLong currentSize = new AtomicLong();
        Set<File> defaultLibs = new LinkedHashSet<>();
        for (File lib : getDefaultLibs().keySet()) {
            if(lib.exists() && MD5Util.getMd5(lib).equals(libs.get(lib))){
                currentSize.addAndGet(lib.length());
                continue;
            }
            if(MohistConfigUtil.getString(MohistConfigUtil.mohistyml, "libraries_black_list:", "xxxxx").contains(lib.getName())) {
                continue;
            }
            defaultLibs.add(lib);
        }
        for (File lib : defaultLibs) {
            lib.getParentFile().mkdirs();
            //if (i18n.isCN() && !lib.getName().contains("mcp_config")) url = "https://MohistMC.gitee.io/mohistdown/"; //Gitee Mirror
            String u = url + "libraries/" + lib.getAbsolutePath().replaceAll("\\\\", "/").split("/libraries/")[1];
            System.out.println(
                    i18n.get("libraries.global.percentage") +
                            Math.round(currentSize.get() * 100 / 62557711d) + "%"); //Global percentage
            try {
                UpdateUtils.downloadFile(u, lib);
                if(lib.getName().endsWith(".jar") && !lib.getName().contains("asm-tree-6.1.1.jar")) new JarLoader().loadJar(lib);
                currentSize.addAndGet(lib.length());
                fail.remove(u);
            } catch (Exception e) {
                System.out.println(i18n.get("file.download.nook", u));
                lib.delete();
                fail.put(u, lib.getAbsolutePath());
            }
        }
        /*FINISHED | RECHECK IF A FILE FAILED*/
        if (retry < 3 && !fail.isEmpty()) {
            retry++;
            System.out.println(i18n.get("libraries.check.retry", retry));
            run();
        } else {
            System.out.println(i18n.get("libraries.check.end"));
            if(!fail.isEmpty()) {
                System.out.println(i18n.get("libraries.check.missing"));
                for (String lib : fail.keySet())
                    System.out.println("Link : " + lib + "\nPath : " + fail.get(lib) + "\n");
                System.exit(0);
            }
        }
    }

    public static LinkedHashMap<File, String> getDefaultLibs() throws Exception {
        LinkedHashMap<File, String> temp = new LinkedHashMap<>();
        BufferedReader b = new BufferedReader(new InputStreamReader(DefaultLibraries.class.getClassLoader().getResourceAsStream("mohist_libraries.txt")));
        String str;
        while ((str = b.readLine()) != null) {
            String[] s = str.split("\\|");
            temp.put(new File(JarTool.getJarDir() + "/" + s[0]), s[1]);
        }
        b.close();
        return temp;
    }
}
