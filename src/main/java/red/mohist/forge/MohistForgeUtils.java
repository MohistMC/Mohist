package red.mohist.forge;

import java.util.Collection;
import red.mohist.configuration.MohistConfig;

public class MohistForgeUtils {

    public static boolean modsblacklist(String modslist) {
        String mbl = MohistConfig.instance.modsblacklist.getValue();
        String[] mod = mbl.split(";");
        Collection list = java.util.Arrays.asList(mod);
        for (Object str : list) {
            if(str.toString().length() > 0 && modslist.contains(str.toString())) return true;
        }
        return false;
    }
}
