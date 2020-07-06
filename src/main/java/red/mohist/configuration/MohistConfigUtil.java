package red.mohist.configuration;

import org.bukkit.configuration.file.YamlConfiguration;
import red.mohist.Mohist;
import red.mohist.util.IOUtil;
import red.mohist.util.NumberUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class MohistConfigUtil {

    public static String getString(String s, String key, String defaultreturn) {
        if(s.contains(key)) {
            String string = s.substring(s.indexOf(key));
            String s1 = (string.substring(string.indexOf(": ") + 2));
            String[] ss = s1.split("\n");
            return ss[0].trim().replace("'", "").replace("\"", "");
        }
        return defaultreturn;
    }

    public static String getString(File f, String key, String defaultreturn) {
        try {
            return getString(IOUtil.readContent(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8)), key, defaultreturn);
        } catch (IOException e) {
            return defaultreturn;
        }
    }

    public static boolean getBoolean(File f, String key) {
        return !getString(f, key, "true").equals("false");
    }

    public static int getInt(File f, String key, String defaultreturn) {
        String s = getString(f, key, defaultreturn);
        if(NumberUtils.isInteger(s)) return Integer.parseInt(s);
        return Integer.parseInt(defaultreturn);
    }

  public static void copyMohistConfig() {
    try {
      File configfile = new File("mohist-config/mohist.yml");
      if (!configfile.exists()) {
        configfile.mkdirs();
        Files.copy(Mohist.class.getClassLoader().getResourceAsStream("configurations/mohist.yml"), Paths.get("mohist-config", "mohist.yml"), StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (Exception e) {
      System.out.println("File copy exception!");
    }
  }

  public static boolean bMohist(String key) { return MohistConfigUtil.getBoolean(new File("mohist-config", "mohist.yml"), key+":"); }

  public static void setValueMohist(String oldValue, String value) {
    File mohistyml = new File("mohist-config", "mohist.yml");
    YamlConfiguration yml = YamlConfiguration.loadConfiguration(mohistyml);
    yml.set(oldValue, value);
    try {
      yml.save(mohistyml);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void setValueMohist(String oldValue, boolean value) {
    File mohistyml = new File("mohist-config", "mohist.yml");
    YamlConfiguration yml = YamlConfiguration.loadConfiguration(mohistyml);
    yml.set(oldValue, value);
    try {
      yml.save(mohistyml);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
