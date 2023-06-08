package com.mohistmc.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;

/**
 * @author Mgazul by MohistMC
 * @date 2023/5/25 21:28:44
 */
public class Level2LevelStem {

    public static ResourceKey<LevelStem> getTypeKey(Level p_53026_) {
        if (p_53026_.dimension() == Level.END) {
            return LevelStem.END;
        } else if (p_53026_.dimension() == Level.NETHER) {
            return LevelStem.NETHER;
        }else  {
            return LevelStem.OVERWORLD;
        }
    }
}
