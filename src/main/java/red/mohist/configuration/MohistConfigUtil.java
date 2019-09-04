package red.mohist.configuration;

import java.io.File;
import java.io.IOException;
import red.mohist.util.FileUtil;

public class MohistConfigUtil {

    public static boolean isBoolean(String key) {
        try {
            File f = new File("mohist.yml");
            String s = FileUtil.readContent(f, "UTF-8");
            if(s.contains(key)){
                String string = s.substring(s.indexOf(key));
                String s1 = string.substring(string.indexOf(":") + 1);
                String s2 = s1.substring(1, 5);
                String locale = s2.substring(0, 4);
                if (locale.equals("true")){
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
