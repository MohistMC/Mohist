package red.mohist.common.remap.proxy;

import java.lang.reflect.Field;
import red.mohist.common.remap.RemapUtils;

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
