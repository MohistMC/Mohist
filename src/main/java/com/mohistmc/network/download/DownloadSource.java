package com.mohistmc.network.download;


import com.mohistmc.configuration.MohistConfigUtil;
import com.mohistmc.util.i18n.Message;

public enum DownloadSource {

    MOHIST("https://maven.mohistmc.com/"),
    CHINA("http://s1.devicloud.cn:25119/"),
    GITHUB("https://mavenmirror.mohistmc.com/");

    public static final DownloadSource defaultSource = Message.isCN() ? CHINA : MOHIST;
    final String url;

    DownloadSource(String url) {
        this.url = url;
    }

    public static DownloadSource get() {
        String ds = MohistConfigUtil.sMohist("libraries_downloadsource", defaultSource.name());
        for (DownloadSource me : DownloadSource.values()) {
            if (me.name().equalsIgnoreCase(ds))
                return me;
        }
        return defaultSource;
    }

    public String getUrl() {
        return url;
    }
}
