package red.mohist.reampper.proxy;

import red.mohist.reampper.utils.RemapUtils;

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
