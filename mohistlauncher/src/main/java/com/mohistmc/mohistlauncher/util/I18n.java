package com.mohistmc.mohistlauncher.util;

import com.mohistmc.mohistlauncher.Main;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/23 3:19:38
 */
public class I18n {

    public static String as(String key) {
        return Main.i18n.as(key);
    }

    public static String as(String key, Object... objects) {
        return Main.i18n.as(key, objects);
    }
}
