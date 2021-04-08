package com.mohistmc.forge;

import com.mohistmc.configuration.MohistConfig;

/**
 * @author Mgazul
 * @date 2020/4/15 0:30
 */
public class ModCompatibleFixUtils {

    /**
     * Fix NPE caused by persistent transmission when dimension does not exist
     * Adding the dimension id to mohist.yml/world.dimensionsNotLoaded triggers the error
     *
     * @param dim
     */
    public static void fixPortalEnter(int dim) {
        for (Integer dimyml : MohistConfig.instance.dimensionsNotLoaded) {
            if (dimyml.intValue() == dim) {
                return;
            }
        }
    }
}
