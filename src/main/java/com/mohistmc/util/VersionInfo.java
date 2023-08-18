package com.mohistmc.util;

import java.util.Map;

/**
 * @author Mgazul by MohistMC
 * @date 2023/8/18 11:47:11
 */
public record VersionInfo(String mohist, String bukkit, String craftbukkit, String spigot, String forge, String neoforge) {

    public VersionInfo(Map<String, String> arguments) {
        this(arguments.get("mohist"), arguments.get("bukkit"), arguments.get("craftbukkit"), arguments.get("spigot"), arguments.get("forge"), arguments.get("neoforge"));
    }

}
