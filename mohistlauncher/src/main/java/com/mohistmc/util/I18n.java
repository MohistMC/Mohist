package com.mohistmc.util;

import com.mohistmc.MohistMCStart;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/23 3:19:38
 */
public class I18n {

    public static String as(String key) {
        return MohistMCStart.i18n.as(key);
    }

    public static String as(String key, Object... objects) {
        return MohistMCStart.i18n.as(key, objects);
    }
}
