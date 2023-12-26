package com.mohistmc.mohist.plugins.ban;

import com.mohistmc.mohist.MohistConfig;
import java.util.List;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/27 15:10:47
 */
public class BanUtils {

    public static void saveToYaml(List<String> list, BanType banType) {
        MohistConfig.yml.set(banType.key, list);
        MohistConfig.save();
    }
}
