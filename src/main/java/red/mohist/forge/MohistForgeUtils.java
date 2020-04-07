package red.mohist.forge;

import red.mohist.configuration.MohistConfig;

public class MohistForgeUtils {

    public static boolean modsblacklist(String modslist) {
        String[] mod = MohistConfig.instance.modsblacklist.getValue().split(";");
        for (Object str : mod) {
            if(str.toString().length() > 0 && !modslist.contains(str.toString())) return true;
        }
        return false;
    }
}
