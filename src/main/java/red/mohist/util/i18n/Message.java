package red.mohist.util.i18n;

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
        return MohistConfigUtil.getString("lang:", "xx");
    }
}
