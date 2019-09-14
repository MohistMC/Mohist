package red.mohist.configuration;

import java.io.File;
import java.io.IOException;
import red.mohist.util.FileUtil;
import red.mohist.util.Number;

public class MohistConfigUtil {

    public static File f = new File("mohist-config", "mohist.yml");

    public static String getString(String key, String defaultreturn) {
        try {
            String s = FileUtil.readContent(f, "UTF-8");
            if(s.contains(key)){
                String string = s.substring(s.indexOf(key));
                String s1 = (string.substring(string.indexOf(": ") + 2));
                String[] ss = s1.split("\n");
                return ss[0].trim().replace("'", "").replace("\"" , "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultreturn;
    }

    public static boolean getBoolean(String key, Boolean defaultreturn) {
        String s = getString(key, defaultreturn.toString());
        if (s.equals("false")){
            return false;
        }
        return defaultreturn;
    }

    public static int getInt(String key, String defaultreturn){
        String s = getString(key, defaultreturn);
        if (Number.isInteger(s)) {
            return Integer.parseInt(s);
        }
        return  Integer.parseInt(defaultreturn);
    }
}
