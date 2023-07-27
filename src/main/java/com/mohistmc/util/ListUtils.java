package com.mohistmc.util;

import java.util.List;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/27 14:37:28
 */
public class ListUtils {

    public static void isDuplicate(List<String> list, String key) {
        if (!list.contains(key)) {
            list.add(key);
        }
    }
}
