package com.mohistmc.bukkit;

import com.mohistmc.util.i18n.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mohistmc.util.PluginsModsDelete.check;
import static com.mohistmc.util.PluginsModsDelete.webContent;

/**
 * @author Mgazul
 * @date 2020/3/22 23:15
 */
public class AutoDeletePlugins {

  public static List<String> classlist = new ArrayList<>();

  public static void jar() throws Exception {
    System.out.println(Message.getString("update.plugins"));
    String web = webContent("plugins");

    if (web != null){
      String[] line = web.split("\n");
      String[] sp1 = line[1].split("\": \"")[1].split("\"");
      Collections.addAll(classlist, sp1[sp1.length-2].split(","));

      String[] sp2 = line[2].split("\": \"")[1].split("\"");
      String[] l2 = sp2[sp2.length-2].split(",");

      for (String t : l2)
        check("plugins", t);
    }

    for (String t : classlist)
      check("plugins", t);
  }
}
