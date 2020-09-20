package com.mohistmc.forge;

import com.mohistmc.util.i18n.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.mohistmc.util.PluginsModsDelete.check;
import static com.mohistmc.util.PluginsModsDelete.webContent;

/**
 * Why is there such a class?
 * Because we have included some MOD optimizations and modifications,
 * as well as some mods that are only used on the client, these cannot be loaded in Mohist
 */
public class AutoDeleteMods {
  public static final List<String> classlist = new ArrayList<>(Arrays.asList(
    "org.spongepowered.mod.SpongeMod" /*SpongeForge*/,
    "lumien.custommainmenu.CustomMainMenu" /*CustomMainMenu*/,
    "com.performant.coremod.Performant" /*Performant*/,
    "optifine.Differ" /*OptiFine*/,
    "ichttt.mods.firstaid.FirstAid" /*FirstAid*/,
    "guichaguri.betterfps.patches.misc.ServerPatch" /*BetterFps*/));

  public static void jar() throws Exception {
    System.out.println(Message.getString("update.mods"));
    String web = webContent("mods");

    if (web != null){
      String[] line = web.split("\n");
      String[] sp1 = line[1].split("\": \"")[1].split("\"");
      classlist.clear();
      Collections.addAll(classlist, sp1[sp1.length-2].split(","));
    }

    for (String t : classlist)
      if(!t.contains("fastbench"))
        check("mods", t);
  }
}
