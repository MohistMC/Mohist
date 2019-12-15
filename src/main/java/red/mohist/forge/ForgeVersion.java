package red.mohist.forge;

import java.util.Map;

public class ForgeVersion {

    public static boolean isCompatibleLowForge(Map<String, String> modList) {
        String forgeVersion = modList.get("forge");
        if (forgeVersion != null) {
            try {
                if (Integer.parseInt(forgeVersion.split("\\.")[3]) < 2826) {
                    return false;
                }
            } catch (Exception e) {
                return true;
            }
        }
        return true;
    }
}
