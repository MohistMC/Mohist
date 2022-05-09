/*
 * MohistMC
 * Copyright (C) 2019-2022.
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

package com.mohistmc.util;

import javax.annotation.Nullable;

public class MohistEnumHelper {

    @Nullable
    public static <T extends Enum<? >> T addEnum0(Class<T> enumType, String enumName, Class<?>[] paramTypes, Object... paramValues)
    {
        return addEnum(enumType, enumName, paramTypes, paramValues);
    }

    @Nullable
    public static <T extends Enum<?>> T addEnum(Class<T> enumType, String enumName, Class<?>[] paramTypes, Object[] paramValues) {
        if (Float.parseFloat(System.getProperty("java.class.version")) != 52.0) {
            return MohistJDK9EnumHelper.addEnum(enumType, enumName, paramTypes, paramValues);
        } else {
            return MohistJDK8EnumHelper.addEnum(enumType, enumName, paramTypes, paramValues);
        }
    }
}
