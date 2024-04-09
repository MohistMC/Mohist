/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2023.
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

package com.mohistmc.mohistlauncher.config;

import com.mohistmc.i18n.i18n;
import com.mohistmc.mohistlauncher.Main;
import com.mohistmc.yml.Yaml;
import com.mohistmc.yml.YamlSection;
import java.util.Locale;

public class MohistConfigUtil {

    public static final Yaml yaml = new Yaml("mohist-config/mohist.yml");
    public static YamlSection INSTALLATIONFINISHED;
    public static YamlSection CHECK_UPDATE_AUTO_DOWNLOAD;
    public static YamlSection CHECK_LIBRARIES;
    public static YamlSection LIBRARIES_DOWNLOADSOURCE;
    public static YamlSection CHECK_UPDATE;
    public static YamlSection MOHISTLANG;
    public static YamlSection SHOW_LOGO;

    public static void init() {
        try {
            yaml.load();
            INSTALLATIONFINISHED = yaml.put("mohist", "installation-finished").setDefValues(false);
            CHECK_UPDATE_AUTO_DOWNLOAD = yaml.put("mohist", "check_update_auto_download").setDefValues(false);
            CHECK_LIBRARIES = yaml.put("mohist", "libraries", "check").setDefValues(true);
            LIBRARIES_DOWNLOADSOURCE = yaml.put("mohist", "libraries", "downloadsource").setDefValues("AUTO");
            CHECK_UPDATE = yaml.put("mohist", "check_update").setDefValues(true);
            MOHISTLANG = yaml.put("mohist", "lang").setDefValues(Locale.getDefault().toString());
            SHOW_LOGO = yaml.put("mohist", "show_logo").setDefValues(true);
            yaml.save();
        } catch (Exception e) {
            System.out.println("File init exception!");
        }
    }

    public static void i18n() {
        Main.i18n = new i18n(Main.class.getClassLoader(), MOHISTLANG.asString());
    }

    public static void save() {
        try {
            yaml.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
