package red.mohist.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author ChenhaoShen
 */
public class HttpUtil {

    public static String doGet(String url) throws Exception{
        URL localURL = new URL(url);
        URLConnection connection = localURL.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        InputStream in = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line = null;

        if (httpURLConnection.getResponseCode() >= 300) {
            throw new Exception("HTTP Request Failed! Response code is " + httpURLConnection.getResponseCode());
        }
        try {

            in = httpURLConnection.getInputStream();
            isr = new InputStreamReader(in);
            br = new BufferedReader(isr);

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            if (br != null) {
                br.close();
            }

            if (isr != null) {
                isr.close();
            }

            if (in != null) {
                in.close();
            }
        }
        return sb.toString();
    }
}
