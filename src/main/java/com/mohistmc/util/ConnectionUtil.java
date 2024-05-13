package com.mohistmc.util;

import com.mohistmc.tools.Tools;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.StandardOpenOption;
import java.time.Duration;

/**
 * @author Mgazul by MohistMC
 * @date 2023/10/7 0:58:48
 */
public class ConnectionUtil {

    private static final boolean debug = true;

    public static boolean isValid(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static boolean canAccess(String urlStr) {
        try {
            URLConnection urlConnection = getConn(urlStr);
            if (urlConnection == null) {
                return false;
            }
            HttpURLConnection connection = (HttpURLConnection) urlConnection;
            connection.connect();

            int responseCode = connection.getResponseCode();
            return responseCode >= 200 && responseCode < 300;
        } catch (IOException e) {
            return false;
        }
    }

    public static Integer getCode(String urlStr) {
        try {
            URLConnection urlConnection = getConn(urlStr);
            if (urlConnection == null) {
                return null;
            }
            HttpURLConnection connection = (HttpURLConnection) urlConnection;
            connection.connect();
            return connection.getResponseCode();
        } catch (IOException e) {
            return null;
        }
    }

    public static URLConnection getConn(String URL) {
        URLConnection connection;
        try {
            connection = URI.create(URL).toURL().openConnection();
            connection.setRequestProperty("User-Agent", "MohistMC-Tools/" + Tools.version());
            return connection;
        } catch (IOException e) {
            return null;
        }
    }

    public static long measureLatency(String urlString) {
        try {
            long start = System.nanoTime();
            URLConnection urlConnection = getConn(urlString);
            if (urlConnection == null) {
                return Long.MAX_VALUE;
            }
            HttpURLConnection connection = (HttpURLConnection) urlConnection;

            int responseCode = connection.getResponseCode();
            long end = System.nanoTime();
            return Duration.ofNanos(end - start).toMillis();
        } catch (Exception e) {
            return Long.MAX_VALUE;
        }
    }

    public static boolean downloadFile(String URL, File f) {
        try {
            URLConnection conn = getConn(URL);
            if (conn == null) {
                return false;
            }
            ReadableByteChannel rbc = Channels.newChannel(conn.getInputStream());
            FileChannel fc = FileChannel.open(f.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

            fc.transferFrom(rbc, 0, Long.MAX_VALUE);
            fc.close();
            rbc.close();
            return true;
        } catch (IOException | NullPointerException ignored) {
            return false;
        }
    }

    static class LatencyResult {
        final String url;
        final long latency;

        LatencyResult(String url, long latency) {
            this.url = url;
            this.latency = latency;
        }
    }
}
