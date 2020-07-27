package red.mohist.forge;

import com.google.gson.JsonParser;
import java.io.File;
import java.io.InputStreamReader;
import red.mohist.util.ClassJarUtil;
import red.mohist.util.i18n.Message;

import static red.mohist.network.download.NetworkUtil.getInput;

public class AutoDeleteMods {
  private static String libDir = "mods";

  private static String[] getInfos(byte type) {
    String[] l = null;
    try {
      if (type == 1)
        l = new JsonParser().parse(new InputStreamReader(getInput("https://shawiizz.github.io/mods.json"))).getAsJsonObject().get("list").toString().replaceAll("\"", "").split(",");
      if (type == 2)
        l = new JsonParser().parse(new InputStreamReader(getInput("https://shawiizz.github.io/mods.json"))).getAsJsonObject().get("implemented").toString().replaceAll("\"", "").split(",");
    } catch (Throwable e) {
      if(type==1)
        l = new String[] {"org.spongepowered.mod.SpongeMod" /*SpongeForge*/,
          "org.dimdev.vanillafix.VanillaFix" /*VanillaFix*/,
          "lumien.custommainmenu.CustomMainMenu" /*CustomMainMenu*/,
          "com.performant.coremod.Performant" /*Performant*/,
          "shadows.fastbench.proxy.BenchServerProxy" /*FastWorkbench*/,
          "optifine.Differ" };
    }
    return l;
  }

  public static void jar(byte type) {
    if (!new File(libDir).exists()) new File(libDir).mkdir();
    try {
      if(type == 1) {
        System.out.println(Message.getString("update.mods"));
        for (String classname : getInfos((byte) 1))
          ClassJarUtil.checkOther(libDir, classname, false);
      } else {
        System.out.println(Message.getString("update.mods.implemented"));
        for (String classname : getInfos((byte) 2))
          ClassJarUtil.checkOther(libDir, classname, true);
      }
    } catch (Throwable ignored) {}
  }
}
