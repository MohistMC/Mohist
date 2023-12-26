package com.mohistmc.mohist.util;

import com.mohistmc.mohist.Mohist;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/23 6:15:26
 */
public class I18n {

    public static String as(String key) {
        return Mohist.i18n.as(key);
    }

    public static String as(String key, Object... objects) {
        return Mohist.i18n.as(key, objects);
    }
}
