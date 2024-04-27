package com.mohistmc.network.download;


import com.mohistmc.configuration.MohistConfigUtil;
import com.mohistmc.util.i18n.Message;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public enum DownloadSource {

    MOHIST("https://maven.mohistmc.com/"),
    CHINA("https://libraries.mohistmc.cn:25119/releases/"),
    GITHUB("https://mohistmc.github.io/maven/");

    public static final DownloadSource defaultSource = Message.isCN() ? CHINA : MOHIST;
    final String url;

    DownloadSource(String url) {
        this.url = url;
    }

    public static DownloadSource get() {
        String ds = MohistConfigUtil.sMohist("libraries_downloadsource", defaultSource.name());
        for (DownloadSource me : DownloadSource.values()) {
            if (me.name().equalsIgnoreCase(ds)) {
                if (isDown(me.url.replace("releases/", "")) != 200) return GITHUB;
                return me;
            }
        }
        return defaultSource;
    }

    public String getUrl() {
        return url;
    }

    public static int isDown(String s) {
        try {
            URL url = new URL(s);
            URLConnection rulConnection = url.openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;
            httpUrlConnection.connect();
            return httpUrlConnection.getResponseCode();
        } catch (Exception e) {
            return 0;
        }
    }
}
