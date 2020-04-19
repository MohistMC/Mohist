package red.mohist.bukkit.nms.proxy;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import red.mohist.bukkit.nms.remappers.RemapUtils;

/**
 *
 * @author pyz
 * @date 2019/7/6 10:09 AM
 */
public class ProxyField {

    private final static ConcurrentHashMap<Field, String> fieldGetNameCache = new ConcurrentHashMap<>();

    public static String getName(Field field) {
        if (!RemapUtils.isClassNeedRemap(field.getDeclaringClass(), false)) return field.getName();
        String cache = fieldGetNameCache.get(field);
        if (cache != null) return cache;
        String retn = RemapUtils.demapFieldName(field);
        fieldGetNameCache.put(field, retn);
        return retn;
    }
}
