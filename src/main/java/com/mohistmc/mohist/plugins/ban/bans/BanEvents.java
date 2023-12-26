package com.mohistmc.mohist.plugins.ban.bans;

import com.mohistmc.mohist.MohistConfig;

/**
 * @author Mgazul by MohistMC
 * @date 2023/8/9 20:09:51
 */
public class BanEvents {

    public static boolean banFireTick() {
        return MohistConfig.doFireTick;
    }
}
