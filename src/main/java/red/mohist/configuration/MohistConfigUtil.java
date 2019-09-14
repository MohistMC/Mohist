package red.mohist.configuration;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import red.mohist.util.FileUtil;
import red.mohist.util.Number;

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

    public static String getStringInt(String key, String defaultreturn) {
        try {
            String s = FileUtil.readContent(f, "UTF-8");
            if(s.contains(key)){
                String string = s.substring(s.indexOf(key));
                String s1 = (string.substring(string.indexOf(": ") + 2));
                String[] ss = s1.split("\n");
                String regEx="[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(ss[0]);
                return m.replaceAll("").trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultreturn;
    }

    public static int getInt(String key, String defaultreturn){
        String s = getStringInt(key, defaultreturn);
        if (Number.isInteger(s)) {
            return Integer.parseInt(s);
        }
        return  Integer.parseInt(defaultreturn);
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
