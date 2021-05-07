package com.mohistmc.util.i18n;

import com.mohistmc.config.MohistConfigUtil;
import com.mohistmc.util.NumberUtils;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class i18n {
    public static ResourceBundle rb = ResourceBundle.getBundle("lang.message", new Locale(getLanguage(), getCountry()), new UTF8Control());

    public static String get(String key) {
        return rb.getString(key);
    }

    public static String get(String key, Object... f) {
        return new MessageFormat(get(key)).format(f);
    }

    public static String getLocale(int key) {
        String locale = MohistConfigUtil.sMohist("lang", "xx_XX");
        if (locale.length() == 5) {
            if (key == 1) return locale.substring(0, 2);
            if (key == 2) return locale.substring(3, 5);
        }
        return "xx_XX";
    }

    public static String getLanguage() {
        return getLocale(1);
    }

    public static String getCountry() {
        return getLocale(2);
    }

    public static String getLocale() {
        return rb.getLocale().toString();
    }

    public static boolean isCN() {
        TimeZone timeZone = TimeZone.getDefault();
        return timeZone.getID().equals("Asia/Shanghai") || getLocale().contains("CN") || getCountry().contains("CN");
    }
}