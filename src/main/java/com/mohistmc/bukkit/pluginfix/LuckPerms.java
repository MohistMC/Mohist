package com.mohistmc.bukkit.pluginfix;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.permissions.PermissibleBase;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/11 23:38:43
 */
public class LuckPerms {

    public static Map<UUID, PermissibleBase> perCache = new HashMap<>();
}
