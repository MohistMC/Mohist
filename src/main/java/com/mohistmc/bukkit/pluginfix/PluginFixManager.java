package com.mohistmc.bukkit.pluginfix;

import com.mohistmc.bukkit.pluginfix.fix.WorldEditFix;

public class PluginFixManager {

    public static byte[] injectPluginFix(String className, byte[] clazz) {
        if (className.equals("com.sk89q.worldedit.bukkit.WorldEditPlugin")) {
            return WorldEditFix.replaceGetKeyByGetKeyForge(clazz);
        }
        if(className.equals("com.sk89q.worldedit.bukkit.BukkitAdapter")) {
            return WorldEditFix.replaceAdaptBlockType(clazz);
        }
        return clazz;
    }

}
