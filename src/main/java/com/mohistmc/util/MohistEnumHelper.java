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
