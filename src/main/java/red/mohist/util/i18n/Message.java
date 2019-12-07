package red.mohist.util.i18n;

import java.io.File;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import red.mohist.configuration.MohistConfigUtil;

public class Message {

    public static ResourceBundle rb = ResourceBundle.getBundle("assets.mohist.lang.message", new Locale(getLanguage(1), getLanguage(2)), new UTF8Control());

    public static String getString(String key) {
        return rb.getString(key);
    }

    public static String getFormatString(String key, Object[] f) {
        return new MessageFormat(getString(key)).format(f);
    }

    public static String getLanguage(int key) {
        File f = new File("mohist-config", "mohist.yml");
        String locale = MohistConfigUtil.getString(f, "lang:", "xx");
        if (locale.length() == 5) {
            String language = locale.substring(0, 2);
            String country = locale.substring(3, 5);
            if (key == 1) {
                return language;
            }
            if (key == 2) {
                return country;
            }
        }
        return "xx";
    }
}
