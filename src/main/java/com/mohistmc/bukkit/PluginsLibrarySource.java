package com.mohistmc.bukkit;

import com.mohistmc.MohistMC;
import java.net.HttpURLConnection;
import java.net.URI;

/**
 * @author Mgazul by MohistMC
 * @date 2023/10/8 19:16:34
 */
public enum PluginsLibrarySource {

    ALIBABA("https://maven.aliyun.com/repository/public/"),
    MAVEN2("https://repo.maven.apache.org/maven2/");

    public final String url;

    public static final String DEFAULT = isCN() ? ALIBABA.url : MAVEN2.url;

    PluginsLibrarySource(String url) {
        this.url = url;
    }

    public static boolean isCN() {
        return MohistMC.i18n.isCN() && getUrlMillis(ALIBABA.url) < getUrlMillis(MAVEN2.url);
    }

    public static long getUrlMillis(String link) {
        try {
            var connection = (HttpURLConnection) URI.create(link).toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            var start = System.currentTimeMillis();
            var responseCode = connection.getResponseCode();
            var end = System.currentTimeMillis();
            return end - start;
        } catch (Exception e) {
            return -0L;
        }
    }
}
