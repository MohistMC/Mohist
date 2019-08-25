package red.mohist.common.asm.remap.proxy;

import red.mohist.common.asm.remap.RemapUtils;

import java.lang.reflect.Field;

/**
 *
 * @author pyz
 * @date 2019/7/6 10:09 AM
 */
public class ProxyField {
    public static String getName(Field field) {
        return RemapUtils.inverseMapFieldName(field.getDeclaringClass(), field.getName());
    }
}
