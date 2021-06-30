package com.mohistmc.bukkit.pluginfix;

import com.mohistmc.bukkit.pluginfix.fix.ActivePaperOptimization;
import com.mohistmc.bukkit.pluginfix.fix.WorldEditFix;
import com.mohistmc.config.MohistConfigUtil;

public class PluginFixManager {

    public static byte[] injectPluginFix(String className, byte[] clazz) {
        if (ActivePaperOptimization.plugin_work_with_paper_optimization.contains(className)) {
            clazz = ActivePaperOptimization.activeIfItWorkWithPaperOptimization(clazz);
        }
        if (className.equals("com.sk89q.worldedit.bukkit.WorldEditPlugin") && MohistConfigUtil.bMohist("enable_worldedit_fix", "true")) {
            return WorldEditFix.replaceGetKeyByGetKeyForge(clazz);
        }
        if(className.equals("com.sk89q.worldedit.bukkit.BukkitAdapter") && MohistConfigUtil.bMohist("enable_worldedit_fix", "true")) {
            return WorldEditFix.replaceAdaptBlockType(clazz);
        }
        if (className.endsWith("PaperLib")) {
            return ActivePaperOptimization.activePaperOptimization(clazz);
        }
        return clazz;
    }

}
