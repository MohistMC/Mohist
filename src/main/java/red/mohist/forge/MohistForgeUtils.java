package red.mohist.forge;

import java.io.File;
import java.util.Collection;
import red.mohist.configuration.MohistConfig;
import red.mohist.configuration.MohistConfigUtil;

public class MohistForgeUtils {

    public static boolean modsblacklist(String modslist) {
        String[] mod = MohistConfig.instance.modsblacklist.getValue().split(";");
        if(!MohistConfigUtil.getBoolean(new File("mohist-config", "mohist.yml"), "disable_mods_whitelist:")) {
            Collection list = java.util.Arrays.asList(mod);
            for (Object str : list) {
                if(str.toString().length() > 0 && modslist.contains(str.toString())) return true;
            }
        }
        return false;
    }
}
