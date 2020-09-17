package com.mohistmc.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HasteUtils {
    public static String paste(String text) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("https://hastebin.com/documents").openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", "Hastebin Java Api");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(text);
        wr.flush();
        wr.close();
        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = rd.readLine();
        rd.close();
        return "https://hastebin.com/" + response.substring(response.indexOf(":") + 2, response.length() - 2);
    }
}
