package com.mohistmc.util.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class Message {
    public static ResourceBundle rb = ResourceBundle.getBundle("assets.mohist.lang.message", new UTF8Control());

    public static String get(String key) {
        return rb.getString(key);
    }

    public static String get(String key, Object[] f) {
        return new MessageFormat(get(key)).format(f);
    }

    public static String getLocale() {
        return Message.rb.getLocale().toString();
    }

    public static boolean isCN(){
        TimeZone timeZone = TimeZone.getDefault();
        return timeZone.getID().equals("Asia/Shanghai") || Message.getLocale().contains("CN");
    }
}