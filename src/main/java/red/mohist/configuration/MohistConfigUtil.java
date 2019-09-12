package red.mohist.configuration;

import java.io.File;
import java.io.IOException;
import red.mohist.util.FileUtil;

public class MohistConfigUtil {

    public static File f = new File("mohist-config", "mohist.yml");

    public static boolean getBoolean(String key, Boolean defaultreturn) {
        return getBoolean(key, 1, 6, defaultreturn);
    }
    public static boolean getBoolean(String key, int a, int b, Boolean defaultreturn) {
        try {
            String s = FileUtil.readContent(f, "UTF-8");
            if(s.contains(key)){
                String string = s.substring(s.indexOf(key));
                String s1 = (string.substring(string.indexOf(":") + 1)).substring(a, b);
                if (s1.equals("false")){
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultreturn;
    }

    public static String getString(String key, int a, int b, int c, int d, String defaultreturn) {
        try {
            String s = FileUtil.readContent(f, "UTF-8");
            if(s.contains(key)){
                String string = s.substring(s.indexOf(key));
                String s1 = (string.substring(string.indexOf(":") + 1)).substring(a, b);
                return s1.substring(c, d);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultreturn;
    }

    public static int getInt(String key, int a, int b, int c, int d, int defaultreturn) {
        try {
            String s = FileUtil.readContent(f, "UTF-8");
            if(s.contains(key)){
                String string = s.substring(s.indexOf(key));
                String s1 = (string.substring(string.indexOf(":") + 1)).substring(a, b);
                return Integer.valueOf(s1.substring(c, d)).intValue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultreturn;
    }
}
