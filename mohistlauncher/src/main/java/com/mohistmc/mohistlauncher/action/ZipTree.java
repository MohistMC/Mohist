package com.mohistmc.mohistlauncher.action;

import com.mohistmc.mohistlauncher.Main;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ZipTree {

    public static void init() throws IOException {
        InputStream is = Main.class.getClassLoader().getResourceAsStream("META-INF/libraries");
        getFileContent(is);
    }

    public static void getFileContent(Object fileInPath) throws IOException {
        BufferedReader br = null;
        switch (fileInPath) {
            case null -> {
                return;
            }
            case String s -> br = new BufferedReader(new FileReader(s));
            case InputStream inputStream -> br = new BufferedReader(new InputStreamReader(inputStream));
            default -> {
            }
        }
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
    }
}
