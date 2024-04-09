package com.mohistmc.mohist.bukkit;

import com.mohistmc.mohist.Mohist;
import com.mohistmc.tools.ConnectionUtil;

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
        return Mohist.i18n.isCN() &&  ConnectionUtil.measureLatency(ALIBABA.url) < ConnectionUtil.measureLatency(MAVEN2.url);
    }
}
