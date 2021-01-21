package com.mohistmc.network.download;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class NetworkUtil {

    public static URLConnection getConn(String URL) {
        URLConnection conn = null;
        try {
            conn = new URL(URL).openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static InputStream getInput(String URL) throws IOException {
        return getConn(URL).getInputStream();
    }

}
