package com.mohistmc.dynamicenumutil;

public class MohistEnumHelper {

    public static <T extends Enum<?>> T addEnum0(Class<T> enumType, String enumName, Class<?>[] paramTypes, Object... paramValues) {
        return addEnum(enumType, enumName, paramTypes, paramValues);
    }

    public static <T extends Enum<?>> T addEnum(Class<T> enumType, String enumName, Class<?>[] paramTypes, Object[] paramValues) {
        return MohistJDK17EnumHelper.addEnum(enumType, enumName, paramTypes, paramValues);
    }
}
