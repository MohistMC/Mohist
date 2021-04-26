package com.mohistmc.bukkit;

import static com.mohistmc.util.PluginsModsDelete.check;
import com.mohistmc.util.i18n.Message;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mgazul
 * @date 2020/3/22 23:15
 */
public class AutoDeletePlugins {

    public static List<String> deletelist = new ArrayList<>();

    public static List<String> updatelist = new ArrayList<>(Arrays.asList(
            "ch.njol.skript.Skript|2.5.3#https://github.com/UeberallGebannt/Skript/releases/download/2.5.3-1/Skript.jar", //Skript update
            "com.massivecraft.factions.Factions|mohist#https://cdn.discordapp.com/attachments/436907032352653312/698176876656590918/Factions.jar")); //Factions update

    public static void jar() throws Exception {
        System.out.println(Message.getString("update.plugins"));
        for (String t : updatelist)
            check("plugins", t);
        for (String t : deletelist)
            check("plugins", t);
    }
}
