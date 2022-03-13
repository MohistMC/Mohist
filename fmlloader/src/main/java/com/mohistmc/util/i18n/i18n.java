package com.mohistmc.util.i18n;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

public class i18n {
    private static ResourceBundle rb;
    private static List<String> a = Arrays.asList("en_us", "es_es", "fr_fr", "ru_ru", "zh_cn");
    public static List<String> b = Arrays.asList("fr_FR", "ru_RU", "zh_CN");

    public static String get(String key) {
        rb = ResourceBundle.getBundle("lang.message", new Locale(getLanguage(), getCountry()), new UTF8Control());
        return rb.getString(key);
    }

    public static String get(String key, Object... f) {
        MessageFormat mf = new MessageFormat(get(key));
        // TODO: fill params position with "{}" for MessageFormat when params is not enough.
        ArrayList<Object> list = new ArrayList<>(Arrays.stream(f).toList());
        list.addAll(new ArrayList<>(){{
            for (int i = 0; i < mf.getFormats().length - list.size(); i++) {
                add("{}");
            }
        }
        });
        return mf.format(list.toArray());
    }

    public static String getLocale(int key) {
        /*
        String locale = MohistConfigUtil.sMohist("lang", "xx_XX");
        if (locale.length() == 5) {
            if (key == 1) return locale.substring(0, 2);
            if (key == 2) return locale.substring(3, 5);
        }
         */
        return "xx";
    }

    public static String getVanillaLanguage() {
        /*
        String locale = MohistConfigUtil.sMohist("lang", "en_us");
        if (locale.length() == 5) {
            if (a.contains(locale.toLowerCase())) {
                return locale.toLowerCase();
            }
        }
         */
        return "en_us";
    }

    public static String getLanguage() {
        return getLocale(1);
    }

    public static String getCountry() {
        return getLocale(2);
    }

    public static String getLocale() {
        return getLanguage() + "_" + getCountry();
    }

    public static boolean isCN() {
        TimeZone timeZone = TimeZone.getDefault();
        return timeZone.getID().equals("Asia/Shanghai") || getLocale().contains("CN") || getCountry().contains("CN");
    }
}