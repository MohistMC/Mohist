package com.mohistmc.bukkit.pluginfix;

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
