package red.mohist.util.i18n;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import red.mohist.util.FileUtil;

public class Message {

    public static ResourceBundle rb = ResourceBundle.getBundle("assets.mohist.lang.message", new Locale(getLanguage(1), getLanguage(2)), new UTF8Control());

    public static String getString(String key) {
        return rb.getString(key);
    }

    public static String getFormatString(String key, Object[] f) {
        return new MessageFormat(getString(key)).format(f);
    }

    public static String getLanguage(int key) {
        try {
            File f = new File("mohist.yml");
            String s = FileUtil.readContent(f, "UTF-8");
            if(s.contains("lang: ")){
                // Using regular expressions
                String string = s.substring(s.indexOf("lang: "));
                String s1 = string.substring(string.indexOf(":") + 1);
                String s2 = s1.substring(1, 6);
                String language = s2.substring(0, 2);
                String country = s2.substring(3, 5);

                String locale = s2.substring(0, 5);
                if (key == 1){
                    return language;
                }
                if (key == 2){
                    return country;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Returns a value that does not exist to return the system default
        return "xx";
    }
}
