package red.mohist.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import red.mohist.Mohist;
import red.mohist.util.IOUtil;
import red.mohist.util.NumberUtils;

public class MohistConfigUtil {
    public static File mohistyml = new File("mohist-config", "mohist.yml");

    public static String getString(String s, String key, String defaultreturn) {
        if(s.contains(key)) {
            String string = s.substring(s.indexOf(key));
            String s1 = (string.substring(string.indexOf(": ") + 2));
            String[] ss = s1.split("\n");
            return ss[0].trim().replace("'", "").replace("\"", "");
        } else return defaultreturn;
    }

    public static String getString(File f, String key, String defaultreturn) {
      StringBuilder s = new StringBuilder();
      try {
      for(String l : FileUtils.readLines(f, StandardCharsets.UTF_8))
        if(!l.startsWith("#"))
          s.append(l).append("\n");
      return getString(s.toString(), key, defaultreturn);
      } catch (IOException e) {
        return defaultreturn;
      }
    }

    public static boolean getBoolean(File f, String key) { return Boolean.parseBoolean(getString(f, key, "true")); }
    public static boolean getBoolean(File f, String key, String b) { return Boolean.parseBoolean(getString(f, key, b)); }

    public static int getInt(File f, String key, String defaultreturn) {
        String s = getString(f, key, defaultreturn);
        if(NumberUtils.isInteger(s)) return Integer.parseInt(s);
        return Integer.parseInt(defaultreturn);
    }

  public static void copyMohistConfig() {
    try {
      if (!mohistyml.exists()) {
        mohistyml.mkdirs();
        Files.copy(Mohist.class.getClassLoader().getResourceAsStream("configurations/mohist.yml"), mohistyml.toPath(), StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (Exception e) {
      System.out.println("File copy exception!");
    }
  }

  public static boolean bMohist(String key) { return getBoolean(mohistyml, key+":"); }
  public static boolean bMohist(String key, String defaultReturn) {
    return getBoolean(mohistyml, key, defaultReturn);
  }

  public static void setValueMohist(String oldValue, String value) {
    YamlConfiguration yml = YamlConfiguration.loadConfiguration(mohistyml);
    yml.set(oldValue, value);
    try {
      yml.save(mohistyml);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void setValueMohist(String oldValue, boolean value) {
    YamlConfiguration yml = YamlConfiguration.loadConfiguration(mohistyml);
    yml.set(oldValue, value);
    try {
      yml.save(mohistyml);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
