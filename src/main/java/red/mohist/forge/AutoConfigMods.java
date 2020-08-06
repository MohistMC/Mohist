package red.mohist.forge;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import red.mohist.network.download.UpdateUtils;
import red.mohist.util.FileUtil;
import red.mohist.util.i18n.Message;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static red.mohist.network.download.NetworkUtil.getInput;
import static red.mohist.util.ClassJarUtil.isClassExist;

public class AutoConfigMods {
  public static List<String> blCheck = new ArrayList<>();

  public static void startCheck() {
    System.out.println(Message.getString("update.config"));
    try {
      JsonElement root = new JsonParser().parse(new InputStreamReader(getInput("https://shawiizz.github.io/mods.json")));
      String str = root.getAsJsonObject().get("changeconfig").toString();
      for (String classname : str.substring(1).substring(0, str.length() - 2).replaceAll("\\\\", "").split(",")) {
        if(isClassExist(AutoDeleteMods.modsDir, classname.split("#")[0])) {
          blCheck.add(classname.split("#")[0]);
          File f = new File("config/" + classname.split("#")[1]);
          if(!f.exists()) {
            try {
              UpdateUtils.downloadFile(classname.split("#")[4], f);
              System.out.println(Message.getFormatString("update.configupdate", new Object[]{f.getName()}));
            } catch (Exception e) {
              blCheck.remove(classname.split("#")[0]);
              System.out.println(Message.getString("update.configupdate.failed"));
            }
          } else {
            if(FileUtil.readContent(f).contains(classname.split("#")[2])) {
              try {
                FileUtils.writeStringToFile(f, FileUtils.readFileToString(f).replace(classname.split("#")[2], classname.split("#")[3]));
                System.out.println(Message.getFormatString("update.configupdate", new Object[]{f.getName()}));
              } catch (Exception e) {
                blCheck.remove(classname.split("#")[0]);
                System.out.println(Message.getString("update.configupdate.failed"));
              }
            }
          }
        }
      }
    } catch (Exception e) {
      System.out.println(Message.getString("delete.json"));
    }
  }
}
