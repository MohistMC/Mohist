package red.mohist.forge;

import red.mohist.configuration.MohistConfig;
import red.mohist.configuration.MohistConfigUtil;

import java.io.File;

public class MohistForgeUtils {

    public static boolean modsblacklist(String modslist) {
        String[] mod = MohistConfig.instance.modsblacklist.getValue().split(";");
        if(!MohistConfigUtil.getBoolean(new File("mohist-config", "mohist.yml"), "disable_mods_whitelist:")) {
            for (Object str : mod) {
                if(str.toString().length() > 0 && !modslist.contains(str.toString())) return true;
            }
        }
        return false;
    }
}
