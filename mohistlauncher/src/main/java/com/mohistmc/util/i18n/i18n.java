/*
 * MohistMC
 * Copyright (C) 2018-2022.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.util.i18n;

import com.mohistmc.config.MohistConfigUtil;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class i18n {
    public static ResourceBundle rb;

    static {
        try {
            rb = ResourceBundle.getBundle("lang.message", new Locale(getLanguage(), getCountry()), new UTF8Control());
        } catch (UnsupportedOperationException e) {
            // ResourceBundle.Control is not supported in named modules.
            // See https://docs.oracle.com/javase/9/docs/api/java/util/ResourceBundle.html#bundleprovider for more details
            rb = ResourceBundle.getBundle("lang.message", new Locale(getLanguage(), getCountry()));
        }
    }

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
        return timeZone.getID().equals("Asia/Shanghai") || rb.getLocale().getCountry().equals("CN");
    }
}