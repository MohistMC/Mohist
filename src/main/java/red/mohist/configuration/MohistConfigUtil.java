package red.mohist.configuration;

import red.mohist.Mohist;
import red.mohist.util.FileUtil;
import red.mohist.util.NumberUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class MohistConfigUtil {

    public static String getString(String s, String key, String defaultreturn) {
        if (s.contains(key)) {
            String string = s.substring(s.indexOf(key));
            String s1 = (string.substring(string.indexOf(": ") + 2));
            String[] ss = s1.split("\n");
            return ss[0].trim().replace("'", "").replace("\"", "");
        }
        return defaultreturn;
    }

    public static String getString(File f, String key, String defaultreturn) {
        try {
            String s = FileUtil.readContent(f);
            if (s.contains(key)) {
                String string = s.substring(s.indexOf(key));
                String s1 = (string.substring(string.indexOf(": ") + 2));
                String[] ss = s1.split("\n");
                return ss[0].trim().replace("'", "").replace("\"", "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultreturn;
    }

    public static boolean getBoolean(File f, String key) {
        return !getString(f, key, "true").equals("false");
    }

    public static int getInt(File f, String key, String defaultreturn) {
        String s = getString(f, key, defaultreturn);
        if (NumberUtils.isInteger(s)) {
            return Integer.parseInt(s);
        }
        return Integer.parseInt(defaultreturn);
    }

    public static void copyMohistConfig() {
        try {
            File configfile = new File("mohist-config");
            if (!configfile.exists()) {
                configfile.mkdirs();

                Files.copy(Mohist.class.getClassLoader().getResourceAsStream("configurations/mohist.yml"), Paths.get("mohist-config", "mohist.yml"), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            System.out.println("File copy exception!");
        }
    }

}
