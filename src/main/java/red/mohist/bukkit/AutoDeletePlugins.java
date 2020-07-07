package red.mohist.bukkit;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import red.mohist.util.i18n.Message;

import java.io.File;
import java.io.InputStreamReader;

import static red.mohist.network.download.NetworkUtil.getInput;
import static red.mohist.util.ClassJarUtil.checkOther;
import static red.mohist.util.ClassJarUtil.checkPl;

public class AutoDeletePlugins {

  public static void jar() {
    System.out.println(Message.getString("update.plugins"));
    String libDir = "plugins";
    if (!new File(libDir).exists()) new File(libDir).mkdir();
    try {
      JsonElement root = new JsonParser().parse(new InputStreamReader(getInput("https://shawiizz.github.io/plugins.json")));
      for (String classname : root.getAsJsonObject().get("list").toString().replaceAll("\"", "").split(","))
        checkOther(libDir, classname, false);
      for (String classname : root.getAsJsonObject().get("listupdates").toString().replaceAll("\"", "").split(","))
        checkPl(classname.substring(0, classname.indexOf("|")), classname.substring(classname.indexOf("|") + 1, classname.indexOf("#")), classname.substring(classname.indexOf("#") + 1), "plugins");
      for (String classname : root.getAsJsonObject().get("listpluginstoforge").toString().replaceAll("\"", "").split(","))
        checkPl(classname.substring(0, classname.indexOf("|")), "=é", classname.substring(classname.indexOf("|") + 1), "mods");
    } catch (Throwable ignored) {
      System.out.println(Message.getString("delete.json"));
    }
  }
}
