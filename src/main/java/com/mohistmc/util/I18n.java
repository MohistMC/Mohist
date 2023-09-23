package com.mohistmc.util;

import com.mohistmc.MohistMC;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/23 6:15:26
 */
public class I18n {

    public static String as(String key) {
        return MohistMC.i18n.as(key);
    }

    public static String as(String key, Object... objects) {
        return MohistMC.i18n.as(key, objects);
    }
}
