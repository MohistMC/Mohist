package com.mohistmc.bukkit;

/**
 * @author Mgazul by MohistMC
 * @date 2023/10/8 19:16:34
 */
public enum PluginsLibrarySource {

    MAVEN2("https://repo.maven.apache.org/maven2/");

    public final String url;

    public static final String DEFAULT = MAVEN2.url;

    PluginsLibrarySource(String url) {
        this.url = url;
    }
}
