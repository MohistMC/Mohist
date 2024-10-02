package com.mohistmc.action;

import com.mohistmc.MohistMCStart;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ZipTree {

    public static void init() throws IOException {
        InputStream is = MohistMCStart.class.getClassLoader().getResourceAsStream("META-INF/libraries");
        getFileContent(is);
    }

    public static void getFileContent(Object fileInPath) throws IOException {
        BufferedReader br = null;
        if (fileInPath == null) {
            return;
        }
        if (fileInPath instanceof String) {
            br = new BufferedReader(new FileReader((String) fileInPath));
        } else if (fileInPath instanceof InputStream) {
            br = new BufferedReader(new InputStreamReader((InputStream) fileInPath));
        }
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
    }
}
