package red.mohist.util.i18n;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import red.mohist.configuration.MohistConfigUtil;

public class Message {

    private static ResourceBundle rb = ResourceBundle.getBundle("assets.mohist.lang.message", new Locale(getLanguage(), getCountry()), new UTF8Control());
    private static Map<String, String> msgs = Maps.newHashMap();

    public static String getString(String key) {
        // add map cache to cut back i/o
        if (!msgs.containsKey(key)) {// if the map not have the value
            String msg = rb.getString(key);// get string value
            if (!Strings.isNullOrEmpty(msg)) {// the value must be not a null or empty
                msgs.put(key, msg);// add value to map
            }
        }
        return msgs.get(key);
    }

    public static String getFormatString(String key, Object[] f) {
        return new MessageFormat(getString(key)).format(f);
    }

    public static String getLocale(int key) {
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

    public static String getLanguage() {
        return getLocale(1);
    }

    public static String getCountry() {
        return getLocale(2);
    }

    public static String getLocale() {
        return Message.rb.getLocale().toString();
    }
}
