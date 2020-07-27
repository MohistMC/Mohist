package red.mohist.forge;

import java.util.Arrays;
import red.mohist.configuration.MohistConfig;

import static red.mohist.configuration.MohistConfigUtil.bMohist;

public class MohistForgeUtils {

  // Some mods such as Twilight Forest listen for specific events as their WorldProvider loads to hotload its dimension. This prevents this from happening so MV can create worlds using the same provider without issue.
  public static boolean craftWorldLoading = false;

  public static boolean modsblacklist(String modslist) {
        String[] clientMods = modslist.split(",");
        if(bMohist("enable_mods_whitelist")) {
          if(MohistConfig.instance.modsnumber.getValue()!=0)
            return !(Arrays.asList(modslist.split(",")).containsAll(Arrays.asList(MohistConfig.instance.modsblacklist.getValue().split(","))) && clientMods.length == MohistConfig.instance.modsnumber.getValue());
          else
            return !Arrays.asList(modslist.split(",")).containsAll(Arrays.asList(MohistConfig.instance.modsblacklist.getValue().split(",")));
        }
    return false;
  }
}
