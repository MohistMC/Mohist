package com.mohistmc;

import static com.mohistmc.util.PluginsModsDelete.check;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mgazul
 * @date 2020/3/22 23:15
 */
public class AutoDeletePlugins {

    public static List<String> deletelist = new ArrayList<>(Arrays.asList(
        "com.comphenix.protocol.ProtocolLib" // Until it is resolved, the plug-in is not allowed
    ));

    public static void jar() throws Exception {
        for (String t : deletelist) {
            check("plugins", t);
        }
    }
}
