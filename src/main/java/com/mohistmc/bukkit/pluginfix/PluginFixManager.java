package com.mohistmc.bukkit.pluginfix;

import com.mohistmc.bukkit.pluginfix.fix.ActivePaperOptimization;
import com.mohistmc.bukkit.pluginfix.fix.MythicMobFix;
import com.mohistmc.bukkit.pluginfix.fix.WorldEditFix;
import com.mohistmc.configuration.MohistConfig;

public class PluginFixManager {

    public static byte[] injectPluginFix(String className, byte[] clazz) {
        if (ActivePaperOptimization.plugin_work_with_paper_optimization.contains(className)) {
            clazz = ActivePaperOptimization.activeIfItWorkWithPaperOptimization(clazz);
        }
        if (className.equals("com.sk89q.worldedit.bukkit.WorldEditPlugin") && MohistConfig.instance.enableworldeditfix.getValue()) {
            return WorldEditFix.replaceGetKeyByGetKeyForgeAndGetAsString(clazz);
        }
        if(className.equals("com.sk89q.worldedit.bukkit.BukkitAdapter") && MohistConfig.instance.enableworldeditfix.getValue()) {
            return WorldEditFix.replaceAdaptBlockType(WorldEditFix.replaceGetKeyByGetKeyForgeAndGetAsString(clazz));
        }
        if (className.equals("io.lumine.xikage.mythicmobs.volatilecode.v1_16_R3.VolatileWorldHandler_v1_16_R3")) {
            return MythicMobFix.patchVolatileWorldHandler(clazz);
        }
        if (className.endsWith("PaperLib")) {
            return ActivePaperOptimization.activePaperOptimization(clazz);
        }
        return clazz;
    }

}
