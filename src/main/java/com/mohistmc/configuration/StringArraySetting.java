/*
 * Mohist - MohistMC
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

package com.mohistmc.configuration;

import java.util.ArrayList;
import java.util.HashSet;

public class StringArraySetting extends ArraySetting<String> {
    public StringArraySetting(ConfigBase config, String path, String def) {
        super(path, def, config);
    }

    @Override
    public void initArr(String values) {
        String[] vals = values.split(",");

        this.value_array = new ArrayList<>(vals.length);
        this.value_set = new HashSet<>(vals.length);

        for (String val : vals) {
            if (val.length() == 0) {
                continue;
            }
            this.value_array.add(val);
        }
        this.value_set.addAll(this.value_array);
    }
}
